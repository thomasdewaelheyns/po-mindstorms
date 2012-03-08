/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.map;

import penoplatinum.simulator.tiles.Tile;

/**
 *
 * @author Rupsbant
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
  
}
