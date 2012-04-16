package penoplatinum.simulator.tiles;

import penoplatinum.util.Bearing;

public interface Tile {
  int getBarcode();
  int getColorAt(int x, int y);
  Boolean hasWall(Bearing location);
  int toInteger();
  @Override
  String toString();
  int getSize();
  TileDraw getDrawer();
}
