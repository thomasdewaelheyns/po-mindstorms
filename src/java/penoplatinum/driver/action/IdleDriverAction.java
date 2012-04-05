package penoplatinum.driver.action;

/**
 * In Idle state, the robot does nothing.
 * 
 * @author: Team Platinum
 */

import penoplatinum.robot.RobotAPI;


public class IdleDriverAction implements DriverAction  {

  // the robot is idle ... doing nothing ... so it's NOT busy
  public boolean isBusy() {
    return false;
  }

  // the robot is idle ... doing nothing ... so it CAN'T be interrupted and
  // therefore CAN be interrupted
  public boolean canBeInterrupted() {
    return true;
  }

  // the robot is idle ... doing nothing ...
  public boolean interrupts() {
    return false;
  }

  // the robot is idle ... doing nothing ...
  public IdleDriverAction work(RobotAPI api) {
    // do nothing
    return this;
  }

}
