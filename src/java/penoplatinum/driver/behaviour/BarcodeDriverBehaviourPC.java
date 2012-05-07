package penoplatinum.driver.behaviour;

/**
 * BarcodeDriverBehaviour
 * 
 * This behaviour checks whether we're reading a barcode and makes sure the
 * barcode is read in its entirety and that the robot doesn't stop halfway.
 *
 * @author Team Platinum
 */
import penoplatinum.model.Model;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.driver.action.DriverAction;
import penoplatinum.driver.action.CombinedDriverAction;
import penoplatinum.driver.action.MoveDriverAction;

public class BarcodeDriverBehaviourPC implements DriverBehaviour {

  // configuration of correction parameters
  private final static double CORRECTION = 0.17;
  // the correction we apply
  private DriverAction correctingAction = null;

  public boolean requiresAction(Model model, DriverAction currentAction) {

    BarcodeModelPart barcode = BarcodeModelPart.from(model);

    if (barcode.isReadingBarcode()) {
      this.correctingAction = new MoveDriverAction(model){

        @Override
        public boolean canBeInterrupted() {
          return false;
        }
        
      }.set(CORRECTION);
      return true;
    }

    return false;
  }

  // we return an aggregateAction, that implements a simple move forward.
  // NOTE: if the correction is null, we throw an Exception because this is 
  //       only possible if the caller hasn't honoured a "false" reply on
  //       requiresAction.
  public DriverAction getNextAction() {
    if (this.correctingAction == null) {
      //throw new RuntimeException("No correction means no next action!");
      throw new RuntimeException();
    }
    return new CombinedDriverAction().firstPerform(this.correctingAction);
  }
}
