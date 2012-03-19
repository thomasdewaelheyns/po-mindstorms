package penoplatinum.simulator.mini;

/**
 * GridUpdateProcessor
 * 
 * ModelProcessor to update a Grid based on other information in the Model
 * TODO: has a hard dependency on GhostModel
 * 
 * @author: Team Platinum
 */

import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;

import penoplatinum.model.processor.ModelProcessor;

import penoplatinum.simulator.Bearing;


public class MiniGridUpdateProcessor extends ModelProcessor {

  public MiniGridUpdateProcessor() { super(); }
  public MiniGridUpdateProcessor( ModelProcessor nextProcessor ) {
    super(nextProcessor);
  }

  // update the agent
  protected void work() {
    this.updateWallInfo();
    this.addNewSectors();
    this.updateHillClimbingInfo();
  }

  // update the current Sector on the Grid to reflect the currently selected
  private void updateWallInfo() {
    MiniGhostModel model = (MiniGhostModel)this.model;

    Sector detected = model.getDetectedSector();
    Sector current  = model.getCurrentSector();
    for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
      if( detected.isKnown(atLocation) ) {
        if( detected.hasWall(atLocation) ) {
          //if( current.isKnown(atLocation) && !current.hasWall(atLocation) ) {
          //  current.clearWall(atLocation);
          //} else {
            current.addWall(atLocation);
          //}
        } else {
          //if( current.isKnown(atLocation) && current.hasWall(atLocation) ) {
          //  current.clearWall(atLocation);
          //} else {
            current.removeWall(atLocation);
          //}
        }
      }
    }
  }

  // if there are bearing without walls, providing access to unknown Sectors,
  // add such Sectors to the Grid
  private void addNewSectors() {
    Sector current = ((MiniGhostModel)this.model).getCurrentSector();
    for(int location=Bearing.N; location<=Bearing.W; location++) {
      if( current.givesAccessTo(location) &&
          ! current.hasNeighbour(location) )
      {
        // TODO: parameterize the value
        //System.out.println(current.getAgent().getName() + " : adding unknown sector(" + location +")" );
        current.createNeighbour(location).setValue(5000);
      }
    }
  }
  
  private void updateHillClimbingInfo() {
    ((MiniGhostModel)this.model).getGrid().refresh();
  }
}
