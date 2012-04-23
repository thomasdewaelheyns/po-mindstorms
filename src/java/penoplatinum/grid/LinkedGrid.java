package penoplatinum.grid;

/**
 * SimpleGrid
 * 
 * A basic implementation of a Grid.
 * 
 * @author: Team Platinum
 */
import java.util.List;
import java.util.ArrayList;

import penoplatinum.SimpleHashMap;
import penoplatinum.barcode.BarcodeTranslator;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.TransformationTRT;
import penoplatinum.util.CantorDiagonal;
import penoplatinum.util.SimpleHashMap;

/**
 * This class implements a grid using LinkedSectors. The grid can also contain
 * any number of agents, which are placed on sectors.
 * 
 * @author MHGameWork
 */
public class LinkedGrid implements Grid {

  // we keep track of the boundaries of our Grid
  int minLeft = 0, maxLeft = 0, minTop = 0, maxTop = 0;
  // mapping from coordinates to allocating Sector

  private SimpleHashMap<Integer, Sector> sectors = new SimpleHashMap<Integer, Sector>();
  // all agents in a row

  private List<Agent> agents = new ArrayList<Agent>();
  // visualization for the Grid, by default none, is used by Simulator

  private GridView view = NullGridView.getInstance();

  private GridProcessor processor;

  private boolean terminated = false;

  /**
   * Copies the sourcegrid to the target grid, merging sectors using the mergeSector function
   * 
   * TODO: fix a bug with the mintop and probably the other boundaries after importing
   * 
   * @param target
   * @param source
   * @param transformation 
   */
  public static void copyGridTo(Grid target, Grid source, TransformationTRT transformation) {
    // Import sectors
    for (int i = 0; i < source.getSectors().size(); i++) {
      Sector s = source.getSectors().get(i);

      Point p = transformation.transform(s.getLeft(), s.getTop());

      int x = p.getX();
      int y = p.getY();

      Sector thisSector = target.getOrCreateSector(x, y);

      mergeSector(thisSector, transformation.getRotation(), s);

    }
  }

  public Grid setProcessor(GridProcessor processor) {
    this.processor = processor;
    this.processor.useGrid(this);
    return this;
  }

  public Sector getSector(Sector s, Bearing b) {
  }

  private void resize(int left, int top) {
    if (left < this.minLeft) {
      this.minLeft = left;
    }
    if (left > this.maxLeft) {
      this.maxLeft = left;
    }
    if (top < this.minTop) {
      this.minTop = top;
    }
    if (top > this.maxTop) {
      this.maxTop = top;
    }
  }

  public int getMinLeft() {
    return this.minLeft;
  }

  public int getMaxLeft() {
    return this.maxLeft;
  }

  public int getMinTop() {
    return this.minTop;
  }

  public int getMaxTop() {
    return this.maxTop;
  }

  public int getWidth() {
    return this.maxLeft - this.minLeft + 1;
  }

  public int getHeight() {
    return this.maxTop - this.minTop + 1;
  }

  // returns the sector at given absolute/relative coordinates or null
  public Sector getSector(int left, int top) {
    return (Sector) this.sectors.get(CantorDiagonal.transform(left, top));
  }

  public List<Sector> getSectors() {
    List<Sector> sectors = new ArrayList<Sector>(this.sectors.values());
    return sectors;
  }

  private void connect(Sector sector, Sector other, Bearing location) {
    if (sector != null) {
      sector.addNeighbour(other, location);
    }
    if (other != null) {
      other.addNeighbour(sector, Bearing.reverse(location));
    }
  }

  // sets the view to display the Grid on
  public Grid displayOn(GridView view) {
    this.view = view;
    // CHANGED
    this.view.display(this, false);
    return this;
  }

  // refreshes the Grid
  // this triggers the GridProcessors and refreshes any attached view
  public Grid refresh() {
    if (this.processor != null) {
      this.processor.process();
    }
    this.view.refresh();
    return this;
  }

  @Override
  public String toString() {
    String ret = "";

    for (int top = this.getMinTop(); top <= this.getMaxTop(); top++) {
      for (int left = this.getMinLeft(); left <= this.getMaxLeft(); left++) {
        Sector sector = this.getSector(left, top);
        if (sector != null) {
          ret += "(" + left + "," + top + "): " + sector.toString();
          ret += "\n";
        } else {
        }
      }
    }
    return ret;
  }

  public Grid addAgent(Agent agent) {
    if (!this.agents.contains(agent)) {
      this.agents.add(agent);
      this.view.agentsNeedRefresh();
    }
    return this;
  }

  // return a list of all agents
  public List<Agent> getAgents() {
    return this.agents;
  }

  public Agent getAgent(String name) {
    for (Agent agent : this.agents) {
      if (agent.getName().equals(name)) {
        return agent;
      }
    }
    return null;
  }

  // remove all agents from the agent list
  public Grid clearAgents() {
    this.agents.clear();
    this.view.agentsNeedRefresh();
    return this;
  }

  public void importGrid(Grid g, TransformationTRT transformation) {
    copyGridTo(this, g, transformation);
  }

