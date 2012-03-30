package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import penoplatinum.map.MapHashed;

public interface Commando {

  public void interpret(MapHashed m, Scanner sc);

  public String getName();
}
