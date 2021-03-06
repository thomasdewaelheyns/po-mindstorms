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
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.WallsModelPart;
import penoplatinum.util.Bearing;

public class ImportWallsModelProcessor extends ModelProcessor {

  public ImportWallsModelProcessor() {
  }

  public ImportWallsModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  private int lastId = 0;

  public void work() {
    Model model = getModel();
    WallsModelPart wallPart = WallsModelPart.from(model);
    if (wallPart.getSectorId() <= lastId) {
      return;
    }
    lastId = wallPart.getSectorId();
    this.updateWallInfo();
  }

  // update the current Sector on the Grid to reflect the currently selected
  private void updateWallInfo() {
    Model model = getModel();

    GridModelPart g = GridModelPart.from(model);
    WallsModelPart walls = WallsModelPart.from(model);

    Sector detected = walls.getCurrentSector();
    Sector current = g.getMySector();

    for (Bearing b : Bearing.NESW) {
      copyWallSector(detected, current, b);
      g.markSectorChanged(current.getNeighbour(b));      
    }
    g.markSectorChanged(current);
  }

  private void copyWallSector(Sector from, Sector to, Bearing atLocation) {
    if (from.knowsWall(atLocation)) {
      if (from.hasWall(atLocation)) {
        to.setWall(atLocation);
      } else {
        to.setNoWall(atLocation);
      }
    } else {
      to.clearWall(atLocation);
    }
  }
}
