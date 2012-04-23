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

import javax.swing.text.Position;
import penoplatinum.barcode.BarcodeTranslator;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.TransformationTRT;
import penoplatinum.util.CantorDiagonal;
import penoplatinum.util.SimpleHashMap;
import penoplatinum.util.Transformation;

/**
 * This class implements a grid using LinkedSectors. The grid can also contain
 * any number of agents, which are placed on sectors.
 * 
 * @author MHGameWork
 */
public class LinkedGrid implements Grid {

  // visualization for the Grid, by default none, is used by Simulator
  private GridView view = NullGridView.getInstance();

  private GridProcessor processor;

  public Grid setProcessor(GridProcessor processor) {
    this.processor = processor;
    this.processor.useGrid(this);
    return this;
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

  @Override
  public int getMinLeft() {
    return this.minLeft;
  }

  @Override
  public int getMaxLeft() {
    return this.maxLeft;
  }

  @Override
  public int getMinTop() {
    return this.minTop;
  }

  @Override
  public int getMaxTop() {
    return this.maxTop;
  }

  @Override
  public int getWidth() {
    return this.maxLeft - this.minLeft + 1;
  }

  @Override
  public int getHeight() {
    return this.maxTop - this.minTop + 1;
  }

  // returns the sector at given absolute/relative coordinates or null
  public Sector getSector(int left, int top) {
    return (Sector) this.sectors.get(CantorDiagonal.transform(left, top));
  }

  @Override
  public List<Sector> getSectors() {
    List<Sector> sectors = new ArrayList<Sector>(this.sectors.values());
    return sectors;
  }

  private void connect(Sector sector, Sector other, Bearing location) {
    if (sector != null) {
      sector.addNeighbour(other, location);
    }
    if (other != null) {
      other.addNeighbour(sector, location.reverse());
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

  // return a list of all agents
  @Override
  public Iterable<Agent> getAgents() {
    return this.agents.values();
  }

//  public static void mergeSector(Sector thisSector, int rotation, Sector s) {
//    for (int j = Bearing.N; j <= Bearing.W; j++) {
//      int otherBearing = (j - rotation + 4) % 4; // TODO check direction
//
//      Boolean newVal = s.hasWall(otherBearing);
//      Boolean oldVal = thisSector.hasWall(j);
//      if (newVal == oldVal) {
//        continue; // No changes
//      }
//      if (newVal == null) {
//        continue; // Remote has no information
//      }
//
//      if (oldVal == null) {
//        // Use remote information (do nothing) (keep newval)
//      } else {
//        // Conflicting information, set to unknown
//        newVal = null;
//      }
//
//      thisSector.setWall(j, newVal);
//    }
//
//    // Merge the tags and the agents
//    if (s.getAgent() != null) {
//      // Remove old agent if exists
//      // EDIT: NONONONOOOO not good!
////      if (thisSector.hasAgent()) {
////        thisSector.getGrid().removeAgent(thisSector.getAgent());
////      }
//      Agent copyAgent = thisSector.getGrid().getAgent(s.getAgent().getName());
//      if (copyAgent == null) {
//        // create a copy
//        copyAgent = s.getAgent().copyAgent();
//        thisSector.getGrid().addAgent(copyAgent);
//
//      }
//
//      thisSector.put(copyAgent, (s.getAgent().getBearing() + rotation) % 4);
//
//    }
//    if (s.getTagCode() != -1) {
//      thisSector.setTagCode(s.getTagCode());
//      thisSector.setTagBearing((s.getTagBearing() + rotation) % 4);
//    }
//
//  }
//  public void disengage() {
//    for (Sector s : sectors.values()) {
//      s.disengage();
//    }
//    agents.clear();
//
//    terminated = true;
//
//  }
//
//  public boolean areSectorsEqual(Grid other) {
//
//    if (getSectors().size() != other.getSize()) {
//      return false;
//    }
//
//    for (Sector s : getSectors()) {
//      boolean match = false;
//      for (Sector otherS : other.getSectors()) {
//
//
//
//        if (s.getLeft() != otherS.getLeft() || s.getTop() != otherS.getTop()) {
//          continue;
//        }
//        match = true;
//
//        if (s.getWalls() != otherS.getWalls()) {
//          return false;
//        }
//
//        boolean barcodeMismatch = true;
//
//
//        if (s.getTagBearing() == otherS.getTagBearing() && s.getTagCode() == otherS.getTagCode()) {
//          barcodeMismatch = false;
//        }
//        if (s.getTagBearing() == Bearing.reverse(otherS.getTagBearing()) && s.getTagCode() == BarcodeTranslator.reverse(otherS.getTagCode(), 6)) {
//          barcodeMismatch = false;
//        }
//        if (barcodeMismatch) {
//          return false;
//        }
//      }
//      if (!match) {
//        return false;
//      }
//    }
//
//    return true;
//
//  }
  @Override
  public int getSize() {
    return sectors.size();
  }

  public GridView getView() {
    return view;
  }

  public Agent getAgentAt(Sector s) {
    Integer i = sectors.findKey(s);
    if (i == null)
      return null;
    return agents.get(i);
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
  // we keep track of the boundaries of our Grid
  int minLeft = 0, maxLeft = 0, minTop = 0, maxTop = 0;
  // mapping from coordinates to allocating Sector

  private SimpleHashMap<Integer, Sector> sectors = new SimpleHashMap<Integer, Sector>();

  private SimpleHashMap<Integer, Agent> agents = new SimpleHashMap<Integer, Agent>();

  private SimpleHashMap<Agent, Bearing> agentBearings = new SimpleHashMap<Agent, Bearing>();

  private Transformation transformation;

  @Override
  public Transformation getTransformation() {
    return transformation;
  }

  @Override
  public Grid setTransformation(Transformation transform) {
    transformation = transform;
    return this;
  }

  /**
   * Adds a sector to the grid. If there is no path through sectors to the 
   * given position, extra sectors are added by adding sectors first 
   * on the x-axis, next on the y axis
   * 
   */
  @Override
  public Grid add(Sector sector, Point position) {
    placeNewSectorPathTo(new Point(position));
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
   * Adds a x first path to given position, adding new sectors when needed. 
   * a sector is NOT added at position (x,y)
   */
  private void placeNewSectorPathTo(Point pos) {
    int x = 0;
    int y = 0;
    if (x != 0) {
      x -= (int) Math.signum(x);
    } else if (y != 0) {
      y -= (int) Math.signum(y);
    } else {
      // we are at origin, so a path exists!
      return;
    }

    pos.translate(x, y);
    if (getSectorAt(pos) == null) {
      Sector s = new LinkedSector();
      add(s, pos);
    }

    placeNewSectorPathTo(pos);

    pos.translate(-x, -y); // restore point

  }

  @Override
  public Sector getSectorAt(Point position) {
    return sectors.get(CantorDiagonal.transform(position));
  }

  @Override
  public Point getPositionOf(Sector sector) {
    Integer i = sectors.findKey(sector);
    if (i == null)
      return null;
    return CantorDiagonal.transform(i);
  }

  @Override
  public Grid add(Agent agent, Point position, Bearing bearing) {
    int index = CantorDiagonal.transform(position);
    agents.put(index, agent);
    agentBearings.put(agent, bearing);
    //this.view.agentsNeedRefresh();
    return this;
  }

  @Override
  public Agent getAgent(String name) {
    for (Agent agent : getAgents()) {
      if (agent.getName().equals(name)) {
        return agent;
      }
    }
    return null;
  }

  @Override
  public Sector getSectorOf(Agent agent) {
    Integer index = agents.findKey(agent);
    if (index == null)
      return null;
    return sectors.get(index);
  }

  @Override
  public Bearing getBearingOf(Agent agent) {
    return agentBearings.get(agent);
  }

  @Override
  public Grid moveTo(Agent agent, Point position, Bearing bearing) {
    Integer key = agents.findKey(agent);
    if (key == null)
      throw new IllegalArgumentException();

    agents.remove(key);
    agentBearings.remove(agent);

    add(agent, position, bearing);
    
    return this;

  }
}
