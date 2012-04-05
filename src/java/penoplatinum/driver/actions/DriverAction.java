package penoplatinum.driver.actions;

/**
 * Action interface defines an interface for actions that are executed by
 * a Driver
 * 
 * @author Team Platinum
 */

import penoplatinum.robot.RobotAPI;


public interface DriverAction {
  public Action execute(RobotAPI api);

  public boolean isBusy();

  public boolean isInterruptable();
  public DriverAction makeNotInterruptable();
}
