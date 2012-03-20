package penoplatinum.simulator.tiles;

public interface Tile {
  int getBarcode();
  int getColorAt(int x, int y);
  Boolean hasWall(int location);
  int toInteger();
  @Override
  String toString();
  int getSize();
  TileDraw getDrawer();
}
