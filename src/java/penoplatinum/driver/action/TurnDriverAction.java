package penoplatinum.driver.action;

/**
 * Moves a given distance, expressed in meters, forward.
 * 
 * @author: Team Platinum
 */

import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;
import penoplatinum.model.part.SensorModelPart;


public class TurnDriverAction implements DriverAction {

  // a referece to the ModelPart dealing with sensors, which we need to
  // determine if we're still moving == executing the instructed move()
  private SensorModelPart sensors;

  // the distance we cover in one move
  private int angle = 0;
  
  // flag to keep track if we have already started this action
  private boolean actionStarted = false;
  
  
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
    api.turn(this.angle);
    this.actionStarted = true;
    return this;
  }
}
