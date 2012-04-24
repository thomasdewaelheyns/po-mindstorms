package penoplatinum.driver.action.align;

import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.robot.RobotAPI;

public class FindRightWhiteLineEnd implements SubAction {

  AlignDriverAction context;
  private int start;

  public FindRightWhiteLineEnd(AlignDriverAction context) {
    this.context = context;
    //this.start = this.context.getSensor().getTotalTurnedAngle();
  }

  public boolean isBusy() {
    // wait until we have turned at least 10 degrees (so we're off the white
    // line again).
    // FIXME: this isn't nice ... but it will do for now ;-)
    return this.getCurrentAngle() < 10; /*||
    this.context.getLight().getCurrentLightColor() != LightColor.WHITE;/**/
  }

  public void work(RobotAPI api) {
    // if we're not turning ... start turning
    /*if( ! this.context.getSensors().isTurning() ) {
    api.turn(-(SWEEP_ANGLE * 2));
    }/**/
  }

  public SubAction getNextSubAction() {
    // turn back half the turn we made to get back to a white line
    return new TurnToAlign(this.context, -(this.getCurrentAngle() / 2));
  }

  int getCurrentAngle() {
    //return this.context.getSensor().getTotalTurnedAngle() - this.start;
    return 0;
  }
}
