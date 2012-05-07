/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import java.util.List;
import penoplatinum.grid.agent.BarcodeAgent;
import penoplatinum.grid.agent.Agent;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.SimpleHashMap;
import penoplatinum.util.TransformationTRT;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This grid only supports read operations!! The grid is the merged result
 * of all the ghosts information
 * 
 * The name of the Main ghost is provided at construction. Information provided
 * by this ghost is given priority
 * 
 * @author MHGameWork
 */
public class MultiGhostGrid implements Grid, GridObserver {

  private SimpleHashMap<String, Ghost> ghosts = new SimpleHashMap<String, Ghost>();
  private final String mainGhostName;
  private AggregatedGrid grid;

  public MultiGhostGrid(String mainGhostName) {
    this.mainGhostName = mainGhostName;


  }

  @Override
  public void agentChanged(Grid g, Agent a) {
    if (!(a instanceof BarcodeAgent))
      return;
    attemptMapBarcodeOnGrid(a, g);


  }

  public void attemptMapBarcodeOnGrid(Agent a, Grid g) {
    BarcodeAgent barcode = (BarcodeAgent) a;


    if (g == getGhostGrid(mainGhostName)) {
      for (Ghost ghost : ghosts.values()) {
        Grid iGrid = ghost.getGrid();
        if (iGrid == g)
          continue;
     

        TransformationTRT trans = mapBarcode(g, iGrid, barcode);
        if (trans == null)
          continue;
        mapTransitive(iGrid, trans);
        mapGhost(ghost, trans);

      }
    } else {
      TransformationTRT trans = findMapping(getGhostGrid(mainGhostName), g);
      if (trans == null)
        return;

      mapTransitive(g, trans);
      mapGhost(findGhostWithGrid(g), trans);


    }
  }

  public void mapTransitive(Grid iGrid, TransformationTRT trans) {

    // Map transitive

    TransformedGrid transformed = new TransformedGrid(iGrid);
    transformed.setTransformation(trans);

    for (Ghost transtiveGhost : ghosts.values()) {
      Grid transitiveGrid = transtiveGhost.getGrid();
      if (transitiveGrid == getGhostGrid(mainGhostName) || transitiveGrid == iGrid)
        continue;

      TransformationTRT transitiveTRT = findMapping(transformed, transitiveGrid);

      if (transitiveTRT == null)
        continue;
      mapGhost(findGhostWithGrid(transitiveGrid), transitiveTRT);
    }
  }

  /**
   * Creates a transformation to transform coordinates from grid 2 to grid 1
   */
  private TransformationTRT mapBarcode(Grid g1, Grid g2, BarcodeAgent barcode) {
    Bearing b1 = g1.getBearingOf(barcode);
    Bearing b2 = g2.getBearingOf(barcode);
    Point pos1 = g1.getPositionOf(barcode);
    Point pos2 = g2.getPositionOf(barcode);

    if (pos1 == null || pos2 == null)
      return null;

    TransformationTRT transform = new TransformationTRT().setTransformation(-pos2.getX(), -pos2.getY(), b1.to(b2).invert(), pos1.getX(), pos1.getY());
    return transform;
  }

  /**
   * Maps from g2 to g1
   */
  private TransformationTRT findMapping(Grid g1, Grid g2) {

    Grid g = g1;

    for (Agent barcode : g1.getAgents()) {
      if (!(barcode instanceof BarcodeAgent))
        continue;

      Grid iGrid = g2;

      if (iGrid.getPositionOf(barcode) == null)
        continue; // Barcode not found on grid

      TransformationTRT transform = mapBarcode(g, iGrid, (BarcodeAgent) barcode);

      return transform;

    }
    return null;
  }

  public MultiGhostGrid useAggregatedGrid(AggregatedGrid aggGrid) {
    if (grid != null)
      throw new IllegalArgumentException();
    grid = aggGrid;
    aggGrid.useMainGrid(getGhostGrid(mainGhostName));

    return this;
  }

  public Grid getGhostGrid(String name) {
    Ghost g = ghosts.get(name);
    if (g == null)
      g = createNewGhost(name);

    return g.getGrid();

  }

  private Ghost createNewGhost(String name) {
    ObservableGrid grid = new ObservableGrid(new LinkedGrid());
    grid.useObserver(this);
    Ghost g = new Ghost(grid);
    ghosts.put(name, g);

    return g;
  }

