package penoplatinum.navigatoractions;

import penoplatinum.actions.BaseAction;
import penoplatinum.actions.TurnAction;
import penoplatinum.simulator.Model;

/**
 * This action tells the driver to turn left in the next middle of the sector.
 * @author Team Platinum
 */
public class TurnLeftNavigatorAction implements NavigatorAction {

  @Override
  public BaseAction getBaseAction(Model m) {
    return new TurnAction(m, 90).setIsNonInterruptable(true);
  }

  @Override
  public void whenDone(Model m) {
    m.getGridPart().turnLeft();
  }
}
