package penoplatinum.grid;

/**
 * LinkedGrid
 * 
 * This class implements a grid using LinkedSectors. The grid can also contain
 * any number of agents, which are placed on sectors.
 * 
 * Note that this class explicitly uses LinkedSector and will not work with 
 * any other type of sector!!
 * 
 * @author: Team Platinum
 */
 
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.CantorDiagonal;
import penoplatinum.util.Position;
import penoplatinum.util.SimpleHashMap;


public class LinkedGrid implements Grid {

  // we keep track of the boundaries of our Grid
  int minLeft = 0, maxLeft = 0, minTop = 0, maxTop = 0;
  // mapping from coordinates to allocating Sector
  private SimpleHashMap<Integer, Sector> sectors = new SimpleHashMap<Integer, Sector>();
  private SimpleHashMap<Agent, Point> agentPositions = new SimpleHashMap<Agent, Point>();
  private SimpleHashMap<Agent, Bearing> agentBearings = new SimpleHashMap<Agent, Bearing>();

  /**
   * Adds a sector to the grid. If there is no path through sectors to the 
   * given position, extra sectors are added by adding sectors first 
   * on the x-axis, next on the y axis
   * 
   */
  public Grid add(Sector sector, Point position) {
    if (!(sector instanceof LinkedSector))
      throw new IllegalArgumentException();
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

    return this;
  }

  /**
   * Adds a x first path to given position, adding new sectors when needed. 
   * a sector is NOT added at position (x,y)
   */
  private void placeNewSectorPathTo(Point pos) {
    int x = 0;
    int y = 0;
    if (pos.getX() != 0) {
      x = -(int) Math.signum(pos.getX());
    } else if (pos.getY() != 0) {
      y = -(int) Math.signum(pos.getY());
    } else {
      // we are at origin, so a path exists!
      return;
    }

    pos.translate(x, y);
    if (getSectorAt(pos) == null) {
      Sector s = new LinkedSector();
      add(s, pos);
      placeNewSectorPathTo(pos);

    }


    pos.translate(-x, -y); // restore point

  }

  public Sector getSectorAt(Point position) {
    return sectors.get(CantorDiagonal.transform(position));
  }

  public Point getPositionOf(Sector sector) {
    Integer i = sectors.findKey(sector);
    if (i == null)
      return null;
    return CantorDiagonal.transform(i);
  }

  public Grid add(Agent agent, Point position, Bearing bearing) {
    if (getSectorAt(position) == null)
      add(new LinkedSector(), position);

    int index = CantorDiagonal.transform(position);
    agentPositions.put(agent, position);
    agentBearings.put(agent, bearing);
    return this;
  }

  public Agent getAgent(String name) {
    for (Agent agent : getAgents()) {
      if (agent.getName().equals(name)) {
        return agent;
      }
    }
    return null;
  }

  public Agent getAgentAt(Point pos, Class cls) {
    // This is a cheat for optimization!

    for (int i = 0; i < agentPositions.values.size(); i++) {
      if (!pos.equals(agentPositions.values.get(i)))
        continue;
      Agent a = agentPositions.keys.get(i);

      if (a.getClass() == cls)
        return a;
    }
    
    return null;
  }

  public Point getPositionOf(Agent agent) {
    return agentPositions.get(agent);
  }

  public Bearing getBearingOf(Agent agent) {
    return agentBearings.get(agent);
  }

  public Grid moveTo(Agent agent, Point position, Bearing bearing) {
    if (agentPositions.get(agent) == null)
      throw new IllegalArgumentException();

    add(agent, position, bearing);

    return this;

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

  public String toString() {
    return GridUtils.createGridSectorsString(this);
  }

  // return a list of all agents
  public Iterable<Agent> getAgents() {
    return this.agentPositions.keys();
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

  // returns the sector at given absolute/relative coordinates or null
  public Sector getSector(int left, int top) {
    return (Sector) this.sectors.get(CantorDiagonal.transform(left, top));
  }

  public Iterable<Sector> getSectors() {
    return this.sectors.values();
  }

  private void connect(Sector sector, Sector other, Bearing location) {
    if (sector != null) {
      sector.addNeighbour(other, location);
    }
    if (other != null) {
      other.addNeighbour(sector, location.reverse());
    }
  }

  public int getSize() {
    return sectors.size();
  }
}
