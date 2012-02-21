package penoplatinum.map;

import java.lang.String;
import java.util.HashMap;
import java.util.Scanner;
import penoplatinum.simulator.Map;
import penoplatinum.simulator.Map3D;
import penoplatinum.simulator.Panel;
import penoplatinum.simulator.Panels;

/**
 * 
 * @author: Team Platinum
 */
public class MapFactory {
  HashMap<String, Panel> tiles = new HashMap<String, Panel>();

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
        Panel next = tiles.get(str);
        if(next == null){
          next = Panels.NONE;
        }
        map.add(next);
      }
    }
    return map;
  }

  private void addTiles() {
    tiles.put("None.N", Panels.NONE);
    tiles.put("None.E", Panels.NONE);
    tiles.put("None.S", Panels.NONE);
    tiles.put("None.W", Panels.NONE);
    
    tiles.put("RCorner.N", Panels.S_E);
    tiles.put("RCorner.E", Panels.W_S);
    tiles.put("RCorner.S", Panels.N_W);
    tiles.put("RCorner.W", Panels.E_N);
    
    tiles.put("LCorner.N", Panels.S_W);
    tiles.put("LCorner.E", Panels.W_N);
    tiles.put("LCorner.S", Panels.N_E);
    tiles.put("LCorner.W", Panels.E_S);
    
    tiles.put("Straight.N", Panels.S_N);
    tiles.put("Straight.E", Panels.W_E);
    tiles.put("Straight.S", Panels.N_S);
    tiles.put("Straight.W", Panels.E_W);
    
    tiles.put("End.N", Panels.N);
    tiles.put("End.E", Panels.E);
    tiles.put("End.S", Panels.S);
    tiles.put("End.W", Panels.W);
   
  }
  
}
