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
  // an agent has a name, a value and a color
  public String   getName();
  public int      getValue();
  public Color    getColor();

  // an agent can be active or not
  public Agent    activate();
  public boolean  isActive();
  
}
