package penoplatinum.driver.action.align;

import penoplatinum.robot.RobotAPI;

public interface SubAction {

  public boolean isBusy();

  public void work(RobotAPI api);

  public SubAction getNextSubAction();
    
}
