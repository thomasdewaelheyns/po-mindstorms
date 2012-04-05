package penoplatinum.navigatoractions;

import penoplatinum.actions.ActionSkeleton;
import penoplatinum.simulator.Model;

public interface NavigatorAction {
  ActionSkeleton getActionSkeleton(Model m);
  void whenDone(Model m);
}
