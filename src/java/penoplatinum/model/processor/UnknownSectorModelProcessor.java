package penoplatinum.model.processor;

/**
 * GridUpdateProcessor
 * 
 * This modelprocessor adds new sectors to the wall-less boundaries of the grid
 * 
 * @author: Team Platinum
 */
import penoplatinum.grid.Sector;
import penoplatinum.model.GhostModel;

import penoplatinum.model.GridModelPart;
import penoplatinum.simulator.Bearing;

public class NewSectorsUpdateProcessor extends ModelProcessor {

  public NewSectorsUpdateProcessor() {
    super();
  }

  public NewSectorsUpdateProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  public void work() {
    if (!((GhostModel) this.model).getGridPart().hasChangedSectors()) {
      return;
    }

    this.addNewSectors();

  }


  // if there are bearing without walls, providing access to unknown Sectors,
  // add such Sectors to the Grid
  private void addNewSectors() {
    GridModelPart grid = ((GhostModel) this.model).getGridPart();

    //TODO: do this for all new sectors
    
    Sector current = grid.getCurrentSector();
    for (int location = Bearing.N; location <= Bearing.W; location++) {
      if (current.givesAccessTo(location)
              && !current.hasNeighbour(location)) {
        Sector neighbour = current.createNeighbour(location);
        neighbour.setValue(5000);
        grid.markSectorChanged(neighbour);
      }
    }
  }
}
