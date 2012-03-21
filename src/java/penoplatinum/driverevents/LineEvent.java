package penoplatinum.driverevents;

import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.AlignPerpendicularLine;
import penoplatinum.actions.MoveAction;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;

/**
 * The changes the behaviour if a line is detected in the model.
 * @author Rupsbant
 */
public class LineEvent extends BaseEvent {

  public LineEvent() {
  }

  public LineEvent(DriverEvent next) {
    super(next);
  }

  @Override
  public void checkEvent(Model model, ActionQueue queue) {
    if (model.getLightPart().getLine() == Line.NONE) {
      return;
    }
    if (model.getBarcodePart().isReadingBarcode()) {
      return;
    }
    // Line detected
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.02f));
    queue.add(new AlignPerpendicularLine(model, true).setIsNonInterruptable(true));
    queue.add(new MoveAction(model, 0.18f + 0.03f));
  }
}
