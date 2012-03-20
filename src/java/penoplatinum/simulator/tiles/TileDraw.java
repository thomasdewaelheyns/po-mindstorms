package penoplatinum.simulator.tiles;

import java.awt.Graphics2D;

public interface TileDraw {
  
  void drawTile(Graphics2D g2d, int left, int top);  
  int drawSize();
  
}
