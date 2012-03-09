package penoplatinum.map.mazeprotocolinterpreter;

import java.util.Scanner;
import penoplatinum.map.MapHashed;

public class Pacman implements Commando {

  @Override
  public void interpret(MapHashed m, Scanner sc) {
    sc.next();//x
    sc.next();//y
  }

  @Override
  public String getName() {
    return "PACMAN";
  }
}
