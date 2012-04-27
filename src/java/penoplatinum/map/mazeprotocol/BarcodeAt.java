package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import penoplatinum.map.MapHashed;
import penoplatinum.simulator.tiles.Sector;

public class BarcodeAt implements Commando {

  @Override
  public void interpret(MapHashed m, Scanner sc) {
    int x = sc.nextInt();
    int y = sc.nextInt();
    int code = sc.nextInt();
    int dir = sc.nextInt() - 1;
    Sector s = new Sector();

    s.addWall((dir + 1) % 4);
    s.addWall((dir + 3) % 4);
    s.removeWall((dir + 2) % 4);
    s.removeWall(dir);
    s.addBarcode(code, dir);
    m.put(s, x, -y);
  }

  @Override
  public String getName() {
    return "BARCODEAT";
  }
}
