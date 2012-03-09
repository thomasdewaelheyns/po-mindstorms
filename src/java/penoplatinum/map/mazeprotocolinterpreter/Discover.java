package penoplatinum.map.mazeprotocolinterpreter;

import java.util.Scanner;
import penoplatinum.map.MapHashed;
import penoplatinum.simulator.tiles.Sector;

public class Discover implements Commando {

  @Override
  public void interpret(MapHashed m, Scanner sc) {
    int x = sc.nextInt();
    int y = sc.nextInt();
    int data = 0;
    Sector s = new Sector();
    for (int i = 0; i < 4; i++) {
      if (sc.nextInt() == 1) {
        s.addWall(i);
      }
    }
    m.put(s, x, -y);
  }

  @Override
  public String getName() {
    return "DISCOVER";
  }
}
