package penoplatinum.map.mazeprotocolinterpreter;

import java.util.Scanner;

import penoplatinum.map.MapHashed;
import penoplatinum.util.Point;

public class Pacman implements Commando {

  @Override
  public void interpret(MapHashed map, Scanner scanner) {
    int x = scanner.nextInt(),
        y = scanner.nextInt();
    map.setPacmanPosition(new Point(x,y));
  }

  @Override
  public String getName() {
    return "PACMAN";
  }
}
