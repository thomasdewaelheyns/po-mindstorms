package penoplatinum.map;

import java.util.List;

import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Point;

/**
 * Map
 * 
 * Interface for Maps.
 * 
 * @author Team Platinum
 */
public interface Map {
  
  Map add(Tile t);

  /**
   * returns true if the tile exists at position left, top
   * indexed from 1 (one)
   */
  Boolean exists(int left, int top);

  /**
   * returns a tile at position left, top. first row/column are 1 (one)
   */
  Tile get(int left, int top);

  /**
   * Returns the first tile found.
   * This determines the type of the map.
   */
  Tile getFirst();

  int getHeight();

  int getTileCount();

  int getWidth();

  Map put(Tile tile, int left, int top);

  // TODO: this should be separated into a MapDescription class, from which
  //       a map can be constructed.
  // returns the positions where a ghost needs to be positioned
  List<Point> getGhostPositions();
  
  // returns the position where the pacman needs to be positioned
  Point getPacmanPosition();
}
