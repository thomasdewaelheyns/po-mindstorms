package penoplatinum.grid;

/**
 * Grid Interface
 * 
 * Defines a 2D orthogonal Grid of Sectors with Agents
 *  
 * @author: Team Platinum
 */
import penoplatinum.util.Bearing;

import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;
import penoplatinum.util.Bearing;

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
//
//  // returns the boundaries and dimensions of the Grid
//  public int          getMinLeft();
//  public int          getMaxLeft();
//  public int          getMinTop();
//  public int          getMaxTop();
//  public int          getWidth();
//  public int          getHeight();
//  public int          getSize(); // the amount of Sectors
//

  /**
   * Gets the grid's current transformation
   * @return 
   */
  public TransformationTRT getTransformation();

  /**
   * Transform the grid. All grid functions will work as if the grid is 
   * transformed
   * @param transform
   * @return 
   */
  public Grid setTransformation(TransformationTRT transform);
  // using the navigation links on Sectors, the Grid can traverse through
  // the sectors, returning the Sector with requested left and top coordinates

  /**
   * Returns the sector at given position, if there is none, returns null
   * @param position
   * @return 
   */
  public Sector getSectorAt(Point position);

  public Point getPositionOf(Sector sector);

  public List<Sector> getSectors();

  /**
   * Adds given sector to the grid at given position
   * Note: this is a utility function, the 
   * 
   * @param s
   * @param position
   * @return 
   */
  public Grid add(Sector s, Point position);

  public Sector getSectorOf(Agent agent);

  public Bearing getBearingOf(Agent agent);

  /**
   * Places given agent on the grid at position. If the agent is already on the
   * grid, it will fail
   * If there is not sector at given position, the grid will create one
   * @param agent
   * @param position
   * @return 
   */
  public Grid add(Agent agent, Point position);

  /**
   * Returns the agent on this grid with given name, null when not found
   * @param name
   * @return 
   */
  public Agent getAgent(String name);

  /**
   * Returns an Iterable for the agents in this grid
   * @return 
   */
  public Iterable<Agent> getAgentsIterator();

  /**
   * Returns the position given agent is located at. 
   * 
   * @param agent
   * @return 
   */
  public Point getAgentPosition(Agent agent);

  /**
   * Copies the sectors + wall information to the target grid.
   * Agents are cloned using the Agent.createCopy() method
   * @param target
   * @return 
   */
  public Grid copyTo(Grid target);
}
