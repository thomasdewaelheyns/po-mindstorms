package penoplatinum.grid;

/**
 * Grid Interface
 * 
 * Defines a 2D orthogonal Grid of Sectors with Agents
 *  
 * @author: Team Platinum
 */
import penoplatinum.grid.agent.Agent;
import penoplatinum.util.Point;
import penoplatinum.util.Bearing;

/**
 * Optimization idea: replace all Points by sectors!!
 * 
 * @author MHGameWork
 */
public interface Grid {

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
   * Returns the position of the given agent, null when the
   * agent is not on this grid
   */
  public Point getPositionOf(Agent agent);

  public Bearing getBearingOf(Agent agent);

  //TODO: getagentat and getpositionof(agent)
  /**
   * Returns the agent on this grid with given name, null when not found
   */
  public Agent getAgent(String name);

  public Agent getAgentAt(Point position, Class cls);

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

  // SUPERSPEED FUNCTIOOONS
  public int getSectorId(Point position);

  public Sector getSector(int id);

  public boolean hasNeighbour(int sectorId, Bearing atBearing);

  public int getNeighbourId(int sectorId, Bearing atBearing);

  public Grid setValue(int sectorId, int value);

  public int getValue(int sectorId);

  public Grid setWall(int sectorId, Bearing atBearing);

  public Grid setNoWall(int sectorId, Bearing atBearing);

  public Grid clearWall(int sectorId, Bearing atBearing);

  public boolean hasWall(int sectorId, Bearing atBearing);

  public boolean hasNoWall(int sectorId, Bearing atBearing);

  public boolean knowsWall(int sectorId, Bearing atBearing);

  public boolean isFullyKnown(int sectorId);

  public Grid clearWalls(int sectorId);

  public boolean givesAccessTo(int sectorId, Bearing atBearing);
}
