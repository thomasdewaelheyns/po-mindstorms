package penoplatinum.model.processor;

/**
 * GridUpdateProcessor
 * 
 * This modelprocessor adds new sectors to the wall-less boundaries of the grid
 * 
 * @author: Team Platinum
 */
import penoplatinum.grid.LinkedSector;
import penoplatinum.grid.Sector;

import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;

public class UnknownSectorModelProcessor extends ModelProcessor {

  public UnknownSectorModelProcessor() {
    super();
  }

  public UnknownSectorModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  public void work() {
    Model model = getModel();
    GridModelPart gridPart = GridModelPart.from(model);
    if (!gridPart.hasChangedSectors()) {
      return;
    }
    this.addNewSectors();
  }

  // if there are bearing without walls, providing access to unknown Sectors,
  // add such Sectors to the Grid
  private void addNewSectors() {
    Model model = getModel();
    GridModelPart gridPart = GridModelPart.from(model);
    Sector current = gridPart.getMyGrid().getSectorAt(gridPart.getMyGrid().getPositionOf(gridPart.getMyAgent()));
    for (Bearing b : Bearing.NESW) {
      addNewSector(current, b);
    }
  }

  private void addNewSector(Sector current, Bearing direction) {
    if (current.givesAccessTo(direction) && !current.hasNeighbour(direction)) {
      Sector neighbour = new LinkedSector();
      neighbour.setValue(5000);
      current.addNeighbour(neighbour, direction);
    }
  }
}
