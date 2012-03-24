package penoplatinum.navigatoractions;

import penoplatinum.actions.BaseAction;
import penoplatinum.actions.StopAction;
import penoplatinum.simulator.Model;

/**
 * Tells the driver to wait a while to think in the next middle of a section.
 * @author Team Platinum
 */
public class WaitNavigatorAction implements NavigatorAction {

  @Override
  public BaseAction getBaseAction(Model m) {
    return new StopAction(500);
  }

  @Override
  public void whenDone(Model m) {
    //do nothing
    //throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public String toString(){
    return "Wait";
  }
}
