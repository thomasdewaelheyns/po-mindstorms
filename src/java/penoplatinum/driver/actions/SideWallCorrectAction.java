package penoplatinum.actions;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 *
 * @author MHGameWork
 */

public class SideWallCorrectAction extends ActionSkeleton {

  private Model model;
  private final float correction;

  public SideWallCorrectAction(Model m, float correction) {
    super(m);
    model = m;
    this.correction = correction;
  }
  private boolean first = true;
  private boolean complete = false;
  private int phase = 0;

  @Override
  public int getNextAction() {
    Model m = model;
    if (first) {
      first = false;
      setAngle(90);
      setDistance(correction);
      return Navigator.STOP;

    }

    if (model.getSensorPart().isTurning() || model.getSensorPart().isMoving()) {
      return Navigator.NONE;
    }

    phase++;
    return getStartNavigatorAction();
  }

  private int getStartNavigatorAction() {
    switch (phase) {
      case 1:
        return Navigator.TURN;
      case 2:
        return Navigator.MOVE;
      case 3:
        setAngle(-getAngle());
        return Navigator.TURN;
    }
    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return complete || phase > 3;

  }

  @Override
  public String getKind() {
    return "SideWallCorrect";
  }

  @Override
  public String getArgument() {
    return (int) (getDistance() * 100) + "cm";
  }
}
