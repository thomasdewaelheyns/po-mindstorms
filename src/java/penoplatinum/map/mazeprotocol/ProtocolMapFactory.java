package penoplatinum.map.mazeprotocol;

import java.util.HashMap;
import java.util.Scanner;
import penoplatinum.map.Map;
import penoplatinum.map.MapFactory;
import penoplatinum.map.MapHashed;

public class ProtocolMapFactory implements MapFactory{
  HashMap<String, Commando> commandos = new HashMap<String, Commando>();

  public ProtocolMapFactory() {
    addCommando(new BarcodeAt());
    addCommando(new Discover());
    addCommando(new Name());
    addCommando(new Pacman());
    addCommando(new Position());
  }

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
    System.out.println(map.getHeight());
    System.out.println(map.getWidth());
    for(int i = 1; i<map.getHeight(); i++){
      for(int j = 1; j<map.getWidth(); j++){
        //System.out.print((map.get(j, i).hasWall(0)?1:0)+" ");
        //System.out.print((map.get(j, i).hasWall(1)?1:0)+" ");
        //System.out.print((map.get(j, i).hasWall(2)?1:0)+" ");
        //System.out.print((map.get(j, i).hasWall(3)?1:0)+" ");
        System.out.print(j+","+i+",");
        System.out.print((map.get(j, i).hasWall(0)?1:0));
        System.out.print((map.get(j, i).hasWall(1)?1:0));
        System.out.print((map.get(j, i).hasWall(2)?1:0));
        System.out.print((map.get(j, i).hasWall(3)?1:0));
        System.out.print(" ");
        
      }
      System.out.println();
    }
    return map;
  }

  private void addCommando(Commando c) {
    commandos.put(c.getName(), c);
  }
  
}
