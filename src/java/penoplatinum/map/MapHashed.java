package penoplatinum.map;

import penoplatinum.util.Point;
import java.util.HashMap;
import penoplatinum.simulator.tiles.Tile;

public class MapHashed implements Map {

  HashMap<Point, Tile> map = new HashMap();
  int minX = Integer.MAX_VALUE;
  int maxX = Integer.MIN_VALUE;
  int minY = Integer.MAX_VALUE;
  int maxY = Integer.MIN_VALUE;

  @Override
  public Map add(Tile t) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Boolean exists(int left, int top) {
    return map.containsKey(new Point(left+minX-1, top+minY-1));
  }

  @Override
  public Tile get(int left, int top) {
    return map.get(new Point(left+minX-1, top+minY-1));
  }
  
  public Tile getRaw(int left, int top) {
    return map.get(new Point(left, top));
  }

  @Override
  public Tile getFirst() {
    return map.values().toArray(new Tile[1])[0];
  }

  @Override
  public int getHeight() {
    return maxY - minY+1;
  }

  @Override
  public int getWidth() {
    return maxX - minX+1;
  }

  @Override
  public int getTileCount() {
    return map.size();
  }

  @Override
  public Map put(Tile tile, int left, int top) {
    if (top > maxY) {
      maxY = top;
    }
    if (top < minY) {
      minY = top;
    }
    if (left > maxX) {
      maxX = left;
    }
    if (left < minX) {
      minX = left;
    }
    map.put(new Point(left, top), tile);
    return this;
  }
}
