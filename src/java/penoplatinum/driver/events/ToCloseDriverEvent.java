package penoplatinum.driverevents;

import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.MoveAction;
import penoplatinum.simulator.Model;
import penoplatinum.util.Utils;

/**
 * This checks if the robot is to close to the wall.
 * @author Team Platinum
 */
public class ToCloseDriverEvent extends BaseEvent {

  public static final ToCloseDriverEvent singleton = new ToCloseDriverEvent();

  public ToCloseDriverEvent() {
  }

  @Override
  public void checkEvent(Model model, ActionQueue queue) {
    if (model.getWallsPart().getWallFrontDistance() < 10) {
      Utils.Log("TOOO CLOOOSE");
      queue.add(new MoveAction(model, -0.05f));
      return;
    }
  }
}
