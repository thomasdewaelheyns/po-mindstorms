package penoplatinum.simulator.entities;

import java.io.IOException;
import penoplatinum.Config;
import penoplatinum.gateway.MQ;
import penoplatinum.gateway.MessageReceiver;
import penoplatinum.gateway.Queue;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.view.ViewRobot;
import penoplatinum.simulator.RobotEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.Scanner;

public class RemoteEntity implements RobotEntity {

  public final double LENGTH_ROBOT = 10.0;
  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction it's facing
  private Simulator simulator;
  private Queue queue;
  private String entityName;
  private int originX;
  private int originY;
  private int originDirection;

  public RemoteEntity(final String entityName) throws IOException, InterruptedException {
    this.entityName = entityName;
    this.queue = new MQ().connectToMQServer(Config.MQ_SERVER)
                         .follow(Config.GHOST_CHANNEL);
    this.queue.subscribe(new RemoteEntityProtocolSpy());
  }

  public RemoteEntity setOrigin(int originX, int originY, int originDirection) {
    this.originX = originX;
    this.originY = originY;
    this.originDirection = originDirection;
    return this;
  }

  public void useSimulator(Simulator simulator) {
    this.simulator = simulator;
  }

  public double getPosX() {
    return positionX;
  }

  public double getPosY() {
    return positionY;
  }

  public double getDirection() {
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

  public String getEntityName() {
    return this.entityName;
  }

  private class RemoteEntityProtocolSpy implements MessageReceiver {

    @Override
    public void receive(String msg) {
      // strip off the newline
      msg = msg.substring(0, msg.length() - 1);
      Scanner scanner = new Scanner(msg);
      String agentName = scanner.next();
      if (!agentName.equals(getEntityName())) {
        return;
      }
      String command = scanner.next();
      if (!command.equals("POSITION")) {
        return;
      }
      int x = scanner.nextInt();
      int y = scanner.nextInt();
      Rotation r = Rotation.NONE;
      for (int i = 0; i < originDirection; i++) {
        r = r.add(Rotation.L90); //TODO Check this orientation
      }
      Point p = new Point(x, y).rotate(r);
      positionX = (p.getX() + originX) * Sector.SIZE + Sector.SIZE / 2;
      positionY = (p.getY() + originY) * Sector.SIZE + Sector.SIZE / 2;
    }
  }
}