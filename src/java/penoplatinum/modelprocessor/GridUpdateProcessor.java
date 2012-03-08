package penoplatinum.modelprocessor;

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
import penoplatinum.pacman.GhostModel;

import penoplatinum.simulator.mini.Bearing;

public class GridUpdateProcessor extends ModelProcessor {

  public GridUpdateProcessor() {
    super();
  }

  public GridUpdateProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  // update the agent
  public void work() {
    GhostModel model = (GhostModel) this.model;
    if (!model.needsGridUpdate()) {
      return;
    }

    this.updateWallInfo();
    this.addNewSectors();
    for (int i = 0; i < 10; i++) {
      updateHillClimbingInfo();
    }

    model.markGridUpdated();
  }

  // update the current Sector on the Grid to reflect the currently selected
  private void updateWallInfo() {
    GhostModel model = (GhostModel) this.model;

    Sector detected = model.getDetectedSector();
    Sector current = model.getCurrentSector();
    for (int atLocation = Bearing.N; atLocation <= Bearing.W; atLocation++) {
      if (detected.isKnown(atLocation)) {
        if (detected.hasWall(atLocation)) {
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
    model.markSectorUpdated(current);
  }

  // if there are bearing without walls, providing access to unknown Sectors,
  // add such Sectors to the Grid
  private void addNewSectors() {
    Sector current = ((GhostModel) this.model).getCurrentSector();
    for (int location = Bearing.N; location <= Bearing.W; location++) {
      if (current.givesAccessTo(location)
              && !current.hasNeighbour(location)) {
        // TODO: parameterize the value
        //System.out.println(current.getAgent().getName() + " : adding unknown sector(" + location +")" );
        current.createNeighbour(location).setValue(5000);
      }
    }
  }

  private void updateHillClimbingInfo() {
    ((GhostModel) this.model).getGrid().refresh();
  }
}
