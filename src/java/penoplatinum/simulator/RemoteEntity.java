package penoplatinum.simulator;

import java.awt.Point;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import penoplatinum.agent.MQ;
import penoplatinum.bluetooth.CircularQueue;
import penoplatinum.simulator.view.ViewRobot;

public class RemoteEntity implements RobotEntity {

  public final double LENGTH_ROBOT = 10.0;
  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction it's facing
  SimulationRobotAgent robotAgent;  // the communication layer
  private Simulator simulator;
  private MQ mq;
  private CircularQueue<String> messageQueue = new CircularQueue<String>(1000); //TODO: warning this can crash
  private String entityName;

  public RemoteEntity(final String entityName) {
    this.robotAgent = robotAgent;
    this.entityName = entityName;
    try {
      mq = new MQ() {

        @Override
        protected void handleIncomingMessage(String sender, String message) {
          synchronized (this) {
            if (!sender.equals(entityName)) {
              return;
            }
            if (message == null) {
              throw new RuntimeException("Impossible??");
            }
            messageQueue.insert(message);
          }
        }
      }.setMyName(entityName + "Remote").connectToMQServer().follow("ghost-protocol");
    } catch (IOException ex) {
      Logger.getLogger(RemoteEntity.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InterruptedException ex) {
      Logger.getLogger(RemoteEntity.class.getName()).log(Level.SEVERE, null, ex);
    }

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
   * Our internal representation of the baring uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  public int getAngle() {
    return (int) ((this.direction + 90) % 360);
  }
  LinkedBlockingQueue<String> tempQueue = new LinkedBlockingQueue<String>();

  // performs the next step in the movement currently executed by the robot
  public void step() {
    // Process messages in the queue
    synchronized (this) {
      while (!messageQueue.isEmpty()) {
        String msg = messageQueue.remove();
        try {
          //TODO: Temp message protocol
          if (msg.charAt(0) == 'p') {
            // Position msg
            String[] coords = msg.substring(1).split(",");
            positionX = Integer.parseInt(coords[0]);
            positionY = Integer.parseInt(coords[1]);
            direction = Integer.parseInt(coords[2]);
            tempQueue.add(msg);
          }

        } catch (Exception e) {
          System.out.println("Message error in RemoteEntity!! " + entityName);
        }




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