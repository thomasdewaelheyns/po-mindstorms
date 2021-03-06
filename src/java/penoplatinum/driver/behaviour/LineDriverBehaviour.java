package penoplatinum.driver.behaviour;

/**
 * LineDriverBehaviour
 * 
 * This behaviour checks whether we're crossing a line. If so, we use it to
 * align ourselves to it.
 *
 * @author Team Platinum
 */
import penoplatinum.driver.action.AlignDriverAction;
import penoplatinum.model.Model;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.driver.action.DriverAction;
import penoplatinum.driver.action.CombinedDriverAction;
import penoplatinum.driver.action.MoveDriverAction;

public class LineDriverBehaviour implements DriverBehaviour {

  // configuration of correction parameters
  private final static double MOVE_BEFORE_ALIGN = 0.02;
  private final static double MOVE_AFTER_ALIGN = 0.21;
  private DriverAction correctingAction;

  public boolean requiresAction(Model model, DriverAction currentAction) {
    // if we have already issued a correcting action and it is busy, no
    // further action is required
    if (this.correctingAction != null) {
      if (this.correctingAction.isBusy()) {
        return false;
      } else {
        this.correctingAction = null;
      }
    }
    LightModelPart light = LightModelPart.from(model);
    if (!light.isReadingLine()) {
      return false;
    }
    this.correctingAction = new CombinedDriverAction()
            .firstPerform(new MoveDriverAction(model).set(MOVE_BEFORE_ALIGN))
            .thenPerform(new AlignDriverAction(model))
            .thenPerform(new MoveDriverAction(model).set(MOVE_AFTER_ALIGN));
    return true;
  }

  // NOTE: if the action isn't reauired, we throw an Exception because this is 
  //       only possible if the caller hasn't honoured a "false" reply on
  //       requiresAction.
  public DriverAction getNextAction() {
    if (this.correctingAction == null) {
//      throw new RuntimeException("No correction means no next action!");
      throw new RuntimeException();
    }
    return this.correctingAction;
  }
}
