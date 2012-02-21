package penoplatinum.simulator;

import penoplatinum.simulator.tiles.Panels;
import penoplatinum.simulator.tiles.Panel;
import java.util.ArrayList;
import java.util.List;

/**
 * Map
 * 
 * Class representing a Map of Panels that represents the world of our robot.
 *
 *  @author: Team Platinum
 */
public class Map3D extends Map {

  private int width;  //X
  private int length; //Y
  private int height; //Z
  private List<Panel> tiles;

  public Map3D(int length, int width) {
    super();
    this.length = length;
    this.width = width;
    this.tiles = new ArrayList<Panel>();
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.length;
  }
  public int getHeightZ(){
    return this.height;
  }

  public int getTileCount() {
    return this.tiles.size();
  }

  /**
   * adds a tile to the end of the list. based on width left,top coordinates
   * are applied to retrieve them later. this method is used to add a stream
   * of tiles (e.g. reading them from a file).
   */
  public Map3D add(Panel tile) {
    this.tiles.add(tile);
    return this;
  }

  // adds a tile at a given position. left,top 1-based
  public Map3D put(Panel tile, int left, int top) {
    return put(tile, left-1, top-1, 0);
  }
  public Map3D put(Panel tile, int x, int y, int z) {
    int pos = getPosition(x, y, z);
    this.tiles.set(pos, tile);
    return this;
  }

  private int getPosition(int left, int top){
    return getPosition(left-1, top-1, 0);
  }
  private int getPosition(int x, int y, int z) {
    int pos = z * width * length;
    pos += y * width;
    pos += x;
    return pos;
  }

  /**
   * returns a tile at position left, top. first row/column are 1 (one)
   */
  public Panel get(int left, int top){
    return get(left-1, top-1, 0);
  }
  public Panel get(int x, int y, int z) {
    if(getPosition(x, y, z)<0){
      return Panels.NONE;
    }
    if(getPosition(x, y, z)>=tiles.size()){
      return Panels.NONE;
    }
    return this.tiles.get(getPosition(x, y, z));
  }

  /**
   * returns true if the tile exists at position left, top 
   * indexed from 1 (one)
   */
  public Boolean exists (int left, int top){
    return exists(left-1, top-1, 0);
  }
  public Boolean exists(int x, int y, int z) {
    int pos = getPosition(x, y, z);
    if (pos < 0 && pos >= this.tiles.size()) {
      return false;
    }
    return this.tiles.get(pos) != null;
  }
}
