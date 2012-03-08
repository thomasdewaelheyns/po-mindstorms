package penoplatinum.map.mazeprotocolinterpreter;

import java.util.Scanner;
import penoplatinum.map.Map;
import penoplatinum.simulator.tiles.Sector;

public class Discover implements Commando {

  @Override
  public void interpret(Map m, Scanner sc) {
    int x = sc.nextInt();
    int y = sc.nextInt();
    int data = 0;
    for(int i = 1; i<16; i*=2){
      data += i*sc.nextInt();
    }
    m.put(new Sector(data), x+1, y+1);
  }

  @Override
  public String getName() {
    return "DISCOVER";
  }

  @Override
  public boolean equals(Object obj) {
    if(super.equals(obj)){
      return true;
    }
    return getName().equals(obj);
  }
  
  
  
}
