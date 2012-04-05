package penoplatinum.driver.actions;

/**
 * This implements a skeleton for an action to drive the robot with or gather
 * inputdata.
 * Actions can be non-interruptable.
 * 
 * @author: Team Platinum
 */

import penoplatinum.simulator.Model;


public abstract class DriverActionSkeleton implements DriverAction {

  private boolean isInterruptable = true;

  public boolean isInterruptable() {
    return this.isInterruptable;
  }

  public ActionSkeleton makeNotInterruptable() {
    this.isInterruptable = false;
    return this;
  }
}
