/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model.processor;

import penoplatinum.barcode.BarcodeTranslator;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.model.GridModelPart;
import penoplatinum.pacman.GhostAction;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.Model;
import penoplatinum.util.TransformationTRT;

/**
 *
 * This class checks whether grids can be merged by using new barcode 
 *  information obtained by this robot
 * 
 * @author MHGameWork
 */
public class MergeGridModelProcessor extends ModelProcessor {

  @Override
  protected void work() {

    // check if we moved forward
    if (!model.getGridPart().hasRobotMoved() || model.getGridPart().getLastMovement() != GhostAction.FORWARD) {
      return;
    }

    // check if we detected a barcode
    if (model.getBarcodePart().getLastBarcode() == -1) {
      return;
    }

    GridModelPart gridPart = model.getGridPart();

    // We drove over a barcode on this tile

    gridPart.getAgent().getSector().setTagCode(model.getBarcodePart().getLastBarcode());
    gridPart.getAgent().getSector().setTagBearing(gridPart.getAgent().getBearing());

    //Find this barcode in the othergrids and map!!

    for (Grid g : gridPart.getOtherGrids().values()) {
      String name = gridPart.getOtherGrids().findKey(g);
      for (Sector s : g.getTaggedSectors()) {
        attemptMapBarcode(model, gridPart.getAgent().getSector(), s, g, name);
      }
    }


    //To fix protocol shitiness, send a position cmd for safety
    model.getMessagePart().getProtocol().sendBarcode(model.getBarcodePart().getLastBarcode(), gridPart.getAgent().getBearing());
    //TODO: is this needed?          lastBarcode = -1;


    this.model.getBarcodePart().clearLastBarcode(); // This might be better in a seperate modelprocessor, but anyways :D

  }

  // TODO: move this somewhere usefull
  public static boolean attemptMapBarcode(Model model, Sector ourSector, Sector otherSector, final Grid otherGrid, String otherAgentName) {
    int ourCode = ourSector.getTagCode();
    int code = otherSector.getTagCode();
    int bearing = otherSector.getTagBearing();
    int invertedCode = BarcodeTranslator.reverse(code, 6);

    if (invertedCode == code) {
      return false; // THis barcode is symmetrical??
    }
    if (ourCode == invertedCode) {
      code = invertedCode;

      // Switch bearing
      bearing = Bearing.reverse(bearing);
    }

    if (ourCode != code) {
      return false;
    }
    final int relativeBearing = (bearing - ourSector.getTagBearing() + 4) % 4;
    TransformationTRT transform = new TransformationTRT()
            .setTransformation(ourSector.getLeft(), ourSector.getTop(), 
                                relativeBearing, 
                                otherSector.getLeft(), otherSector.getTop());
    model.getGridPart().setOtherGhostInitialOrientation(otherAgentName, transform);
    model.getGridPart().getGrid().importGrid(otherGrid, transform);
    //model.getGrid().refresh();
    return true;
  }
}
