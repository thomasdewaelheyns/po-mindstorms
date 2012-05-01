package penoplatinum.simulator.entities;

import penoplatinum.simulator.view.ViewRobot;
import penoplatinum.simulator.RobotEntity;

public class PacmanEntity implements RobotEntity {

  private double posX;
  private double posY;
  private double dir;

  public PacmanEntity(double posX, double posY, double dir) {
    this.posX = posX;
    this.posY = posY;
    this.dir = dir;
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
}
