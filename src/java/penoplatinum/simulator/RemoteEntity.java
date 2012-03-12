package penoplatinum.simulator;

import java.awt.Point;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import penoplatinum.Config;
import penoplatinum.agent.MQ;
import penoplatinum.bluetooth.CircularQueue;
import penoplatinum.pacman.GhostModel;
import penoplatinum.pacman.GhostProtocolCommandHandler;
import penoplatinum.pacman.GhostProtocolHandler;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.view.ViewRobot;

public class RemoteEntity implements RobotEntity {

  public final double LENGTH_ROBOT = 10.0;
  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction it's facing
  private Simulator simulator;
  private CircularQueue<String> messageQueue = new CircularQueue<String>(1000); //TODO: warning this can crash
  private String entityName;
  private GhostProtocolHandler protocol;
  private int originX;
  private int originY;
  private int originDirection;

  public RemoteEntity(final String entityName) {
    this.entityName = entityName;
    final GhostModel ghostModel = new GhostModel("RemoteEntity-" + entityName);

    createGhostProtocolHandler(ghostModel);

    try {
      MQ mq = new MQ() {


        @Override
        protected void handleIncomingMessage(String message) {
           synchronized (this) {
            if (message == null) {
              throw new RuntimeException("Impossible??");
            }
            messageQueue.insert(message);
          }
        }
      }.connectToMQServer(Config.MQ_SERVER).follow(Config.GHOST_CHANNEL);
    } catch (IOException ex) {
      Logger.getLogger(RemoteEntity.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InterruptedException ex) {
      Logger.getLogger(RemoteEntity.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

  public RemoteEntity setOrigin(int originX, int originY, int originDirection) {
    this.originX = originX;
    this.originY = originY;
    this.originDirection = originDirection;
    return this;
  }

  private void createGhostProtocolHandler(final GhostModel ghostModel) {
    this.protocol = new GhostProtocolHandler(ghostModel.getAgent(), ghostModel, new GhostProtocolCommandHandler() {

      @Override
      public void handleBarcode(String agentName, int code, int bearing) {
      }

      @Override
      public void handleDiscover(String agentName, int x, int y, int n, int e, int s, int w) {
      }

      @Override
      public void handlePosition(String agentName, int x, int y) {
        penoplatinum.map.Point p = Bearing.mapToNorth(originDirection, x, y);
        positionX = (p.getX() + originX) * Sector.SIZE + Sector.SIZE / 2;
        positionY = (p.getY() + originY) * Sector.SIZE + Sector.SIZE / 2;
      }

      @Override
      public void handlePacman(String agentName, int x, int y) {
        
      }
    });
  }

  public void useSimulator(Simulator simulator) {
    this.simulator = simulator;
  }

  public void setPostition(double positionX, double positionY, double direction) {
    this.positionX = positionX;
    this.positionY = positionY;
    this.direction = direction;
  }

  /**
   * A robot is put on the map - as in the real world - on a certain place
   * and in a given direction.
   * The Simulator also instruments the robot with a RobotAPI and sets up
   * the RobotAgent to interact with the robot.
   */
  public RemoteEntity putRobotAt(Robot robot, int x, int y, int direction) {
    this.positionX = x;
    this.positionY = y;
    this.direction = direction;


    return this;
  }

  public double getPosX() {
    return positionX;
  }

  public double getPosY() {
    return positionY;
  }

  public double getDir() {
    return direction;
  }

  public ViewRobot getViewRobot() {
    return new RemoteViewRobot(this);
  }

  /**
   * Our internal representation of the Bearing uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  public int getAngle() {
    return (int) ((this.direction + 90) % 360);
  }

  // performs the next step in the movement currently executed by the robot
  public void step() {
    // Process messages in the queue
    synchronized (this) {
      while (!messageQueue.isEmpty()) {
        String msg = messageQueue.remove();
        protocol.receive(msg);
      }

    }
  }

  public Point getCurrentTileCoordinates() {
    // determine tile coordinates we're on
    int left = (int) (this.positionX / simulator.getTileSize()) + 1;
    int top = (int) (this.positionY / simulator.getTileSize()) + 1;
    return new Point(left, top);
  }

  public Point getCurrentOnTileCoordinates() {
    // determine tile coordinates on the tile we're on
    int left = (int) (this.positionX % simulator.getTileSize());
    int top = (int) (this.positionY % simulator.getTileSize());
    return new Point(left, top);
  }
}