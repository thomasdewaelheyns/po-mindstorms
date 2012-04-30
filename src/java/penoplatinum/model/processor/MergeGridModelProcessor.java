package penoplatinum.model.processor;

/**
 *
 * This class checks whether grids can be merged by using new barcode 
 * 
 * information obtained by this robot
 * 
 * @author Team Platinum
 */

import java.util.List;

import penoplatinum.grid.AggregatedGrid;
import penoplatinum.grid.AggregatedSubGrid;
import penoplatinum.grid.AggregatedSubGrid;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.model.GridModelPart;
import penoplatinum.pacman.GhostAction;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.Model;
import penoplatinum.util.TransformationTRT;


public class MergeGridModelProcessor extends ModelProcessor {

  @Override
  protected void work() {
    // check if we moved forward
    if( ! model.getGridPart().hasRobotMoved() || 
         model.getGridPart().getLastMovement() != GhostAction.FORWARD) {
      return;
    }
    GridModelPart gridPart = model.getGridPart();

    // check if we detected a barcode
    if (model.getBarcodePart().getLastBarcode() == -1) {
      // remove potential incorrect barcode

      gridPart.getAgent().getSector().setTagCode(-1);
      gridPart.getAgent().getSector().setTagBearing(0);
      return;
    }


    // We drove over a barcode on this tile

    gridPart.getAgent().getSector().setTagCode(model.getBarcodePart().getLastBarcode());
    gridPart.getAgent().getSector().setTagBearing(gridPart.getAgent().getBearing());

    //Find this barcode in the othergrids and map!!

    List<AggregatedSubGrid> otherGrids = gridPart.getGrid().getUnmergedGrids();

    for (AggregatedSubGrid g : otherGrids) {
      String name = gridPart.getGrid().getGhostNameForGrid(g);
      for (Sector s : g.getStorageGrid().getTaggedSectors()) {
        gridPart.getGrid().attemptMapBarcode(gridPart.getAgent().getSector(), s, name);
        gridPart.getGrid().DEBUG_checkGridCorrectness(gridPart.getAgent());
      }
    }

    int left = gridPart.getAgent().getSector().getLeft(),
        top  = gridPart.getAgent().getSector().getTop();

    model.getMessagePart().getProtocol().sendBarcodeAt(left, top, model.getBarcodePart().getLastBarcode(), gridPart.getAgent().getBearing());

    this.model.getBarcodePart().clearLastBarcode(); // This might be better in a seperate modelprocessor, but anyways :D

  }
  // TODO: move this somewhere usefull
}
