package penoplatinum.grid;

import java.util.ArrayList;
import java.util.List;
import penoplatinum.SimpleHashMap;
import penoplatinum.barcode.BarcodeTranslator;
import penoplatinum.simulator.Bearing;
import penoplatinum.util.TransformationTRT;

/**
 * AggregatedGrid
 * 
 * Implementation of a Grid, allowing to take into account multiple Grids and
 * answer as if they were all combined.
 * 
 * @author: Team Platinum
 */
public class AggregatedGrid extends SimpleGrid {

  // This is a map of the other robots. It consists of a key representing the
  // name of the other robot/ghost and a value that contains a Grid.
  private SimpleHashMap<String, AggregatedSubGrid> otherGrids = new SimpleHashMap<String, AggregatedSubGrid>();

  /**
   * Gives the relative transform between ghost with given name and the main grid
   * @param name
   * @param transform The transformation that transforms local ghost coordinates to remote ghost coordinates
   */
  public void setGhostRelativeTransformation(String name, TransformationTRT transform) {
    AggregatedSubGrid otherGrid = otherGrids.get(name);

    if (otherGrid.getStorageGrid() != null) {
      // Using a buffergrid!

      // Import the grid
      SimpleGrid.copyGridTo(this, otherGrid.getStorageGrid(), transform);

      // Release memory!!!!
      otherGrid.getStorageGrid().disengage();

      // Relay to this grid from now on
      otherGrid.setStorageGrid(null);

    } else {
      // Just set the new transformation. 
      // TODO: if the transformation is not thesame, this means the grid data is corrupted!!!!!
    }


    otherGrid.setTransformation(transform);
  }

  public List<AggregatedSubGrid> getUnmergedGrids() {
    ArrayList<AggregatedSubGrid> ret = new ArrayList<AggregatedSubGrid>();
    for (int i = 0; i < otherGrids.values().size(); i++) {
      AggregatedSubGrid otherGrid = otherGrids.values().get(i);
      if (otherGrid.getStorageGrid() == null) {
        continue;
      }

      ret.add(otherGrid);
    }
    return ret;

  }

  public String getGhostNameForGrid(AggregatedSubGrid grid) {
    return otherGrids.findKey(grid);
  }

  public AggregatedSubGrid getGhostGrid(String name) {
    AggregatedSubGrid grid = otherGrids.get(name);

    if (grid == null) {
      grid = new AggregatedSubGrid(this);
      grid.setStorageGrid(new SimpleGrid());
      grid.setTransformation(TransformationTRT.Identity);
      otherGrids.put(name, grid);
    }

    return grid;
  }

  public boolean attemptMapBarcode(Sector ourSector, Sector otherSector, String otherAgentName) {
    AggregatedGrid thisGrid = this;
    final Grid otherGrid = thisGrid.getGhostGrid(otherAgentName).getStorageGrid();
    int ourCode = ourSector.getTagCode();
    int code = otherSector.getTagCode();
    int bearing = otherSector.getTagBearing();
    int invertedCode = BarcodeTranslator.reverse(code,6);

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
    final int relativeBearing = (ourSector.getTagBearing() - bearing + 4) % 4;

    TransformationTRT transform = new TransformationTRT().setTransformation(-otherSector.getLeft(), -otherSector.getTop(), relativeBearing, ourSector.getLeft(), ourSector.getTop());

    thisGrid.setGhostRelativeTransformation(otherAgentName, transform);

    return true;
  }
}