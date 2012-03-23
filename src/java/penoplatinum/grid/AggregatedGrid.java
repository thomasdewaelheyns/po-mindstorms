package penoplatinum.grid;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import penoplatinum.SimpleHashMap;
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
   * @param transform 
   */
  public void setGhostRelativeTransformation(String name, TransformationTRT transform) {
    AggregatedSubGrid otherGrid = otherGrids.get(name);

    // Import the grid
    this.importGrid(otherGrid, transform);

    // Release memory!!!!
    otherGrid.getDecoratedGrid().disengage();

    // Relay to this grid from now on
    otherGrid.setDecoratedGrid(this);
    otherGrid.setTransformation(transform);
  }

  public List<Grid> getUnmergedGrids() {
    ArrayList<Grid> ret = new ArrayList<Grid>();
    for (int i = 0; i < otherGrids.values().size(); i++) {
      AggregatedSubGrid otherGrid = otherGrids.values().get(i);
      if (otherGrid.getDecoratedGrid() == this) {
        continue;
      }

      ret.add(otherGrid);
    }
    return ret;

  }

  public String getGhostNameForGrid(Grid name) {
    return otherGrids.findKey((AggregatedSubGrid) name);
  }

  public Grid getGhostGrid(String name) {
    AggregatedSubGrid grid = otherGrids.get(name);

    if (grid == null) {
      grid = new AggregatedSubGrid();
      grid.setDecoratedGrid(new SimpleGrid());
      grid.setTransformation(TransformationTRT.Identity);
      otherGrids.put(name, grid);
    }

    return grid;
  }
}