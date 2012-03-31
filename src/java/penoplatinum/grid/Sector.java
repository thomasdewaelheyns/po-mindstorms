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
   * 
   * @param neighbour
   * @param atBearing
   * @return 
   */
  public Sector addNeighbour(Sector neighbour, Bearing atBearing);
//  public Sector createNeighbour(Bearing atBearing);
  public boolean hasNeighbour(Bearing atBearing);
  public Sector getNeighbour(Bearing atBearing);

  public Sector setValue(int value);
  public int getValue();

  public Sector addWall(Bearing atBearing);
  public void inheritWall(Bearing atBearing);
  public Sector removeWall(Bearing atBearing);
  public void inheritNoWall(Bearing atBearing);
  public Sector addWalls(char walls);

  public Boolean hasWall(Bearing wall);
  public boolean givesAccessTo(Bearing atBearing);
  public char getWalls();
  public Sector clearWall(Bearing atBearing);
  public Sector clearWalls();
  public Sector withWall(Bearing b);
  public void withWalls(char walls);
  public void withoutWall(Bearing atBearing);
  
  public Sector clearCertainty();
  public int getCertainty();
  
  public void dontKnow(Bearing atBearing);
  public boolean hasRawWall(Bearing atBearing);
  public boolean knowsWall(Bearing atBearing);
  public boolean isKnown(Bearing atBearing);
  public boolean isFullyKnown();
}
