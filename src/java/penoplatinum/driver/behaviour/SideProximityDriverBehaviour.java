package penoplatinum.driver.behaviour;

/**
 * SideProximityDriverBehaviour
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
import penoplatinum.driver.action.TurnDriverAction;
import penoplatinum.driver.action.CombinedDriverAction;


public class SideProximityDriverBehaviour implements DriverBehaviour {

  // configuration of minimal distance and corrections
  private final static int MIN_DISTANCE     =  18;
  private final static int CORRECTION_LEFT  = -15;
  private final static int CORRECTION_RIGHT =  15;

  // the correction we apply
  private DriverAction correctingAction = null;
  // the original Action we have intercepted
  private DriverAction originalAction;
  
  public boolean requiresAction(Model model, DriverAction currentAction) {
    // we only operate on a brand new MOVE action
    if( ! (currentAction instanceof MoveDriverAction) ) { return false; }
    
    WallsModelPart walls = WallsModelPart.from(model);

    // when there is a wall in front of us, don't correct anything, we're 
    // going to crash anyway ;-)
    if( walls.isWallFront() ) { return false; }

    // if the left or right wall are too close, schedule a corrective turn
    if( walls.getWallLeftDistance() < MIN_DISTANCE ) {
      this.correctingAction = new TurnDriverAction(model).set(CORRECTION_LEFT);
      return true;
    } else if( walls.getWallRightDistance() < MIN_DISTANCE ) {
      this.correctingAction = new TurnDriverAction(model).set(CORRECTION_RIGHT);
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
      					.thenPerform(this.originalAction);
  }
}
