package penoplatinum.navigatoractions;

import penoplatinum.actions.BaseAction;
import penoplatinum.simulator.Model;

public interface NavigatorAction {
  BaseAction getBaseAction(Model m);
  void whenDone(Model m);
}
