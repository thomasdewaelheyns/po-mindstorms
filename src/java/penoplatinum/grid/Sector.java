package penoplatinum.grid;

/**
 * Sector
 * 
 * A Sector is a single location in a Grid. It has links and movement methods
 * to its neighbours. It can hold an Agent and be assigned a Tag. It stores
 * information abouts its walls and how certain it is about that information.
 * 
 * @author: Team Platinum
 * 
 * THIS IS WIP ...
 * 
 */

import penoplatinum.util.Point;
import penoplatinum.util.Bearing;
import penoplatinum.util.Rotation;
import penoplatinum.util.BitwiseOperations;


public interface Sector {
  public Sector putOn(Grid grid);
  public Grid getGrid();

  public String toString();

  /**
   * Sets given sector as a neighbour of this sector. The 'neighbour' sector is
   * removed from his old neighbours
   * @param neighbour
   * @param atBearing
   * @return 
   */
  public Sector addNeighbour(Sector neighbour, Bearing atBearing);
  public boolean hasNeighbour(Bearing atBearing);
  public Sector getNeighbour(Bearing atBearing);

  public Sector setValue(int value);
  public int getValue();
  
  public Sector setWall(Bearing atBearing);
  public Sector setNoWall(Bearing atBearing);
  public Sector clearWall(Bearing atBearing);

  public boolean hasWall(Bearing wall);
  public boolean hasNoWall(Bearing wall);
  public boolean knowsWall(Bearing atBearing);
  
  public char getWalls();

  
  
  public boolean isFullyKnown();
  public Sector clearWalls();
  public boolean givesAccessTo(Bearing atBearing);

  
  // OLD
  
  //  public int getCertainty();
  //  
  //  public Sector clearCertainty();
  //    
  //  public Sector addWalls(char walls);
  //  
  //  public Sector withWall(Bearing b);
  //  public void withWalls(char walls);
  //  public void withoutWall(Bearing atBearing);
  //  public void dontKnow(Bearing atBearing);
  //  public boolean isKnown(Bearing atBearing);
  
}
