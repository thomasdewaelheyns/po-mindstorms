package penoplatinum.driver.action.subaction;

import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.robot.RobotAPI;
import penoplatinum.util.LightColor;

// Step 2: turns to the right until a white line is encountered. the angle
public class FindRightWhiteLine implements SubAction {
  public static final int MIN_RETURNANGLE = -20;

  private AlignDriverAction context;
  private int originalAngle;
  private int start;
  private int state;

  public FindRightWhiteLine(AlignDriverAction context, int originalAngle) {
    this.context = context;
    this.originalAngle = originalAngle;
    this.start = (int) this.context.getSensorPart().getTotalTurnedAngle();
  }

  public boolean isBusy() {
    return state < 2;
  }

  public void work(RobotAPI api) {
    // if we're not turning ... start turning
    if (state == 0) {
      api.turn(- AlignDriverAction.SWEEP_ANGLE * 2);
      state = 1;
    } else if (state == 1) {
      if (getCurrentAngle() <= MIN_RETURNANGLE && this.context.getLightPart().getCurrentLightColor() == LightColor.WHITE) {
        state = 2;
      } else if (!this.context.getSensorPart().isMoving()) {
        state = 3;
      }
    }
  }

  public SubAction getNextSubAction() {
    // turn back half the turn we made to get back to a white line
    return (state == 2 ? new TurnToAlign(this.context, -(this.getCurrentAngle() / 2)) : 
                         new ReturnSubAction(context, originalAngle));
  }

  private int getCurrentAngle() {
    return (int) (this.context.getSensorPart().getTotalTurnedAngle() - this.start);
  }
}
