package penoplatinum.map;

import java.util.HashMap;
import java.util.Scanner;
import penoplatinum.map.mazeprotocolinterpreter.Commando;

public class ProtocolMapFactory {
  HashMap<String, Commando> commandos = new HashMap<String, Commando>();
  
  public Map getMap(Scanner sc){
    sc.useDelimiter("[, ]");
    int width = sc.nextInt();
    int length = sc.nextInt();
    
    Map map = new MapHashed();
    while(sc.hasNext()){
      sc.next(); //NAME
      String instruction = sc.next();
      Commando c = commandos.get(instruction);
      if(c == null){
        throw new RuntimeException("Unknown commando");
      }
      c.interpret(map, sc);
    }
    return map;
  }
  
}
