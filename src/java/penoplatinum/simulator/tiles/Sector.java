package penoplatinum.simulator.tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import penoplatinum.BitwiseOperations;
import penoplatinum.barcode.BarcodeBlackBlack;
import penoplatinum.barcode.BarcodeCorrector;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.view.Board;

public class Sector implements Tile, Cloneable {

  private int data;
  public static final int barcodeLength = 5;
  private static int startWalls = 0;
  private static int startBarcode = startWalls + 4;
  private static int startIsItABarcode = startBarcode + barcodeLength;
  private static int startBarcodeDirection = startIsItABarcode + 1;
  private static int endBarcodeDirection = startIsItABarcode + 2;
  // logical measurements of a Sector, these are equal to the actual dimensions
  public static int SIZE = 40;
  public static int LINE_WIDTH = 2;
  public static int BARCODE_LINE_WIDTH = 2;
  public static int BARCODE_LINES = 8;
  public static int BARCODE_WIDTH = BARCODE_LINES * BARCODE_LINE_WIDTH;
  // Lines and corners are divided into two sets for white and black
  public static final int NO_COLOR = -1;
  public static final int WHITE = 0;
  public static final int BLACK = 4;
  public static final BarcodeCorrector barcode = new BarcodeBlackBlack();

  public Sector() {
    this.data = 0;
  }

  private Sector(int data) {
    this.data = data;
  }

  @Override
  public int getBarcode() {
    if (BitwiseOperations.hasBit(data, startIsItABarcode)) {
      return barcode.getExpand()[BitwiseOperations.getBits(data, Sector.startBarcode, Sector.barcodeLength)] * 2;
    }
    return -1;
  }

  @Override
  public int getColorAt(int x, int y) {
    if (IsOnLine(x, y)) {
      return Sector.WHITE;
    }



    int color = this.getBarcodeColor(x, y);
    if (color != Sector.NO_COLOR) {
      return color;
    }

    color = Sector.NO_COLOR;
    return color;
  }

  // return the Color from the Barcode
  public int getBarcodeColor(int x, int y) {
    if (!this.robotIsOnBarcode(x, y)) {
      return Sector.NO_COLOR;
    }

    int pos = ((getBarcodeLocation() & 1) != 0 ? x : y);
    pos -= (this.SIZE / 2);
    pos *= ((getBarcodeLocation() & 2) == 0 ? 1 : -1);
    pos += (this.BARCODE_WIDTH / 2);




    if (pos < -0.01f) {
      throw new RuntimeException("Error in barcode algorithm");
    }
    pos /= Sector.BARCODE_LINE_WIDTH;

//    float factor = pos - (int) pos;
//    return (int)(getBarcodeLine((int) (pos)) * factor + getBarcodeLine((int) (pos) + 1));

    return getBarcodeLine(pos);
  }

  public boolean hasBarcode() {
    return BitwiseOperations.hasBit(data, startIsItABarcode);
  }
  // check if the robot is on a barcode

  private boolean robotIsOnBarcode(int x, int y) {
    if (!hasBarcode()) {
      return false;
    }

    int tempBarcodeSize = BARCODE_WIDTH / 2;
    int tempSize = Sector.SIZE / 2;
    if (BitwiseOperations.hasBit(data, startBarcodeDirection)) {
      return (x >= (tempSize - tempBarcodeSize)) && (x < (tempSize + tempBarcodeSize));
    } else {
      return (y >= (tempSize - tempBarcodeSize)) && (y < (tempSize + tempBarcodeSize));
    }
  }

  public int getBarcodeLocation() {
    return BitwiseOperations.getBits(data, Sector.startBarcodeDirection, 2);
  }

  public int getBarcodeLine(int line) {
    return (this.getBarcode() & (1 << (Sector.BARCODE_LINES - line - 1))) == 0
            ? Sector.BLACK : Sector.WHITE;
  }

  private boolean IsOnLine(int x, int y) {
    int start = Sector.SIZE - Sector.LINE_WIDTH - 1;
    int end = Sector.SIZE;

    if (x >= Sector.LINE_WIDTH && x <= start && y >= Sector.LINE_WIDTH && y <= start) {
      return false;
    }

    if (hasWall(1) && start <= x && x < end) {
      return false;
    }
    if (hasWall(2) && start <= y && y < end) {
      return false;
    }
    if (hasWall(3) && x < Sector.LINE_WIDTH) {
      return false;
    }
    if (hasWall(0) && y < Sector.LINE_WIDTH) {
      return false;
    }


    return true;
  }

