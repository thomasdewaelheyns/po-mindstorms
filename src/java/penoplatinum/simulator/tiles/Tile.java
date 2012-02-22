package penoplatinum.simulator.tiles;

import java.awt.Graphics2D;

public interface Tile {
  int getBarcode();
  int getColorAt(int x, int y);
  Boolean hasWall(int location);
  int toInteger();
  @Override
  String toString();
  int getSize();
  void drawTile(Graphics2D g2d, int left, int top);  
  int drawSize();
}
