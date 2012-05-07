package penoplatinum.navigator.mode;

/**
 * ChaseHillClimbingNavigatorMode
 *
 * Implements a NavigatorMode that uses Hill Climbing to discover all Sectors
 * on a Grid.
 *
 * @author Team Platinum
 */

import penoplatinum.util.Bearing;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.Agent;
import penoplatinum.model.Model;

public class ChaseHillClimbingNavigatorMode extends HillClimbingNavigatorMode {

  public ChaseHillClimbingNavigatorMode(Model model) {
    super(model);
  }

  // we reached our goal if we're next to Pacman
  public boolean reachedGoal() {
    // TODO: clean this up ;-)
    Grid myGrid = this.grids.getMyGrid();
    Agent pacman = myGrid.getAgent("pacman"), // TODO: magic name constant
          me     = this.grids.getMyAgent();
    if(pacman == null){ //no pacman found
      //Utils.Log("Pacman not found");
      return false;
    }
    Sector pacmanSector = myGrid.getSectorAt(myGrid.getPositionOf(pacman)),
           mySector     = myGrid.getSectorAt(myGrid.getPositionOf(me));

    for(Bearing atLocation : Bearing.NESW) {
      if( pacmanSector == mySector.getNeighbour(atLocation) ) { return true; }
    }

    return false;
  }

  @Override
  public void activate() {
    this.grids.onlyApplyCollaborateDiffusionOnPacman();
  }
  
}
