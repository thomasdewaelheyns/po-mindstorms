package penoplatinum.model.processor;

/**
 * Reponsible for setting the correct walls on a sector when a barcode has
 * been detected on that sector.
 * 
 * A barcode can only be found on a sector with 2 walls. So no sweep is needed
 * on such a sector.
 * 
 * @author Team Platinum
 */

import penoplatinum.model.Model;

import penoplatinum.grid.LinkedSector;

import penoplatinum.util.Bearing;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.WallsModelPart;


public class BarcodeWallsModelProcessor extends ModelProcessor {
  // decorator boilerplate constructors
  public BarcodeWallsModelProcessor() { super(); }
  public BarcodeWallsModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  private BarcodeModelPart barcode;
  private GridModelPart    grid;
  private WallsModelPart   walls;
  
  private boolean processedCurrentBarcode = false;

  // override the setModel to setup a reference to the BarcodeModelPart
  public void setModel(Model model) {
    super.setModel(model);
    this.barcode = BarcodeModelPart.from(this.getModel());
    this.grid    = GridModelPart.from(this.getModel());
    this.walls   = WallsModelPart.from(this.getModel());
  }

  public void work() {
    if( ! this.needToAddWalls() ) { return; }

    Bearing bearing = this.grid.getMyBearing();
    
    LinkedSector sector = new LinkedSector();
    sector.setNoWall(bearing);             // no wall in front
    sector.setNoWall(bearing.reverse());   // no wall at back
    sector.setWall(bearing.leftFrom());    // wall left
    sector.setWall(bearing.rightFrom());   // wall right

    this.walls.updateSector(sector);

    this.processedCurrentBarcode = true;
  }
  
  private boolean needToAddWalls() {
    if( ! this.barcode.isReadingBarcode() ) {
      this.processedCurrentBarcode = false;
      return false;
    }
    return ! this.processedCurrentBarcode;
  }
}
