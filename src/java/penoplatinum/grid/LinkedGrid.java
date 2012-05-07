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
import penoplatinum.grid.agent.Agent;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.CantorDiagonal;
import penoplatinum.util.Entry;
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
  public Grid add(Sector inSector, Point position) {
    if (!(inSector instanceof LinkedSector))
      throw new IllegalArgumentException();

    LinkedSector sector = (LinkedSector) inSector;

    sector.setId(CantorDiagonal.transform(position));

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
    if (agent == null) {
      throw new IllegalArgumentException();
    }
    if (getSectorAt(position) == null) {
      add(new LinkedSector(), position);
    }
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

    for (Entry e : agentPositions.entries()) {
      if (!pos.equals(e.value))
        continue;
      Agent a = (Agent) e.key;

      if (a.getClass() == cls)
        return a;
    }

    return null;
  }

  public Point getPositionOf(Agent agent) {
    if (agentPositions.get(agent) == null) {
      return null;
    }
    return agentPositions.get(agent).clone();
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
  public LinkedSector getSector(int left, int top) {
    return (LinkedSector) this.sectors.get(CantorDiagonal.transform(left, top));
  }

  public Iterable<Sector> getSectors() {
    return this.sectors.values();
  }

  private void connect(LinkedSector sector, LinkedSector other, Bearing location) {
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

  @Override
  public int getSectorId(Point position) {
    int ret = CantorDiagonal.transform(position);
    if (sectors.get(ret) == null)
      return -1;

    return ret;

  }

  @Override
  public LinkedSector getSector(int id) {
    if (id < 0)
      return null;
    return (LinkedSector) sectors.get(id);
  }

  public Point getPosition(int id) {
    if (id < 0)
      return null;
    return CantorDiagonal.transform(id);

  }

  @Override
  public boolean hasNeighbour(int sectorId, Bearing atBearing) {
    return getNeighbourId(sectorId, atBearing) != -1;
  }

  @Override
  public int getNeighbourId(int sectorId, Bearing atBearing) {
    Point p = getPosition(sectorId);
    if (p == null)
      return -1;
    p = new Point(Position.moveLeft(atBearing, p.getX()), Position.moveTop(atBearing, p.getY()));

    return getSectorId(p);
  }

  @Override
  public Grid setValue(int sectorId, int value) {
    getSector(sectorId).setValue(value);
    return this;
  }

  @Override
  public int getValue(int sectorId) {
    return getSector(sectorId).getValue();
  }

  @Override
  public Grid setWall(int sectorId, Bearing atBearing) {
    LinkedSector sector = getSector(sectorId);

    sector.setWall(atBearing);
    return this;
  }

  @Override
  public Grid setNoWall(int sectorId, Bearing atBearing) {
    LinkedSector sector = getSector(sectorId);

    sector.setNoWall(atBearing);
    return this;
  }

  @Override
  public Grid clearWall(int sectorId, Bearing atBearing) {
    LinkedSector sector = getSector(sectorId);

    sector.clearWall(atBearing);
    return this;
  }

  @Override
  public boolean hasWall(int sectorId, Bearing atBearing) {
    LinkedSector sector = getSector(sectorId);

    return sector.hasWall(atBearing);

  }

  @Override
  public boolean hasNoWall(int sectorId, Bearing atBearing) {
    LinkedSector sector = getSector(sectorId);
    return sector.hasNoWall(atBearing);
  }

  @Override
  public boolean knowsWall(int sectorId, Bearing atBearing) {
    LinkedSector sector = getSector(sectorId);
    return sector.knowsWall(atBearing);
  }

}
