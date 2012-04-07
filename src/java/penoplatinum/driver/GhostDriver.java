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
		this.addBehaviour(new SideProximityDriverBehaviour());
		// .addBehaviour(new DriverBehaviour());
		// .addBehaviour(new DriverBehaviour());
		// .addBehaviour(new DriverBehaviour());
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
          return;
        }

        break;

      default:
        throw new RuntimeException("Invalid state!!");
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
