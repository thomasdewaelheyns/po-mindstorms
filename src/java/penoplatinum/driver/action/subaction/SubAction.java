package penoplatinum.driver.action.subaction;

import penoplatinum.robot.RobotAPI;

/**
 *
 * @author Rupsbant
 */
public interface SubAction {

  public boolean isBusy();

  public void work(RobotAPI api);

  public SubAction getNextSubAction();
    
}
