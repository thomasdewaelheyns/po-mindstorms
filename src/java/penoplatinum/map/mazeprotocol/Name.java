package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import penoplatinum.map.MapHashed;

public class Name implements Commando {

  @Override
  public void interpret(MapHashed m, Scanner sc) {
    sc.next(); //skip version;
  }

  @Override
  public String getName() {
    return "NAME";
  }
}
