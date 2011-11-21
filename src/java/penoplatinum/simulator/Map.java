package penoplatinum.simulator;

/**
 * Map
 * 
 * Class representing a Map of Tiles that represents the world of our robot.
 *
 *  Author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

public class Map {
  private int width;
  
  private List<Tile> tiles;
  
  public Map(int width) {
    this.width = width;
    this.tiles = new ArrayList<Tile>();
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public int getHeight() {
    return (int)Math.ceil(this.tiles.size() / 1.0 / this.width);
  }
  
  public int getTileCount() {
    return this.tiles.size();
  }
  
  /**
   * adds a tile to the end of the list. based on width left,top coordinates
   * are applied to retrieve them later. this method is used to add a stream
   * of tiles (e.g. reading them from a file).
   */
  public Map add( Tile tile ) {
    this.tiles.add(tile);
    return this;
  }

  // adds a tile at a given position. left,top 1-based
  public Map put( Tile tile, int left, int top ) {
    // TODO if useful
    // this.tiles.set( ( top * this.width ) + left, tile );
    return this;
  }
  
  /**
   * returns a tile at position left, top. first row/column are 1 (one)
   */
  public Tile get( int left, int top ) {
    return this.tiles.get( ( ( top - 1 ) * this.width ) + ( left - 1 ) );
  }
  
  /**
   * returns true if the tile exists at position left, top 
   * indexed from 1 (one)
   */
  public Boolean exists(int left, int top){
    int pos = ( ( top - 1 ) * this.width ) + (left - 1);
    if (pos < 0 && pos >= this.tiles.size()) {
      return false;
    }
    return this.tiles.get( pos ) != null;
  }
}
