package penoplatinum.driver;

import penoplatinum.driver.behaviour.BarcodeDriverBehaviour;
import penoplatinum.driver.behaviour.FrontProximityDriverBehaviour;
import penoplatinum.driver.behaviour.LineDriverBehaviour;
import penoplatinum.driver.behaviour.SideProximityDriverBehaviour;

/**
 * GhostDriver
 *
 * An implementation for a Ghost-style Robot. It extends the ManhattanDriver
 * and adds some behaviours to react on changes in the environment.
 * 
 * @author Team Platinum
 */



public class GhostDriver extends ManhattanDriver {

	private final static double SECTOR_SIZE = 0.04;

	public GhostDriver() {
		super(SECTOR_SIZE);
		this.setupBehaviours();
	}
	
	private void setupBehaviours() {
		this.addBehaviour(new FrontProximityDriverBehaviour())
                    .addBehaviour(new SideProximityDriverBehaviour())
		    .addBehaviour(new BarcodeDriverBehaviour()) //Barcodes pauzeren nu ;)
		    .addBehaviour(new LineDriverBehaviour());
		//  .addBehaviour(new RestoreDriverBehaviour()) // TODO
	}

}
