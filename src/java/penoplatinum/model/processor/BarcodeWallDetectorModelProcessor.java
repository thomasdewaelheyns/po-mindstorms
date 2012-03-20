package penoplatinum.model.processor;

import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;
import penoplatinum.simulator.Bearing;

/**
 * Reponsible for setting the correct walls on a sector when a barcode has been
 * detected on that sector
 * 
 * @author
 */
public class BarcodeWallDetectorModelProcessor extends ModelProcessor {

  public BarcodeWallDetectorModelProcessor() {
    super();
  }

  public BarcodeWallDetectorModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  boolean doOnce = false;

  @Override
  public void work() {
    if (!model.getBarcodePart().isReadingBarcode()) {
      doOnce = false;
      return;
    }
    if (doOnce) {
      //don't update the modelSector twice when reading a barcode
      return;
    }
    doOnce = true;
    Agent agent = model.getGridPart().getAgent();
    int bearing = agent.getBearing();
    Sector sector = new Sector();

    // front is WALLFREE.
    sector.removeWall(bearing);
    // back is WALLFREE
    sector.removeWall(Bearing.reverse(bearing));
    // left has a wall
    sector.addWall(Bearing.leftFrom(bearing));
    // right has a wall
    sector.addWall(Bearing.rightFrom(bearing));

    model.getWallsPart().updateSector(sector);
    addNewSectors();
  }

  // if there are bearing without walls, providing access to unknown Sectors,
  // add such Sectors to the Grid
  private void addNewSectors() {
    Sector current = this.model.getGridPart().getCurrentSector();
    for (int location = Bearing.N; location <= Bearing.W; location++) {
      if (current.givesAccessTo(location)
              && !current.hasNeighbour(location)) {
        // TODO: parameterize the value
        //System.out.println(current.getAgent().getName() + " : adding unknown sector(" + location +")" );
        current.createNeighbour(location).setValue(5000);
      }
    }
  }
}
