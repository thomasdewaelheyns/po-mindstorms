package penoplatinum.grid;

/**
 * Grid Interface
 * 
 * Defines a 2D orthogonal Grid of Sectors with Agents
 *  
 * @author: Team Platinum
 */

import penoplatinum.util.Point;
import penoplatinum.util.Bearing;
import penoplatinum.util.TransformationTRT;

public interface Grid {
//  // set the root-processor for this Grid
//  public Grid         setProcessor(GridProcessor processor);
//
//  // a sector contains a root sector
//  public Grid         addRoot(Sector sector);
//
//  // using the navigation links on Sectors, the Grid can traverse through
//  // the sectors, returning the Sector with requested left and top coordinates
//  public Sector       getSector(Point position);
//  public Point        getPosition(Sector sector);
//  public List<Sector> getSectors();
//
//  // Grid contains Agents
//  public Grid         add(Agent agent, Point position);
//  public Grid         moveForward(Agent agent);
//  public Grid         remove(Agent agent);
//  public Agent        getAgent(String name);
//  public List<Agent>  getAgents();
//  public Grid         clearAgents();
//  public Point        getPosition(Agent agent);

  /**
   * Adds given sector to the grid at given position
   */
  public Grid add(Sector s, Point position);

  /**
   * Returns the sector at given position, if there is none, returns null
   */
  public Sector getSectorAt(Point position);

  public Point getPositionOf(Sector sector);

  public Iterable<Sector> getSectors();
  
  public boolean hasAgentOn(Sector sector, Class type);

  /**
   * Places given agent on the grid at position. If the agent is already on the
   * grid, it will fail
   * If there is no sector at given position, the grid will create one
   */
  public Grid add(Agent agent, Point position, Bearing bearing);
  
  /**
   * Moves given agent to specific location, if the agent is not on the grid
   * it will fail
   */
  public Grid moveTo(Agent agent, Point position, Bearing bearing);
  
  /**
   * Returns the sector on which the given agent is placed, null when the
   * agent is not on this grid
   */
  public Sector getSectorOf(Agent agent);

  public Bearing getBearingOf(Agent agent);

  //TODO: getagentat and getpositionof(agent)
  
  /**
   * Returns the agent on this grid with given name, null when not found
   */
  public Agent getAgent(String name);
  public Agent getAgentAt(Point position);

  /**
   * Returns an Iterable for the agents in this grid
   */
  public Iterable<Agent> getAgents();

  /**
   * Copies the sectors + wall information to the target grid.
   * Agents are cloned using the Agent.createCopy() method
   */
//  public Grid copyTo(Grid target);
  
  // returns the boundaries and dimensions of the Grid
  public int getMinLeft();

  public int getMaxLeft();

  public int getMinTop();

  public int getMaxTop();

  public int getWidth();

  public int getHeight();

  public int getSize(); // the amount of Sectors
}
