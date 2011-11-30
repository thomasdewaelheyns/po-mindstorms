package penoplatinum.map;

import java.lang.String;
import java.util.HashMap;
import java.util.Scanner;
import penoplatinum.simulator.Map;
import penoplatinum.simulator.Map3D;
import penoplatinum.simulator.Tile;
import penoplatinum.simulator.Tiles;

public class MapFactory {
  HashMap<String, Tile> tiles = new HashMap<String, Tile>();

  public MapFactory() {
    addTiles();
  }
  
  public Map getMap(Scanner sc){
    int width = sc.nextInt();
    int length = sc.nextInt();
    int height = sc.nextInt();
    Map map = new Map3D(length, width);
    for(int i = 0; i <length; i++){
      for(int j = 0; j <width; j++){
        String str = sc.next();
        Tile next = tiles.get(str);
        if(next == null){
          next = Tiles.NONE;
        }
        map.add(next);
      }
    }
    return map;
  }

  private void addTiles() {
    tiles.put("None.N", Tiles.NONE);
    tiles.put("None.E", Tiles.NONE);
    tiles.put("None.S", Tiles.NONE);
    tiles.put("None.W", Tiles.NONE);
    
    tiles.put("RCorner.N", Tiles.S_E);
    tiles.put("RCorner.E", Tiles.W_S);
    tiles.put("RCorner.S", Tiles.N_W);
    tiles.put("RCorner.W", Tiles.E_N);
    
    tiles.put("LCorner.N", Tiles.S_W);
    tiles.put("LCorner.E", Tiles.W_N);
    tiles.put("LCorner.S", Tiles.N_E);
    tiles.put("LCorner.W", Tiles.E_S);
    
    tiles.put("Straight.N", Tiles.S_N);
    tiles.put("Straight.E", Tiles.W_E);
    tiles.put("Straight.S", Tiles.N_S);
    tiles.put("Straight.W", Tiles.E_W);
    
    tiles.put("End.N", Tiles.N);
    tiles.put("End.E", Tiles.E);
    tiles.put("End.S", Tiles.S);
    tiles.put("End.W", Tiles.W);
   
  }
  
}
