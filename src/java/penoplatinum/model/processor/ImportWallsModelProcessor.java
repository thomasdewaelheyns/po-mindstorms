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

public class AgentWallsUpdateProcessor extends ModelProcessor {

  public AgentWallsUpdateProcessor() {
    super();
  }

  public AgentWallsUpdateProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  // update the agent
  public void work() {
    if (!((GhostModel) this.model).getWallsPart().hasUpdatedSector()) {
      return;
    }
    this.updateWallInfo();

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

}