  public List<Sector> getChangedSectors(String ghostname) {
    //TODO
    throw new NotImplementedException();
  }

  @Override
  public Grid add(Sector s, Point position) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Sector getSectorAt(Point position) {
    return grid.getSectorAt(position);
  }

  @Override
  public Point getPositionOf(Sector sector) {
    return grid.getPositionOf(sector);
  }

  @Override
  public Iterable<Sector> getSectors() {
    return grid.getSectors();
  }

  @Override
  public Grid add(Agent agent, Point position, Bearing bearing) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Grid moveTo(Agent agent, Point position, Bearing bearing) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Bearing getBearingOf(Agent agent) {
    return grid.getBearingOf(agent);
  }

  @Override
  public Agent getAgent(String name) {
    return grid.getAgent(name);
  }

  @Override
  public Iterable<Agent> getAgents() {
    return grid.getAgents();
  }

  @Override
  public int getMinLeft() {
    return grid.getMinLeft();
  }

  @Override
  public int getMaxLeft() {
    return grid.getMaxLeft();
  }

  @Override
  public int getMinTop() {
    return grid.getMinTop();
  }

  @Override
  public int getMaxTop() {
    return grid.getMaxTop();
  }

  @Override
  public int getWidth() {
    return grid.getWidth();
  }

  @Override
  public int getHeight() {
    return grid.getHeight();
  }

  @Override
  public int getSize() {
    return grid.getSize();
  }

  private Ghost findGhostWithGrid(Grid g) {
    for (Ghost ghost : ghosts.values())
      if (ghost.getGrid() == g)
        return ghost;
    return null;
  }

  /**
   * This function handles what should happen when a mapping is found from the
   * local ghost to a remote ghost
   */
  private void mapGhost(Ghost g, TransformationTRT transform) {
    if (g == getGhostGrid(mainGhostName))
      throw new IllegalArgumentException();

    grid.activateSubGrid(g.getGrid(), transform);

  }

  @Override
  public Point getPositionOf(Agent agent) {
    return grid.getPositionOf(agent);
  }

  @Override
  public Agent getAgentAt(Point position, Class cls) {
    return grid.getAgentAt(position, cls);
  }

  @Override
  public int getSectorId(Point position) {
    return grid.getSectorId(position);
  }

  @Override
  public Sector getSector(int id) {
    return grid.getSector(id);
  }

  @Override
  public boolean hasNeighbour(int sectorId, Bearing atBearing) {
    return grid.hasNeighbour(sectorId, atBearing);
  }

  @Override
  public int getNeighbourId(int sectorId, Bearing atBearing) {
    return grid.getNeighbourId(sectorId, atBearing);
  }

  @Override
  public Grid setValue(int sectorId, int value) {
    return grid.setValue(sectorId, value);
  }

  @Override
  public int getValue(int sectorId) {
    return grid.getValue(sectorId);
  }

  @Override
  public Grid setWall(int sectorId, Bearing atBearing) {
    grid.setWall(sectorId, atBearing);
    return this;
  }

  @Override
  public Grid setNoWall(int sectorId, Bearing atBearing) {
    grid.setNoWall(sectorId, atBearing);
    return this;
  }

  @Override
  public Grid clearWall(int sectorId, Bearing atBearing) {
    grid.clearWall(sectorId, atBearing);
    return this;
  }

  @Override
  public boolean hasWall(int sectorId, Bearing atBearing) {
    return grid.hasWall(sectorId, atBearing);
  }

  @Override
  public boolean hasNoWall(int sectorId, Bearing atBearing) {
    return grid.hasNoWall(sectorId, atBearing);
  }

  @Override
  public boolean knowsWall(int sectorId, Bearing atBearing) {
    return grid.knowsWall(sectorId, atBearing);
  }

  @Override
  public boolean isFullyKnown(int sectorId) {
    return grid.isFullyKnown(sectorId);
  }

  @Override
  public Grid clearWalls(int sectorId) {
    return grid.clearWalls(sectorId);
  }

  @Override
  public boolean givesAccessTo(int sectorId, Bearing atBearing) {
    return grid.givesAccessTo(sectorId, atBearing);


  }

  class Ghost {

    private Grid grid;
    private TransformationTRT transform;

    public Ghost(Grid grid) {
      this.grid = grid;
    }

    public Grid getGrid() {
      return grid;
    }
  }

  @Override
  public String toString() {
    return grid.toString();
  }
}
