package penoplatinum.navigatoractions;

import penoplatinum.actions.BaseAction;
import penoplatinum.actions.TurnAction;
import penoplatinum.simulator.Model;

/**
 * Tells the driver to turn right in the next middle of the section.
 * @author Team Platinum
 */
public class TurnRightNavigatorAction implements NavigatorAction {

  @Override
  public BaseAction getBaseAction(Model m) {
    return new TurnAction(m, -90).setIsNonInterruptable(true);
  }

  @Override
  public void whenDone(Model m) {
    m.getGridPart().turnRight();
  }
  
  public String toString(){
    return "Turn Right";
  }
}
