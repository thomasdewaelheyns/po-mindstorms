package penoplatinum.driver.action.align;

import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.robot.RobotAPI;

public class FindLeftWhiteLine implements SubAction {

  private AlignDriverAction context;

  public FindLeftWhiteLine(AlignDriverAction context) {
    this.context = context;
  }

  public boolean isBusy() {
    //return this.context.getLight().getLightColor != LightColor.WHITE;
    return false;
  }

  public void work(RobotAPI api) {
    // if we're not turning ... start turning
    /*if( ! this.context.getSensors().isTurning() ) {
    api.turn(-SWEEP_ANGLE);
    }/**/
  }

  public SubAction getNextSubAction() {
    return new FindLeftWhiteLineEnd(this.context);
  }
}
