package penoplatinum.driver.action.subaction;

import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.robot.RobotAPI;
import penoplatinum.util.LightColor;

public class FindLeftWhiteLine implements SubAction {

  private AlignDriverAction context;
  private int startAngle;
  private int state;

  public FindLeftWhiteLine(AlignDriverAction context) {
    this.context = context;
    this.startAngle = (int) context.getSensorPart().getTotalTurnedAngle();
  }

  public boolean isBusy() {
    return state < 2;
  }

  public void work(RobotAPI api) {
    // if we're not turning ... start turning
    if (state > 0) {
      if (context.getLightPart().getCurrentLightColor() == LightColor.WHITE) {
        state = 2;
      } else if (!context.getSensorPart().isMoving()) {
        state = 3;
      }
    } else if (!context.getSensorPart().isTurning()) {
      api.turn(AlignDriverAction.SWEEP_ANGLE);
      state = 1;
    }
  }

  public SubAction getNextSubAction() {
    return (state == 2 ? new FindRightWhiteLine(this.context, startAngle) : new ReturnSubAction(this.context, startAngle));
  }
}
