package penoplatinum.grid;

/**
 * Sector
 * 
 * A Sector is a single location in a Grid. It provides methods to access its
 * neighbours and holds sector wall information
 * 
 * @author: Team Platinum
 * 
 */

import penoplatinum.util.Bearing;


public interface Sector {
  public Sector putOn(Grid grid);
  public Grid getGrid();

  public String toString();

  /**
   * Sets given sector as a neighbour of this sector. The 'neighbour' sector is
   * removed from his old neighbours
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

  
  /**
   * True when each wall is known
   */
  public boolean isFullyKnown();
  public Sector clearWalls();
  /**
   * Returns true when we can say for sure that it is possible for the robot
   * to access the neighbour at given bearing
   * @param atBearing
   * @return 
   */
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
