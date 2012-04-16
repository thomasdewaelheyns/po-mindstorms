package penoplatinum.simulator.tiles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import penoplatinum.simulator.view.Board;
import penoplatinum.util.Bearing;

public class SectorDraw implements TileDraw{
  
  public static int DRAW_WALL_LINE_WIDTH = 2 * Board.SCALE;
  public static int DRAW_TILE_SIZE = Sector.SIZE * Board.SCALE;
  public static int DRAW_LINE_WIDTH = Sector.LINE_WIDTH * Board.SCALE;
  public static int DRAW_BARCODE_LINE_WIDTH = Sector.BARCODE_LINE_WIDTH * Board.SCALE;
  public static int DRAW_BARCODE_WIDTH = Sector.BARCODE_WIDTH * Board.SCALE;
  
  private Sector s;

  public SectorDraw(Sector s) {
    this.s = s;
  }
  
   @Override
  public void drawTile(Graphics2D g2d, int left, int top) {
    renderBackground(g2d, left, top);
    //renderLinesCross(g2d, left, top);
    renderBarcode(g2d, left, top);
    g2d.setColor(Board.WHITE);
    renderWalls(g2d, left, top,false);
    g2d.setColor(Board.DARK_BROWN);
    renderWalls(g2d, left, top,true);
  }

  @Override
  public int drawSize() {
    return Sector.SIZE * Board.SCALE;
  }
 private void renderBackground(Graphics2D g2d, int left, int top) {
    g2d.setColor(Board.BROWN);
    g2d.fill(new Rectangle(DRAW_TILE_SIZE * (left - 1), DRAW_TILE_SIZE * (top - 1),
            DRAW_TILE_SIZE, DRAW_TILE_SIZE));
  }

  /**
   * This renders lines at wall positions, where hasWall equals wallValue
   * @param g2d
   * @param left
   * @param top
   * @param wallValue 
   */
  private void renderWalls(Graphics2D g2d, int left, int top, boolean wallValue) {
    // walls are 2cm width = 4px
    if (s.hasWall(Bearing.N) == wallValue) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top - 1),
              DRAW_TILE_SIZE,
              DRAW_WALL_LINE_WIDTH));
    }

    if (s.hasWall(Bearing.E) == wallValue) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * left - DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE * (top - 1),
              4,
              DRAW_TILE_SIZE));
    }

    if (s.hasWall(Bearing.S) == wallValue) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top) - DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE,
              4));
    }

    if (s.hasWall(Bearing.W) == wallValue) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top - 1),
              DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE));
    }

  }

  private void renderBarcode(Graphics2D g2d, int left, int top) {
    if (!s.hasBarcode()) {
      return;
    }
    // every bar of the barcode has a 2cm width = 4px
    for (int line = 0; line < 8; line++) {
      g2d.setColor(s.getBarcodeLine(line) == Sector.BLACK ? Board.BLACK : Board.WHITE);
      int baseX = DRAW_TILE_SIZE * (left - 1);
      int baseY = DRAW_TILE_SIZE * (top - 1);

      int lijnRichting = ((s.getBarcodeLocation() & 2) == 0 ? line : (8 - line - 1));
      int dx = DRAW_TILE_SIZE / 2 - DRAW_BARCODE_WIDTH / 2 + DRAW_BARCODE_LINE_WIDTH * lijnRichting;

      int lengthX = 0;
      int lengthY = 0;
      if ((s.getBarcodeLocation() & 1) == 0) {
        baseY += dx;
        lengthX = DRAW_TILE_SIZE;
        lengthY = DRAW_BARCODE_LINE_WIDTH;
      } else {
        baseX += dx;
        lengthX = DRAW_BARCODE_LINE_WIDTH;
        lengthY = DRAW_TILE_SIZE;
      }
      g2d.fill(new Rectangle(
              baseX,
              baseY,
              lengthX,
              lengthY));
    }
  }
}
