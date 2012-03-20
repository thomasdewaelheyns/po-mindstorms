package penoplatinum.simulator.tiles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.view.Board;

public class PanelDraw implements TileDraw {
  
  // sizes in pixel format
  public static final int DRAW_LINE_START = (Panel.LINE_OFFSET * Board.SCALE);
  public static final int DRAW_LINE_END = (Panel.LINE_OFFSET + Panel.LINE_WIDTH) * Board.SCALE;
  public static final int DRAW_TILE_SIZE = Panel.SIZE * Board.SCALE;
  public static final int DRAW_LINE_WIDTH = Panel.LINE_WIDTH * Board.SCALE;
  public static final int DRAW_BARCODE_LINE_WIDTH = Panel.BARCODE_LINE_WIDTH * Board.SCALE;
  public static final int DRAW_WALL_LINE_WIDTH = 2 * Board.SCALE;
  
  private Panel p;

  public PanelDraw(Panel p) {
    this.p = p;
  }

  @Override
  public void drawTile(Graphics2D g2d, int left, int top) {
    // background
    g2d.setColor(Board.BROWN);
    g2d.fill(new Rectangle(DRAW_TILE_SIZE * (left - 1), DRAW_TILE_SIZE * (top - 1),
            DRAW_TILE_SIZE, DRAW_TILE_SIZE));
    this.renderLinesCross(g2d, left, top);
    this.renderLines(g2d, left, top);
    this.renderBarcode(g2d, left, top);
    this.renderNarrowing(g2d, left, top);
    this.renderWalls(g2d, left, top);
  }

  /**
   * Teken de lijnen in het midden van het paneel
   */
  private void renderLinesCross(Graphics2D g2d, int left, int top) {
    left--;
    top--;
    Rectangle horizontal = new Rectangle(
            left * DRAW_TILE_SIZE + DRAW_TILE_SIZE / 2 - DRAW_LINE_WIDTH / 2,
            top * DRAW_TILE_SIZE,
            DRAW_LINE_WIDTH,
            DRAW_TILE_SIZE);
    Rectangle vertical = new Rectangle(
            left * DRAW_TILE_SIZE,
            top * DRAW_TILE_SIZE + DRAW_TILE_SIZE / 2 - DRAW_LINE_WIDTH / 2,
            DRAW_TILE_SIZE,
            DRAW_LINE_WIDTH);
    g2d.setColor(Board.WHITE);


    g2d.fill(horizontal);
    g2d.fill(vertical);
  }

  private void renderLines(Graphics2D g2d, int left, int top) {
    this.renderLine(g2d, left, top, Bearing.N);
    this.renderLine(g2d, left, top, Bearing.E);
    this.renderLine(g2d, left, top, Bearing.S);
    this.renderLine(g2d, left, top, Bearing.W);

    this.renderCorner(g2d, left, top, Bearing.NE);
    this.renderCorner(g2d, left, top, Bearing.SE);
    this.renderCorner(g2d, left, top, Bearing.SW);
    this.renderCorner(g2d, left, top, Bearing.NW);
  }

