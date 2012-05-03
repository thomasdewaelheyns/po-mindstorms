package penoplatinum.grid;

import java.util.ArrayList;
import java.util.List;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.TransformationTRT;

/**
 * AggregatedGrid
 * 
 * Implementation of a Grid, allowing to take into account multiple Grids and
 * answer as if they were all combined.
 * 
 * The AggregatedGrid has one Main grid, which is used to relay all write 
 * operations, no extra logic is added
 * 
 * - All write operations go to the Main grid
 * - All read operations are done by merging all subgrids
 * 
 * 
 * Any number of subgrids can be added to an AggregatedGrid. Each grid can be 
 * given a transformation relative to the AggregatedGrid's coordinate system
 * 
 * Note: write operations are not used in the current project
 * 
 * Option: cache the aggregated sectors in a hashmap with positions as keys
 * 
 * @author: Team Platinum
 */
public class AggregatedGrid implements Grid {

  private penoplatinum.util.SimpleHashMap<Grid, SubGrid> grids = new penoplatinum.util.SimpleHashMap<Grid, SubGrid>();
  private List<Grid> gridList = new ArrayList<Grid>();
  private final Grid mainGrid;

  public AggregatedGrid(Grid mainGrid) {
    this.mainGrid = mainGrid;
    SubGrid sub = new SubGrid(new TransformedGrid(mainGrid), TransformationTRT.Identity);
    grids.put(mainGrid, sub);
    gridList.add(sub.getGrid());

  }

  /**
   * Activates a grid given a transfromation on this AggregatedGrid
   * If given grid already was on this AggregatedGrid, the new transformation
   * is used
   */
  public AggregatedGrid activateSubGrid(Grid grid, TransformationTRT transform) {
    if (grid == mainGrid)
      throw new IllegalArgumentException();
    SubGrid sub = grids.get(grid);
    if (sub == null) {
      sub = new SubGrid(new TransformedGrid(grid), transform);
      gridList.add(sub.getGrid());
      grids.put(grid, sub);
    }

    sub.getGrid().setTransformation(transform);

    return this;
  }

  /**
   * Stops using given grid for aggregation
   */
  public AggregatedGrid deactivateSubGrid(Grid grid) {
    if (grid == mainGrid)
      throw new IllegalArgumentException();

    SubGrid subGrid = grids.get(grid);

    gridList.remove(subGrid.getGrid());
    grids.remove(grid);

    return this;
  }

  /**
   * For internal use by AggregatedSector
   */
  List<Grid> getActiveGrids() {
    return gridList;
  }

  @Override
  public Grid add(Sector s, penoplatinum.util.Point position) {
    mainGrid.add(s, position);
    return this;
  }

  @Override
  public Sector getSectorAt(penoplatinum.util.Point position) {
    return new AggregatedSector(this, position);
  }

  @Override
  public penoplatinum.util.Point getPositionOf(Sector sector) {
    if (!(sector instanceof AggregatedSector))
      throw new UnsupportedOperationException("Not supported yet.");

    return ((AggregatedSector) sector).getPosition();

  }

  @Override
  public Iterable<Sector> getSectors() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Grid add(Agent agent, penoplatinum.util.Point position, Bearing bearing) {
    mainGrid.add(agent, position, bearing);
    return this;
  }

  @Override
  public Grid moveTo(Agent agent, penoplatinum.util.Point position, Bearing bearing) {
    mainGrid.moveTo(agent, position, bearing);
    return this;
  }

  @Override
  public Sector getSectorOf(Agent agent) {
    Sector ret = mainGrid.getSectorOf(agent);
    if (ret != null)
      return getSectorAt(mainGrid.getPositionOf(ret));

    Point retPos = null;

    for (SubGrid g : grids.values()) {
      Sector iSector = g.getGrid().getSectorOf(agent);
      if (iSector == null)
        continue; // No information about agent for this subgrid
      Point iPosition = g.getGrid().getPositionOf(iSector);

      if (retPos == null) {
        // Store information
        retPos = iPosition;
        continue;
      }

      // Check for conflict

      if (!retPos.equals(iPosition))
        return null; // conflict!!

      // Subgrid information matches

    }
    if (retPos == null)
      return null;
    return getSectorAt(retPos);
  }

  @Override
  public Bearing getBearingOf(Agent agent) {
    // Dont merge bearings :D
    Bearing ret = mainGrid.getBearingOf(agent);
    if (ret != null)
      return ret;
    for (SubGrid g : grids.values()) {
      ret = g.getGrid().getBearingOf(agent);
      if (ret != null)
        return ret;
    }

    return null;
  }

  @Override
  public Agent getAgent(String name) {
    // Find it in any of the subgrids
    for (SubGrid g : grids.values()) {
      Agent a = g.getGrid().getAgent(name);
      if (a != null)
        return a;
    }
    return null;
  }

  @Override
  public Agent getAgentAt(penoplatinum.util.Point point) {
    // Just return any agent found
    for (SubGrid g : grids.values()) {
      Agent a = g.getGrid().getAgentAt(point);
      if (a != null)
        return a;
    }
    return null;
  }

  @Override
  public Iterable<Agent> getAgents() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int getMinLeft() {
    int ret = mainGrid.getMinLeft();
    for (SubGrid g : grids.values()) {
      int sub = g.getGrid().getMinLeft();
      if (sub < ret)
        ret = sub;
    }
    return ret;
  }

  @Override
  public int getMaxLeft() {
    int ret = mainGrid.getMaxLeft();
    for (SubGrid g : grids.values()) {
      int sub = g.getGrid().getMaxLeft();
      if (sub > ret)
        ret = sub;
    }
    return ret;
  }

  @Override
  public int getMinTop() {
    int ret = mainGrid.getMinTop();
    for (SubGrid g : grids.values()) {
      int sub = g.getGrid().getMinTop();
      if (sub < ret)
        ret = sub;
    }
    return ret;
  }

  @Override
  public int getMaxTop() {
    int ret = mainGrid.getMaxTop();
    for (SubGrid g : grids.values()) {
      int sub = g.getGrid().getMaxTop();
      if (sub > ret)
        ret = sub;
    }
    return ret;
  }

  @Override
  public int getWidth() {
    return getMaxLeft() - getMinLeft() + 1;
  }

  @Override
  public int getHeight() {
    return getMaxTop() - getMinTop();
  }

  @Override
  public int getSize() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean hasAgentOn(Sector sector, Class type) {
    if (!(sector instanceof AggregatedSector))
      throw new IllegalArgumentException();

    Point position = getPositionOf(sector);

    boolean ret = mainGrid.hasAgentOn(mainGrid.getSectorAt(position), type);
    if (ret)
      return ret;

    for (SubGrid g : grids.values()) {
      boolean iSector = g.getGrid().hasAgentOn(g.getGrid().getSectorAt(position), type);
      if (iSector)
        return true;
    }
    return false;

  }

  class SubGrid {

    private TransformedGrid grid;
    private TransformationTRT transformation;

    public SubGrid(TransformedGrid grid, TransformationTRT transformation) {
      this.grid = grid;
      this.transformation = transformation;
    }

    public TransformedGrid getGrid() {
      return grid;
    }

    public TransformationTRT getTransformation() {
      return transformation;
    }
  }
}
