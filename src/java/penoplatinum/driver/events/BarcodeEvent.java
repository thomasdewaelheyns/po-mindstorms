package penoplatinum.driverevents;

import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.MoveAction;
import penoplatinum.simulator.Model;

/**
 * This changes the driving style of the robot when a barcode is read.
 * @author Rupsbant
 */
public class BarcodeEvent extends BaseEvent {

  public BarcodeEvent() {
  }

  public BarcodeEvent(DriverEvent next) {
    super(next);
  }

  @Override
  public void checkEvent(Model model, ActionQueue queue) {
    if(!model.getBarcodePart().isReadingBarcode()){
      return;
    }
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.05f).setIsNonInterruptable(false));
  }
  
}
