package penoplatinum.driverevents;

import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.TurnAction;
import penoplatinum.simulator.Model;

/**
 * This changes the orientation slightly when being to close to the wall.
 * @author Team Platinum
 */
public class ProximityCorrectionEvent extends BaseEvent {
  
  public static final ProximityCorrectionEvent singleton = new ProximityCorrectionEvent();

  public ProximityCorrectionEvent() {
  }

  public ProximityCorrectionEvent(DriverEvent next) {
    super(next);
  }

  @Override
  public void checkEvent(Model m, ActionQueue queue) {
    if (m.getWallsPart().isWallFront()) {
      return;
    }
    if (m.getWallsPart().getWallLeftDistance() < 18) {
      queue.add(new TurnAction(m, -15).setIsNonInterruptable(true));
    } else if (m.getWallsPart().getWallRightDistance() < 18) {
      queue.add(new TurnAction(m, 15).setIsNonInterruptable(true));
    }
  }
}
