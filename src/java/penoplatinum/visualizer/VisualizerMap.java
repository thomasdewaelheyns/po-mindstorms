/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.visualizer;

import java.util.Map;
import penoplatinum.simulator.tiles.Tile;

/**
 *
 * @author MHGameWork
 */
public interface VisualizerMap {

  // Returns the lowest x coord
  int getMinX();

  // Returns the highest x coord
  int getMaxX();

  // Returns the lowest y coord
  int getMinY();

  // Returns the highest y coord
  int getMaxY();

  int getWidth();

  int getHeight();

  int getTileCount();

  Tile get(int x, int y);

  // adds a tile at a given position. left,top 1-based
  Map put(Tile tile, int x, int y);

  Boolean exists(int x, int y);
}
