package penoplatinum.driver.action;

/**
 * Action interface defines an interface for actions that are executed by
 * a Driver
 * 
 * @author Team Platinum
 */

import penoplatinum.robot.RobotAPI;


public interface DriverAction {
  // indicates that this action has not finished yet
  public boolean isBusy();

  // indicates that this action can be interrupted. this is typically the
  // case for basic movement action, but not for corrective actions
  public boolean canBeInterrupted();

  // indicates whether this action interrupts any previous action
  public boolean interrupts();

  // perform the actual action, using the supploed robot API
  public DriverAction work(RobotAPI api);
}
