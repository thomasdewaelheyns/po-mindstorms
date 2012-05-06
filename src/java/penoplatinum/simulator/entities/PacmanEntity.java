package penoplatinum.simulator.entities;

import java.io.IOException;
import penoplatinum.Config;
import penoplatinum.gateway.MQ;
import penoplatinum.gateway.MessageReceiver;
import penoplatinum.simulator.view.ViewRobot;
import penoplatinum.simulator.RobotEntity;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.Scanner;

public class PacmanEntity implements RobotEntity {
  private int originX = 0;
  private int originY = 0;
  private int originD = 0;

  private double posX;
  private double posY;
  private double dir;
  private final MQ queue;

  public PacmanEntity(double posX, double posY, double dir) {
    this.posX = posX;
    this.posY = posY;
    this.dir = dir;
    this.queue = null;
  }

  public PacmanEntity() throws IOException, InterruptedException {
    this.queue = new MQ().connectToMQServer(Config.MQ_SERVER)
                         .follow(Config.GHOST_CHANNEL);
    this.queue.subscribe(new PacmanProtocolSpy());
  }

  @Override
  public void step() {
    //does nothing
  }

  @Override
  public ViewRobot getViewRobot() {
    return new PacmanViewRobot(this);
  }

  @Override
  public double getDirection() {
    return dir;
  }

  @Override
  public double getPosX() {
    return posX;
  }

  @Override
  public double getPosY() {
    return posY;
  }

  public String getEntityName() {
    return "pacman";
  }

  private class PacmanProtocolSpy implements MessageReceiver {

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
      for (int i = 0; i < originD; i++) {
        r = r.add(Rotation.L90); 
      }
      Point p = new Point(x, y).rotate(r);
      posX = (p.getX() + originX) * Sector.SIZE + Sector.SIZE / 2;
      posY = (-p.getY() + originY) * Sector.SIZE + Sector.SIZE / 2;
    }
  }
}