  @Override
  public Boolean hasWall(int location) {
    switch (location) {
      case Bearing.NE:
        return this.hasWall(Bearing.N) || this.hasWall(Bearing.E);
      case Bearing.SE:
        return this.hasWall(Bearing.S) || this.hasWall(Bearing.E);
      case Bearing.SW:
        return this.hasWall(Bearing.S) || this.hasWall(Bearing.W);
      case Bearing.NW:
        return this.hasWall(Bearing.N) || this.hasWall(Bearing.W);
      default:
        // "simple" location, just check the bit
        return BitwiseOperations.hasBit(data, Sector.startWalls + location);
    }
  }

  @Override
  public int toInteger() {
    return this.data;
  }

  @Override
  public String toString() {
    /*String bits = "";
    for( int i=0; i<32; i++ ) {
    bits += this.hasBit(i) ? "1" : "0";
    }
    return bits;/**/
    return Integer.toBinaryString(data);
  }

  /**
   * Adds a wall on the given position.
   * 0: north
   * 1: east
   * 2: south
   * 3: west
   * @param pos a position between 0 and 4
   */
  public Sector addWall(int pos) {
    data = BitwiseOperations.setBit(data, startWalls + pos);
    return this;
  }

  /**
   * Adds a barcode to a sector
   * code: the barcode
   * direction: 0: north to south
   *            1: south to north
   *            2: east to west
   *            3: west to east
   * @param code
   * @param direction 
   */
  public Sector addBarcode(int code, int direction) {
    data = BitwiseOperations.setBit(data, startIsItABarcode);
    data = BitwiseOperations.setBits(data, startBarcode, 4, code);
    data = BitwiseOperations.setBits(data, startBarcodeDirection, 2, direction);
    return this;
  }

  public void removeWall(int pos) {
    data = BitwiseOperations.unsetBit(data, pos);
  }

  public void removeBarcode() {
    data = BitwiseOperations.unsetBit(data, startIsItABarcode);
  }

  @Override
  public int getSize() {
    return this.SIZE;
  }

  public Sector clone() {
    return new Sector(data);
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

  private final TileDraw draw = new SectorDraw(this);
  @Override
  public TileDraw getDrawer() {
    return draw;
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
    if (hasWall(Bearing.N) == wallValue) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top - 1),
              DRAW_TILE_SIZE,
              DRAW_WALL_LINE_WIDTH));
    }

    if (hasWall(Bearing.E) == wallValue) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * left - DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE * (top - 1),
              4,
              DRAW_TILE_SIZE));
    }

    if (hasWall(Bearing.S) == wallValue) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top) - DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE,
              4));
    }

    if (hasWall(Bearing.W) == wallValue) {
      g2d.fill(new Rectangle(
              DRAW_TILE_SIZE * (left - 1),
              DRAW_TILE_SIZE * (top - 1),
              DRAW_WALL_LINE_WIDTH,
              DRAW_TILE_SIZE));
    }

  }

  private void renderBarcode(Graphics2D g2d, int left, int top) {
    if (!hasBarcode()) {
      return;
    }
    // every bar of the barcode has a 2cm width = 4px
    for (int line = 0; line < 8; line++) {
      g2d.setColor(getBarcodeLine(line) == Sector.BLACK ? Board.BLACK : Board.WHITE);
      int baseX = DRAW_TILE_SIZE * (left - 1);
      int baseY = DRAW_TILE_SIZE * (top - 1);

      int lijnRichting = ((getBarcodeLocation() & 2) == 0 ? line : (8 - line - 1));
      int dx = DRAW_TILE_SIZE / 2 - DRAW_BARCODE_WIDTH / 2 + DRAW_BARCODE_LINE_WIDTH * lijnRichting;

      int lengthX = 0;
      int lengthY = 0;
      if ((getBarcodeLocation() & 1) == 0) {
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

  public Sector clone() {
    return new Sector(data);
  }
}
