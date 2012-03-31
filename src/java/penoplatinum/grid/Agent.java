package penoplatinum.grid;

/**
 * Agent Interface
 * 
 * Defines an agent on a Grid.
 * 
 * @author: Team Platinum
 */

import penoplatinum.util.Color;


public interface Agent {
  // an agent is located on a Grid
  public Agent    assignGrid(Grid grid);
  public Grid     getGrid();

  // an agent has a name, a value and a color
  public String   getName();
  public int      getValue();
  public Color    getColor();

  // an agent can turn left, turn right and move forward
  public Agent    turnLeft();
  public Agent    turnRight();
  public Agent    moveForward();
  
  // an agent can be active or not
  public Agent    activate();
  public boolean  isActive();
  
  /**
   * Creates a copy if this agent, for use on another grid
   * @return 
   */
  public Agent createCopy();
}
