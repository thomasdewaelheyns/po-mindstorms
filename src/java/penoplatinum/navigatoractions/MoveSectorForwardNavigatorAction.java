package penoplatinum.navigatoractions;

import penoplatinum.actions.BaseAction;
import penoplatinum.actions.MoveAction;
import penoplatinum.simulator.Model;

public class MoveSectorForwardNavigatorAction implements NavigatorAction {

  @Override
  public BaseAction getBaseAction(Model m) {
    return new MoveAction(m, 0.4f);
  }

  @Override
  public void whenDone(Model m) {
    m.getGridPart().moveForward();
  }
}
