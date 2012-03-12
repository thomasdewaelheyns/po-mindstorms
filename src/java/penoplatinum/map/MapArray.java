package penoplatinum.map;

/**
 * Map
 * 
 * Class representing a Map of Tiles that represents the world of our robot.
 *
 *  @author: Team Platinum
 */
import penoplatinum.simulator.tiles.Tile;
import java.util.ArrayList;

public class MapArray implements Map {

  public static MapArray lastMap;
  
  private int width;
  private ArrayList<Tile> tiles;

  protected MapArray() {
    this.tiles = new ArrayList<Tile>();
    lastMap = this;
  }

  public MapArray(int width) {
    this();
    this.width = width;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return (int) Math.ceil(this.tiles.size() / 1.0 / this.width);
  }

  @Override
  public int getTileCount() {
    return this.tiles.size();
  }

  /**
   * adds a tile to the end of the list. based on width left,top coordinates
   * are applied to retrieve them later. this method is used to add a stream
   * of tiles (e.g. reading them from a file).
   */
  public Map add(Tile tile) {
    this.tiles.add(tile);
    return this;
  }

  // adds a tile at a given position. left,top 1-based
  @Override
  public Map put(Tile tile, int left, int top) {
    // TODO if useful
    int index = getPosition(left, top);
    while (this.tiles.size() <= index) {
      this.tiles.add(null);
    }
    this.tiles.set(index, tile);
    return this;
  }

  /**
   * returns a tile at position left, top. first row/column are 1 (one)
   */
  @Override
  public Tile get(int left, int top) {
    if (left < 1) return null;
    if (top < 1) return null;
    int index = getPosition(left, top);
    if (index >= tiles.size()) {
      return null;
    }

    return this.tiles.get(index);
  }
  /*public Panel get(int left, int top, int level){
  return get(left, top);
  }/**/

  private int getPosition(int left, int top) {
    return ((top - 1) * this.width) + (left - 1);
  }

  /**
   * returns true if the tile exists at position left, top 
   * indexed from 1 (one)
   */
  /*public Boolean exists(int left, int top, int level){
  return exists(left, top);
  }/**/
  @Override
  public Boolean exists(int left, int top) {
    int pos = getPosition(left, top);
    if (pos < 0 && pos >= this.tiles.size()) {
      return false;
    }
    return this.tiles.get(pos) != null;
  }

  /**
   * Returns the first tile found.
   * This determines the type of the map.
   */
  @Override
  public Tile getFirst() {
    return tiles.iterator().next();
  }
  
  
  
  
}
