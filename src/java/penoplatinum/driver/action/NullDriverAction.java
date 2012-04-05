package penoplatinum.actions;

/**
 * In this state, the robot does absolutely nothing. It won't even change its
 * state.
 * 
 * @author: Team Platinum
 */

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;


public class NullAction extends ActionSkeleton {

  public NullAction execute(RobotAPI api) {
    return this;
  }

  public boolean isBusy() {
    return false;
  }
}
