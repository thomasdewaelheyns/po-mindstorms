package penoplatinum.simulator.tiles;

import penoplatinum.util.Bearing;

public interface Tile {
  int getBarcode8Bit();
  int getColorAt(int x, int y);
  boolean hasWall(Bearing location);
  @Override
  String toString();
  int getSize();
  TileDraw getDrawer();
}