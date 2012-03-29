package penoplatinum.grid;

/**
 * Grid Interface
 * 
 * Defines a 2D orthogonal Grid of Sectors with Agents
 *  
 * @author: Team Platinum
 */

import java.util.List;

import penoplatinum.util.Point;
 

public interface Grid {
  // set the root-processor for this Grid
  public Grid         setProcessor(GridProcessor processor);

  // a sector contains a root sector
  public Grid         addRoot(Sector sector);

  // using the navigation links on Sectors, the Grid can traverse through
  // the sectors, returning the Sector with requested left and top coordinates
  public Sector       getSector(Point position);
  public Point        getPosition(Sector sector);
  public List<Sector> getSectors();

  // Grid contains Agents
  public Grid         add(Agent agent, Point position);
  public Grid         moveForward(Agent agent);
  public Grid         remove(Agent agent);
  public Agent        getAgent(String name);
  public List<Agent>  getAgents();
  public Grid         clearAgents();
  public Point        getPosition(Agent agent);

  // returns the boundaries and dimensions of the Grid
  public int          getMinLeft();
  public int          getMaxLeft();
  public int          getMinTop();
  public int          getMaxTop();
  public int          getWidth();
  public int          getHeight();
  public int          getSize(); // the amount of Sectors
}
