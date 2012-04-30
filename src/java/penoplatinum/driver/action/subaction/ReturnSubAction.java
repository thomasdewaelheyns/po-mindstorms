package penoplatinum.driver.action.subaction;

import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.robot.RobotAPI;

class ReturnSubAction implements SubAction {
  private int turnAngle;
  private boolean started;
  private AlignDriverAction context;

  public ReturnSubAction(AlignDriverAction context, int startAngle) {
    this.context = context;
    this.turnAngle = (int) (startAngle - context.getSensorPart().getTotalTurnedAngle());
  }

  @Override
  public boolean isBusy() {
    return !started || context.getSensorPart().isMoving();
  }

  @Override
  public SubAction getNextSubAction() {
    return null;
  }

  @Override
  public void work(RobotAPI api) {
    if (!started) {
      started = true;
      api.turn(turnAngle);
    }
  }
    
}
