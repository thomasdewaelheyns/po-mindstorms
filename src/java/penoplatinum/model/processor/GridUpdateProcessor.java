package penoplatinum.model.processor;

/**
 * GridUpdateProcessor
 * 
 * ModelProcessor to update a Grid based on other information in the Model
 * TODO: has a hard dependency on GhostModel
 * 
 * @author: Team Platinum
 */
import penoplatinum.grid.Sector;
import penoplatinum.model.GhostModel;

import penoplatinum.model.GridModelPart;
import penoplatinum.model.WallsModelPart;
import penoplatinum.simulator.Bearing;

public class GridUpdateProcessor extends ModelProcessor {

  public GridUpdateProcessor() {
    super();
  }

  public GridUpdateProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  // update the agent
  public void work() {
    if (!((GhostModel) this.model).getWallsPart().needsGridUpdate()) {
      return;
    }

    this.updateWallInfo();
    this.addNewSectors();

  }

  // update the current Sector on the Grid to reflect the currently selected
  private void updateWallInfo() {
    GhostModel model = (GhostModel) this.model;

    GridModelPart grid = model.getGridPart();
    WallsModelPart walls = model.getWallsPart();
    
    Sector detected = walls.getDetectedSector();
    Sector current = grid.getCurrentSector();
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
    grid.markSectorChanged(current);
  }

  // if there are bearing without walls, providing access to unknown Sectors,
  // add such Sectors to the Grid
  private void addNewSectors() {
    GridModelPart grid = ((GhostModel) this.model).getGridPart();

    Sector current = grid.getCurrentSector();
    for (int location = Bearing.N; location <= Bearing.W; location++) {
      if (current.givesAccessTo(location)
              && !current.hasNeighbour(location)) {
        Sector neighbour = current.createNeighbour(location);
        // TODO: parameterize the value
        //System.out.println(current.getAgent().getName() + " : adding unknown sector(" + location +")" );
        neighbour.setValue(5000);
        grid.markSectorChanged(neighbour);
      }
    }
  }
}
