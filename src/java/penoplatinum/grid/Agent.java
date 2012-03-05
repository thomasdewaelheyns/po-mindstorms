package penoplatinum.grid;

/**
 * Agent Interface
 * 
 * Defines an agent on a Grid, travelling in a Manhattan-style
 * 
 * @author: Team Platinum
 */

import java.awt.Color;

// interface of an agent that is placed on the Grid
public interface Agent {
  public Agent    assignSector(Sector sector, int bearing);
  public Sector   getSector();
  
  public String   getName();
  public int      getValue();
  public Color    getColor();

  public int      getLeft();
  public int      getOriginalLeft();
  public int      getTop();
  public int      getOriginalTop();

  public int      getBearing();
  public int      getOriginalBearing();
  
  public Agent    turnLeft();
  public Agent    turnRight();

  public boolean  canMoveForward();
  public Agent    moveForward();
}
