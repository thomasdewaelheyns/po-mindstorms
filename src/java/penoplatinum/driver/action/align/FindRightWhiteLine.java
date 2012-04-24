package penoplatinum.driver.action.align;

import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.robot.RobotAPI;

public class FindRightWhiteLine implements SubAction {

  private AlignDriverAction context;
  private int correction;
  private boolean started = false;

  public FindRightWhiteLine(AlignDriverAction context, int correction) {
    this.context = context;
    this.correction = correction;
  }

  public boolean isBusy() {
    return this.context.getSensors().isTurning();
  }

  public void work(RobotAPI api) {
    // if we're not turning ... start turning
    if (!this.started) {
      api.turn(this.correction);
      this.started = true;
    }
  }

  public SubAction getNextSubAction() {
    // this is the end ...
    return this;
  }
}