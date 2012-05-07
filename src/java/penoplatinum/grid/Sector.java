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
  /**
   * TODO: attempt to remove this getter, it brings risks when using grid
   *        decoration
   */
  public Grid getGrid();

  public String toString();

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
  
  //TODO: do we need this? public char getWalls();


  
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
