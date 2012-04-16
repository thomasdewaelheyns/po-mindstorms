package penoplatinum.map.mazeprotocol;

import java.util.HashMap;
import java.util.Scanner;
import penoplatinum.map.Map;
import penoplatinum.map.MapFactory;
import penoplatinum.map.MapHashed;
import penoplatinum.util.Bearing;

public class ProtocolMapFactory implements MapFactory{
  HashMap<String, Commando> commandos = new HashMap<String, Commando>();

  public ProtocolMapFactory() {
    addCommando(new BarcodeAt());
    addCommando(new Discover());
    addCommando(new Name());
    addCommando(new Pacman());
    addCommando(new Position());
  }

  @Override
  public Map getMap(Scanner sc){
    sc.useDelimiter("[, \\r\\n]+");
    
    MapHashed map = new MapHashed();
    while(sc.hasNext()){
      String tmp = sc.next(); //NAME
      String instruction = sc.next();
      Commando c = commandos.get(instruction);
      if(c == null){
        throw new RuntimeException("Unknown commando: " + instruction);
      }
      c.interpret(map, sc);
    }
    
    //printMap(map);
    
    return map;
  }

  private void printMap(MapHashed map) {
    System.out.println(map.getHeight());
    System.out.println(map.getWidth());
    for(int i = 1; i<map.getHeight(); i++){
      for(int j = 1; j<map.getWidth(); j++){
        System.out.print(j+","+i+",");
        System.out.print((map.get(j, i).hasWall(Bearing.N)?1:0));
        System.out.print((map.get(j, i).hasWall(Bearing.E)?1:0));
        System.out.print((map.get(j, i).hasWall(Bearing.S)?1:0));
        System.out.print((map.get(j, i).hasWall(Bearing.W)?1:0));
        System.out.print(" ");
        
      }
      System.out.println();
    }
  }

  private void addCommando(Commando c) {
    commandos.put(c.getName(), c);
  }
  
}
