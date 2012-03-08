package penoplatinum.map;

import java.awt.Point;
import java.util.HashMap;
import penoplatinum.simulator.tiles.Tile;

public class MapHashed implements Map{
  HashMap<Point, Tile> map = new HashMap();

  @Override
  public Map add(Tile t) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Boolean exists(int left, int top) {
    return map.containsKey(new Point(left, top));
  }

  @Override
  public Tile get(int left, int top) {
    return map.get(new Point(left, top));
  }

  @Override
  public Tile getFirst() {
    return map.values().toArray(new Tile[1])[0];
  }

  @Override
  public int getHeight() {
    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;
    for(Point p : map.keySet()){
      if(p.getY()>max){
        max = (int) p.getY();
      }
      if(p.getY()<min){
        min = (int) p.getY();
      }
    }
    return max-min;
  }

  @Override
  public int getTileCount() {
    return map.size();
  }

  @Override
  public int getWidth() {
    int max = Integer.MIN_VALUE;
    int min = Integer.MAX_VALUE;
    for(Point p : map.keySet()){
      if(p.getY()>max){
        max = (int) p.getX();
      }
      if(p.getY()<min){
        min = (int) p.getX();
      }
    }
    return max-min;
  }

  @Override
  public Map put(Tile tile, int left, int top) {
    map.put(new Point(left, top), tile);
    return this;
  }
  
}
