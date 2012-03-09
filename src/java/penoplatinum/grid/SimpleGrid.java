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
import penoplatinum.map.Point;
import penoplatinum.simulator.mini.Bearing;

// we're using commons collections HashedMap because HashMap isn't implemented
// on Lejos.
public class SimpleGrid implements Grid {
  // we keep track of the boundaries of our Grid

  int minLeft = 0, maxLeft = 0, minTop = 0, maxTop = 0;
  // mapping from coordinates to allocating Sector
  private SimpleHashMap<String, Sector> sectors = new SimpleHashMap<String, Sector>();
  private SimpleHashMap<String, Sector> tags = new SimpleHashMap<String, Sector>();
  // all agents in a row
  private List<Agent> agents = new ArrayList<Agent>();
  // visualization for the Grid, by default none, is used by Simulator
  private GridView view = new NullGridView();
  private GridProcessor processor;

  public Grid setProcessor(GridProcessor processor) {
    this.processor = processor;
    this.processor.useGrid(this);
    return this;
  }

  // adds a sector to the grid.
  // a sector already contains its own coordinates, based on the coordinates
  // of the sector is was attached to. we store the sector using a <left,top>
  // string-key
  public Grid addSector(Sector sector) {
    sector.putOn(this);

    int left = sector.getLeft(),
            top = sector.getTop();
    this.resize(left, top);

    // add the sector to the list of all sectors in this grid
    this.sectors.put(left + "," + top, sector);
    // connect neighbours
    this.connect(sector, this.getSector(left, top - 1), Bearing.N);
    this.connect(sector, this.getSector(left + 1, top), Bearing.E);
    this.connect(sector, this.getSector(left, top + 1), Bearing.S);
    this.connect(sector, this.getSector(left - 1, top), Bearing.W);

    this.view.sectorsNeedRefresh();

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
    return (Sector) this.sectors.get(left + "," + top);
  }

  public List<Sector> getSectors() {
    @SuppressWarnings("unchecked")
    List<Sector> sectors = new ArrayList(this.sectors.values());
    return sectors;
  }

  private void connect(Sector sector, Sector other, int location) {
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
    this.view.display(this);
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

  // dumps the Grid using its values
  public Grid dump() {
    for (int top = this.getMinTop(); top <= this.getMaxTop(); top++) {
      for (int left = this.getMinLeft(); left <= this.getMaxLeft(); left++) {
        Sector sector = this.getSector(left, top);
        int walls, value;
        if (sector != null) {
          walls =
                  value = sector.getValue();
//          System.out.printf("%5d ", (int) sector.getValue());
        } else {
//          System.out.print("../.....");
        }
      }
      System.out.println("");
    }
    return this;
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
    for (Agent agent : this.agents) {
      agent.getSector().removeAgent();
    }
    this.agents.clear();
    this.view.agentsNeedRefresh();
    return this;
  }

  public Grid sectorsNeedRefresh() {
    this.view.sectorsNeedRefresh();
    return this;
  }

  public Grid wallsNeedRefresh() {
    // TODO: also separate walls update
    this.view.sectorsNeedRefresh();
    return this;
  }

  public Grid valuesNeedRefresh() {
    this.view.valuesNeedRefresh();
    return this;
  }

  public Grid agentsNeedRefresh() {
    this.view.agentsNeedRefresh();
    return this;
  }

  public void importGrid(Grid g, int localX, int localY, int otherX, int otherY, int rotation) {
    for (int i = 0; i < g.getSectors().size(); i++) {
      Sector s = g.getSectors().get(i);

      // Relative current other sector to otherx and y
      int x = s.getLeft() - otherX;
      int y = s.getTop() - otherY;

      // Now rotate this vector
      Point p = Bearing.mapToNorth(rotation, x, y);

      // Now apply this vector to our coordinates
      x += localX;
      y += localY;


      Sector thisSector = getSector(x, y);

      if (thisSector == null) {
        thisSector = new Sector(this).setCoordinates(x, y);
        addSector(thisSector);
      }

      for (int j = Bearing.N; j <= Bearing.W; j++) {
        int otherBearing = (j + rotation) % 4; // TODO check direction

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

        if (newVal == null) {
          thisSector.clearWall(j);
        } else if (newVal) {
          thisSector.addWall(j);
        } else {
          thisSector.removeWall(j);
        }
      }

    }
  }
}
