package penoplatinum.driver.action.subaction;

import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.robot.RobotAPI;

// Step 3: turns a given angle to align again to the crossed white line
public class TurnToAlign implements SubAction {
  private AlignDriverAction context;
  private int correction;
  private boolean started = false;

  public TurnToAlign(AlignDriverAction context, int correction) {
    this.context = context;
    this.correction = correction;
  }

  public boolean isBusy() {
    return !started || this.context.getSensorPart().isTurning();
  }

  public void work(RobotAPI api) {
    // if we're not turning ... start turning
    if (!this.started) {
      api.turn(this.correction);
      this.started = true;
    }
  }

  public SubAction getNextSubAction() {
    return null;
  }
    
}
