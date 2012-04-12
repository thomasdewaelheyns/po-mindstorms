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
		this.addBehaviour(new FrontProximityDriverBehaviour())
				.addBehaviour(new SideProximityDriverBehaviour())
		    .addBehaviour(new BarcodeDriverBehaviour())
		    .addBehaviour(new LineDriverBehaviour());
		//  .addBehaviour(new RestoreDriverBehaviour()) // TODO
	}

}
