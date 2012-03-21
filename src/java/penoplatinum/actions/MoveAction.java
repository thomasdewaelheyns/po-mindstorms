package penoplatinum.actions;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 * Moves a given distance in meter forward
 * @author: Team Platinum
 */
public class MoveAction extends BaseAction {

  public MoveAction(Model m, float distance) {
    super(m);
    setDistance(distance);
    setAngle(0);
  }
  private boolean first = true;

  @Override
  public int getNextAction() {
    if (first) {
      first = false;
      return Navigator.MOVE;
    }
    return Navigator.NONE;
  }

  @Override
  public boolean isComplete() {
    return !getModel().getSensorPart().isMoving() && !first;
  }

  @Override
  public String getKind() {
    return "Move";
  }

  @Override
  public String getArgument() {
    return (int) (getDistance() * 100) + "cm";
  }
}
