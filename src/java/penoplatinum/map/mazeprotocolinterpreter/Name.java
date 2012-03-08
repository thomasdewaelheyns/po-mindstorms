package penoplatinum.map.mazeprotocolinterpreter;

import java.util.Scanner;
import penoplatinum.map.Map;

public class Name implements Commando {

  @Override
  public void interpret(Map m, Scanner sc) {
    sc.next(); //skip version;
  }

  @Override
  public boolean equals(Object obj) {
    if(super.equals(obj)){
      return true;
    }
    return getName().equals(obj);
  }
  
  

  @Override
  public String getName() {
    return "NAME";
  }
  
}
