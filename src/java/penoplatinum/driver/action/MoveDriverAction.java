package penoplatinum.driver.action;

/**
 * Moves a given distance, expressed in meters, forward
 * 
 * @author: Team Platinum
 */

import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;
import penoplatinum.model.part.SensorModelPart;


public class MoveDriverAction implements DriverAction {

  // a referece to the ModelPart dealing with sensors, which we need to
  // determine if we're still moving == executing the instructed move()
  private SensorModelPart sensors;

  // the distance we cover in one move
  private double distance = 0;
  
  // flag to keep track if we have already started this action
  private boolean actionStarted = false;
  
  
  public MoveDriverAction(Model model) {
    this.sensors = SensorModelPart.from(model);
  }

  public boolean isBusy() {
    return this.actionStarted ? this.sensors.isMoving() : true;
  }

  public boolean canBeInterrupted() {
    return true;
  }

  public boolean interrupts() {
    return false;
  }

  public MoveDriverAction set(double distance) {
    this.distance = distance;
    return this;
  }

  public MoveDriverAction work(RobotAPI api) {
    if( ! this.actionStarted ) {
      api.move(this.distance);
      this.actionStarted = true;
    }
    return this;
  }
}
