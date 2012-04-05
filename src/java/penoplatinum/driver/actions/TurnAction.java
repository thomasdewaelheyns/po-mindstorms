package penoplatinum.actions;

/**
 *
 * @author: Team Platinum
 */

import penoplatinum.model.Model;
import penoplatinum.navigator.Navigator;


public class TurnAction extends ActionSkeleton {

  public TurnAction(Model m, int angle) {
    super(m);
    setDistance(0);
    setAngle(angle);
  }
  
  private boolean first = true;

  @Override
  public int getNextAction() {
    if (first) {
      first = false;
      return Navigator.TURN;
    }
    return Navigator.NONE;
  }

  @Override
  public boolean isComplete() {
    return !getModel().getSensorPart().isMoving() && !first;
  }

  @Override
  public String getKind() {
    return "Turn";
  }

  @Override
  public String getArgument() {
    return getAngle() + "deg";
  }
}