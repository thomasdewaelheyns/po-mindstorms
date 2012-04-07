package penoplatinum.driver;

/**
 * GhostDriver
 *
 * An implementation for a Ghost-style Robot. It extends the ManhattanDriver
 * and adds some behaviours to react on changes in the environment.
 * 
 * @author Team Platinum
 */

import penoplatinum.driver.behaviour.*;


public class GhostDriver extends ManhattanDriver {

	private final static double SECTOR_SIZE = 0.04;

	public GhostDriver() {
		super(SECTOR_SIZE);
		this.setupBehaviours();
	}
	
	private void setupBehaviours() {
		this.addBehaviour(new ProximityDriverBehaviour());
	}

}

/*
  private void onQueueEmpty() {

    if (navigatorAction == null) {
      queue.add(new StopAction());
      return;
    }

    GhostModel model = (GhostModel) this.model;
    driverState++;

    switch (driverState) {
      case STARTING:
        Integer a = navigatorAction;

        if (model.getWallsPart().getWallFrontDistance() < 10) {
          queue.clearActionQueue();
          queue.add(new MoveAction(model, -0.1f));
          //queue.add(new PerformSweepAction(api, model));
          //queue.add(new GapDetectionRestoreAction(api, model));
          return;
        }

        if (a == GhostAction.NONE) {
          queue.add(new StopAction(100));
        } else if (a == GhostAction.TURN_LEFT) {
          queue.add(new TurnAction(model, 90).setIsNonInterruptable(true));
        } else if (a == GhostAction.TURN_RIGHT) {
          queue.add(new TurnAction(model, -90).setIsNonInterruptable(true));
        } else if (a == GhostAction.FORWARD) {
          queueProximityCorrectionAction();
          queue.add(new MoveAction(model, 0.4f));
        } else {
          throw new RuntimeException("Unknown GhostAction");
        }
        break;
      case COMPLETE:
        Integer prevAction = navigatorAction;

        if (prevAction == GhostAction.NONE) {
        } else if (prevAction == GhostAction.TURN_LEFT) {
          model.getGridPart().turnLeft();
        } else if (prevAction == GhostAction.TURN_RIGHT) {
          model.getGridPart().turnRight();
        } else if (prevAction == GhostAction.FORWARD) {
          model.getGridPart().moveForward();
        } else {
          throw new RuntimeException("Unknown GhostAction");
        }

        navigatorAction = null;

        queue.add(new StopAction());
        break;

      default:
        throw new RuntimeException("Invalid state!!");
    }
  }

  private void queueProximityCorrectionAction() {
    GhostModel m = (GhostModel) model;

    if (m.getWallsPart().getWallLeftDistance() < 18 && !m.getWallsPart().isWallFront()) {
      queue.add(new TurnAction(m, -15).setIsNonInterruptable(true));

    } else if (m.getWallsPart().getWallRightDistance() < 18 && !m.getWallsPart().isWallFront()) {
      queue.add(new TurnAction(m, 15).setIsNonInterruptable(true));

    }
  }

  private void checkBarcodeEvent(){
    if( ! model.getBarcodePart().isReadingBarcode()){ return; }
    // we're reading a barcode, 
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.05f).setIsNonInterruptable(false));
  }
  
  private void checkLineEvent() {
    if( model.getLightPart().getLine() == Line.NONE ) { return; }
    if( model.getBarcodePart().isReadingBarcode())    { return; }

    // we're crossing a line -> start avoiding it
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.02f));
    queue.add(new AlignPerpendicularLine(model, true).setIsNonInterruptable(true));
    queue.add(new MoveAction(model, 0.18f + 0.03f));
  }

*/
