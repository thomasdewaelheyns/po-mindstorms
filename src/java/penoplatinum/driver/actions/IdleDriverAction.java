package penoplatinum.actions;

/**
 * In Idle state, the robot does nothing, but acts upon events, changing its
 * state to a more active one ;-)
 * 
 * @author: Team Platinum
 */

import penoplatinum.robot.RobotAPI;


public class IdleAction extends ActionSkeleton {

  public IdleAction execute(RobotAPI api) {
    return this;
  }
  
  

  public boolean isBusy() {
    return false;
  }
}
