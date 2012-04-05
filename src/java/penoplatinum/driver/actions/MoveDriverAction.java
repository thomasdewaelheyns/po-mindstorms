package penoplatinum.actions;

/**
 * Moves a given distance, expressed in meters, forward
 * 
 * @author: Team Platinum
 */

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;


public class MoveAction extends ActionSkeleton {

  private boolean actionStarted = false;

  public Action execute(RobotAPI api) {
    
  }

  public boolean isBusy() {
    return (!this.actionStarted) || this.getModel().getSensorPart().isMoving();
  }
}
