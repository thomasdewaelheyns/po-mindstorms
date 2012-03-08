package penoplatinum.map.mazeprotocolinterpreter;

import java.util.Scanner;
import penoplatinum.map.Map;

public interface Commando {

  public void interpret(Map m, Scanner sc);

  @Override
  public boolean equals(Object o);

  public String getName();
}
