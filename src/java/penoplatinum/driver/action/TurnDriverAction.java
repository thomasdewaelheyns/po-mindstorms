package penoplatinum.driver.action;

/**
 * TurnDriverAction
 * 
 * Turns a given angle, expressed in degrees.
 * 
 * @author: Team Platinum
 */
import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;
import penoplatinum.model.part.SensorModelPart;

public class TurnDriverAction implements DriverAction, Cloneable {

  // a referece to the ModelPart dealing with sensors, which we need to
  // determine if we're still moving == executing the instructed move()
  private SensorModelPart sensors;
  // the angle we're turning
  private int angle = 0;
  // flag to keep track if we have already started this action
  private boolean actionStarted = false;

  private TurnDriverAction() {
  }

  public TurnDriverAction(Model model) {
    this.sensors = SensorModelPart.from(model);
  }

  public boolean isBusy() {
    return this.actionStarted ? this.sensors.isTurning() : true;
  }

  public boolean canBeInterrupted() {
    return true;
  }

  public boolean interrupts() {
    return false;
  }

  public TurnDriverAction set(int angle) {
    this.angle = angle;
    return this;
  }

  public TurnDriverAction work(RobotAPI api) {
    if (!this.actionStarted) {
      api.turn(this.angle);
      this.actionStarted = true;
    }
    return this;
  }

  public TurnDriverAction clone() {
    TurnDriverAction out = new TurnDriverAction();
    out.angle = this.angle;
    out.sensors = this.sensors;
    out.actionStarted = false;
    return out;
  }
}
