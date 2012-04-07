package penoplatinum.driver.behaviour;

/**
 * FrontProximityDriverBehaviour
 * 
 * This behaviour checks the distance to both left and right walls and applies
 * a corrective turn to move away from them.
 * The behaviour only kicks in at the beginning of a forward move, adding
 * a turn and then re-injecting the original Move action.
 *
 * @author: Team Platinum
 */

import penoplatinum.model.Model;

import penoplatinum.model.part.WallsModelPart;

import penoplatinum.driver.action.DriverAction;
import penoplatinum.driver.action.MoveDriverAction;
import penoplatinum.driver.action.CombinedDriverAction;


public class FrontProximityDriverBehaviour implements DriverBehaviour {

  // configuration of minimal and correction distance
  private final static int    MIN_DISTANCE  =  10;
  private final static double CORRECTION    =  -0.05;

  // the correction we apply
  private DriverAction correctingAction = null;

  
  public boolean requiresAction(Model model, DriverAction currentAction) {
		// if we have already issued a correcting action and it is busy, no
		// further action is required
		if( this.correctingAction != null ) {
			if( this.correctingAction.isBusy() ) {
				return false;
			}
		}
    
    WallsModelPart walls = WallsModelPart.from(model);

    // when there is a wall in front of us, don't correct anything, we're 
    // going to crash anyway ;-)
    if( walls.getWallFrontDistance() < MIN_DISTANCE ) {
			this.correctingAction = new MoveDriverAction(model).set(CORRECTION);
			return true;
		}

    // no correction needed, proceed as planned
    return false;
  }

  // we return an aggregateAction, that first implements a turn with the 
  // correction angle and then the original action
	// NOTE: if the correction is zero, we throw an Exception because this is 
	//       only possible if the caller hasn't honoured a "false" reply on
	//       requiresAction.
  public DriverAction getNextAction() {
		if( this.correctingAction == null ) {
			throw new RuntimeException( "No correction means no next action!" );
		}
		return new CombinedDriverAction()
      					.firstPerform(this.correctingAction)
								.makeInterrupting();
  }
}