  public static void mergeSector(Sector thisSector, int rotation, Sector s) {
    for (int j = Bearing.N; j <= Bearing.W; j++) {
      int otherBearing = (j - rotation + 4) % 4; // TODO check direction

      Boolean newVal = s.hasWall(otherBearing);
      Boolean oldVal = thisSector.hasWall(j);
      if (newVal == oldVal) {
        continue; // No changes
      }
      if (newVal == null) {
        continue; // Remote has no information
      }

      if (oldVal == null) {
        // Use remote information (do nothing) (keep newval)
      } else {
        // Conflicting information, set to unknown
        newVal = null;
      }

      thisSector.setWall(j, newVal);
    }

    // Merge the tags and the agents
    if (s.getAgent() != null) {
      // Remove old agent if exists
      // EDIT: NONONONOOOO not good!
//      if (thisSector.hasAgent()) {
//        thisSector.getGrid().removeAgent(thisSector.getAgent());
//      }
      Agent copyAgent = thisSector.getGrid().getAgent(s.getAgent().getName());
      if (copyAgent == null) {
        // create a copy
        copyAgent = s.getAgent().copyAgent();
        thisSector.getGrid().addAgent(copyAgent);

      }

      thisSector.put(copyAgent, (s.getAgent().getBearing() + rotation) % 4);

    }
    if (s.getTagCode() != -1) {
      thisSector.setTagCode(s.getTagCode());
      thisSector.setTagBearing((s.getTagBearing() + rotation) % 4);
    }

  }

  public void disengage() {
    for (Sector s : sectors.values()) {
      s.disengage();
    }
    agents.clear();

    terminated = true;

  }

  @Override
  public Sector getOrCreateSector(int x, int y) {
    Sector sector = getSector(x, y);
    if (sector == null) {
      sector = new Sector().setCoordinates(x, y);
      addSector(sector);
    }

    return sector;
  }

  @Override
  public void removeAgent(Agent agent) {
    agents.remove(agent);
  }

  public boolean areSectorsEqual(Grid other) {

    if (getSectors().size() != other.getSectors().size()) {
      return false;
    }

    for (Sector s : getSectors()) {
      boolean match = false;
      for (Sector otherS : other.getSectors()) {
        if (s.getLeft() != otherS.getLeft() || s.getTop() != otherS.getTop()) {
          continue;
        }
        match = true;

        if (s.getWalls() != otherS.getWalls()) {
          return false;
        }

        boolean barcodeMismatch = true;


        if (s.getTagBearing() == otherS.getTagBearing() && s.getTagCode() == otherS.getTagCode()) {
          barcodeMismatch = false;
        }
        if (s.getTagBearing() == Bearing.reverse(otherS.getTagBearing()) && s.getTagCode() == BarcodeTranslator.reverse(otherS.getTagCode(), 6)) {
          barcodeMismatch = false;
        }
        if (barcodeMismatch) {
          return false;
        }
      }
      if (!match) {
        return false;
      }
    }

    return true;

  }

  @Override
  public int getSize() {
    return sectors.size();
  }

  public GridView getView() {
    return view;
  }

  public Agent getAgentAt(Sector s) {
    for (int i = 0; i < agents.size(); i++) {
      if (agents.get(i).getSector() == s) {
        return agents.get(i);
      }
    }
    return null;
  }

  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  //
  @Override
  public TransformationTRT getTransformation() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Grid setTransformation(TransformationTRT transform) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Adds a sector to the grid. If there is no path through sectors to the 
   * given position, extra sectors are added by adding sectors first 
   * on the x-axis, next on the y axis
   * 
   */
  @Override
  public Grid add(Sector sector, Point position) {
    placeNewSectorPathTo(position);
    sector.putOn(this);

    int left = position.getX();
    int top = position.getY();
    this.resize(left, top);

    // add the sector to the list of all sectors in this grid
    this.sectors.put(CantorDiagonal.transform(left, top), sector);
    // connect neighbours
    this.connect(sector, this.getSector(left, top - 1), Bearing.N);
    this.connect(sector, this.getSector(left + 1, top), Bearing.E);
    this.connect(sector, this.getSector(left, top + 1), Bearing.S);
    this.connect(sector, this.getSector(left - 1, top), Bearing.W);

    this.view.sectorsNeedRefresh();

    return this;
  }

  /**
   * Adds a x first path to given position, adding new sectors when needed
   */
  private void placeNewSectorPathTo(Point position) {
    final int x = position.getX();
    final int y = position.getY();

    if (x != 0) {
      placeNewSectorPathTo(new Point(x - (int) Math.signum(x), y));
      return;
    } else if (y != 0) {
      placeNewSectorPathTo(new Point(x, y - (int) Math.signum(y)));
      return;
    }
    // we are at origin, so a path exists!
  }

  @Override
  public Sector getSectorAt(Point position) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Point getPositionOf(Sector sector) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Grid add(Agent agent, Point position) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Sector getSectorOf(Agent agent) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Bearing getBearingOf(Agent agent) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Grid copyTo(Grid target) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