  // TODO: further refactor this code, the switch is still ugly as hell ;-)
  private void renderLine(Graphics2D g2d, int left, int top, int line) {
    if (p.hasLine(line)) {
      g2d.setColor(p.hasLine(line, Panel.WHITE) ? Board.WHITE : Board.BLACK);
      int length = DRAW_TILE_SIZE;
      int offset = 0;
      if (p.hasLine(Bearing.getLeftNeighbour(line)) || p.hasLine(Bearing.getRightNeighbour(line))) {
        length -= DRAW_LINE_START;
      }
      if (p.hasLine(Bearing.getLeftNeighbour(line))) {
        offset += DRAW_LINE_START;
      }
      int x = 0, y = 0, dX = 0, dY = 0;
      switch (line) {
        case Bearing.N:
          dX = length;
          dY = DRAW_LINE_WIDTH;
          x = offset;
          y = DRAW_LINE_START;
          break;
        case Bearing.S:
          dX = length;
          dY = DRAW_LINE_WIDTH;
          x = offset;
          y = DRAW_TILE_SIZE - DRAW_LINE_START - DRAW_LINE_WIDTH;
          break;
        case Bearing.E:
          dX = DRAW_LINE_WIDTH;
          dY = length;
          x = DRAW_TILE_SIZE - DRAW_LINE_START - DRAW_LINE_WIDTH;
          y = offset;
          break;
        case Bearing.W:
          dX = DRAW_LINE_WIDTH;
          dY = length;
          x = DRAW_LINE_START;
          y = offset;
      }

      int dLeft = left - 1, dTop = top - 1;
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * dLeft + x,
              DRAW_TILE_SIZE * dTop + y,
              dX,
              dY));
    }
  }

  private void renderCorner(Graphics2D g2d, int left, int top, int corner) {
    if (p.hasCorner(corner)) {
      g2d.setColor(p.hasCorner(corner, Panel.WHITE)
              ? Board.WHITE : Board.BLACK);
      int offsetLeftH = 0, offsetTopH = 0,
              offsetLeftV = 0, offsetTopV = 0;
      switch (corner) {
        case Bearing.NE:
          offsetLeftH = offsetLeftV = DRAW_TILE_SIZE - DRAW_LINE_START - DRAW_LINE_WIDTH;
          offsetTopH = DRAW_LINE_START;
          offsetTopV = 0;
          break;
        case Bearing.SE:
          offsetLeftH = offsetLeftV = DRAW_TILE_SIZE - DRAW_LINE_START - DRAW_LINE_WIDTH;
          offsetTopH = offsetTopV = DRAW_TILE_SIZE - DRAW_LINE_START - DRAW_LINE_WIDTH;
          break;
        case Bearing.SW:
          offsetLeftH = 0;
          offsetLeftV = DRAW_LINE_START;
          offsetTopH = DRAW_TILE_SIZE - DRAW_LINE_START - DRAW_LINE_WIDTH;
          offsetTopV = DRAW_TILE_SIZE - DRAW_LINE_START - DRAW_LINE_WIDTH;
          break;
        case Bearing.NW:
          offsetLeftH = 0;
          offsetLeftV = DRAW_LINE_START;
          offsetTopV = 0;
          offsetTopH = DRAW_LINE_START;
          break;
      }
      int tileLeft = DRAW_TILE_SIZE * (left - 1);
      int tileTop = DRAW_TILE_SIZE * (top - 1);

      // horizontal
      g2d.fill(new Rectangle(
              tileLeft + offsetLeftH,
              tileTop + offsetTopH,
              DRAW_LINE_START + DRAW_LINE_WIDTH,
              DRAW_LINE_WIDTH));
      // vertical
      g2d.fill(new Rectangle(
              tileLeft + offsetLeftV,
              tileTop + offsetTopV,
              DRAW_LINE_WIDTH,
              DRAW_LINE_START + DRAW_LINE_WIDTH));

    }
  }

  private void renderBarcode(Graphics2D g2d, int left, int top) {
    // every bar of the barcode has a 2cm width = 4px
    for (int line = 0; line < 7; line++) {
      g2d.setColor(p.getBarcodeLine(line) == Panel.BLACK ? Board.BLACK : Board.WHITE);

      switch (p.getBarcodeLocation()) {
        case Bearing.N:
          g2d.fill(new Rectangle(
                  DRAW_TILE_SIZE * (left - 1),
                  DRAW_TILE_SIZE * (top - 1) + DRAW_BARCODE_LINE_WIDTH * line,
                  DRAW_TILE_SIZE,
                  DRAW_BARCODE_LINE_WIDTH));
          break;
        case Bearing.E:
          g2d.fill(new Rectangle(
                  DRAW_TILE_SIZE * (left) - DRAW_BARCODE_LINE_WIDTH * (line + 1),
                  DRAW_TILE_SIZE * (top - 1),
                  DRAW_BARCODE_LINE_WIDTH,
                  DRAW_TILE_SIZE));
          break;
        case Bearing.S:
          g2d.fill(new Rectangle(
                  DRAW_TILE_SIZE * (left - 1),
                  DRAW_TILE_SIZE * (top) - DRAW_BARCODE_LINE_WIDTH * (line + 1),
                  DRAW_TILE_SIZE,
                  DRAW_BARCODE_LINE_WIDTH));
          break;
        case Bearing.W:
          g2d.fill(new Rectangle(
                  DRAW_TILE_SIZE * (left - 1) + DRAW_BARCODE_LINE_WIDTH * line,
                  DRAW_TILE_SIZE * (top - 1) + DRAW_WALL_LINE_WIDTH,
                  DRAW_BARCODE_LINE_WIDTH,
                  DRAW_TILE_SIZE));
          break;
      }
    }
  }

  private void renderNarrowing(Graphics2D g2d, int left, int top) {
    // TODO
  }

  private void renderWalls(Graphics2D g2d, int left, int top) {
    // walls are 2cm width = 4px
    g2d.setColor(Board.DARK_BROWN);
    if (p.hasWall(Bearing.N)) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top - 1),
              DRAW_TILE_SIZE,
              DRAW_WALL_LINE_WIDTH));
    }
    if (p.hasWall(Bearing.E)) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * left - DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE * (top - 1),
              4,
              DRAW_TILE_SIZE));
    }
    if (p.hasWall(Bearing.S)) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top) - DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE,
              4));
    }
    if (p.hasWall(Bearing.W)) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top - 1),
              DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE));
    }
  }

  @Override
  public int drawSize() {
    return Sector.SIZE * Board.SCALE;
  }
}
