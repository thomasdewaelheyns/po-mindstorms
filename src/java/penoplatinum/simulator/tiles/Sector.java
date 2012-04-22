package penoplatinum.simulator.tiles;

import penoplatinum.util.Bearing;
import penoplatinum.util.BitwiseOperations;

public class Sector implements Tile, Cloneable {

  private int data;
  private static final int barcodeLength = 6;
  private static final int startWalls = 0;
  private static final int startBarcode = startWalls + 4;
  private static final int startHasBarcode = startBarcode + barcodeLength;
  private static final int startBarcodeDirection = startHasBarcode + 1;
  private static final int endBarcodeDirection = startBarcodeDirection + 2;
  // logical measurements of a Sector, these are equal to the actual dimensions
  // (in cm)
  public static int SIZE = 40;
  public static int LINE_WIDTH = 1;
  public static int BARCODE_LINE_WIDTH = 2;
  public static int BARCODE_LINES = 8;
  public static int BARCODE_WIDTH = BARCODE_LINES * BARCODE_LINE_WIDTH;
  // Lines and corners are divided into two sets for white and black
  public static final int NO_COLOR = -1;
  public static final int WHITE = 0;
  public static final int BLACK = 4;

  public Sector() {
    this.data = 0;
  }

  public Sector(int data) {
    this.data = data;
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
    pos += ((getBarcodeLocation() & 2) == 0 ? 0 : -1);
    pos += (this.BARCODE_WIDTH / 2);
    pos /= Sector.BARCODE_LINE_WIDTH;
    return getBarcodeLine(pos);
  }

  @Override
  public int getBarcode8Bit() {
    if (this.hasBarcode()) {
      return BitwiseOperations.getBits(data, Sector.startBarcode, Sector.barcodeLength) * 2;
    }
    return -1;
  }

  public boolean hasBarcode() {
    return BitwiseOperations.hasBit(data, startHasBarcode);
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
    if (!this.hasBarcode()) {
      return -1;
    }
    return BitwiseOperations.getBits(data, Sector.startBarcodeDirection, 2);
  }

  public int getBarcodeLine(int line) {
    return (this.getBarcode8Bit() & (1 << (Sector.BARCODE_LINES - line - 1))) == 0
            ? Sector.BLACK : Sector.WHITE;
  }

  private boolean IsOnLine(int x, int y) {
    if (x < 0 || y < 0 || x >= Sector.SIZE || y >= Sector.SIZE) { // Out of bounds
      throw new RuntimeException("Out of bounds in sector");
    }

    int start = Sector.SIZE - Sector.LINE_WIDTH;
    int end = Sector.SIZE;
    if (x >= Sector.LINE_WIDTH && x < start && y >= Sector.LINE_WIDTH && y < start) {
      return false;
    }

    if (hasWall(Bearing.E) && start <= x && x < end) {
      return false;
    }
    if (hasWall(Bearing.S) && start <= y && y < end) {
      return false;
    }
    if (hasWall(Bearing.W) && x < Sector.LINE_WIDTH) {
      return false;
    }
    if (hasWall(Bearing.N) && y < Sector.LINE_WIDTH) {
      return false;
    }
    return true;
  }

  @Override
  public boolean hasWall(Bearing location) {
    switch (location) {
      case NE:
        return this.hasWall(Bearing.N) || this.hasWall(Bearing.E);
      case SE:
        return this.hasWall(Bearing.S) || this.hasWall(Bearing.E);
      case SW:
        return this.hasWall(Bearing.S) || this.hasWall(Bearing.W);
      case NW:
        return this.hasWall(Bearing.N) || this.hasWall(Bearing.W);
      case N:
        return BitwiseOperations.hasBit(data, startWalls+0);
      case E:
        return BitwiseOperations.hasBit(data, startWalls+1);
      case S:
        return BitwiseOperations.hasBit(data, startWalls+2);
      case W:
        return BitwiseOperations.hasBit(data, startWalls+3);
      default:
        throw new RuntimeException("Unknown bearing");
    }
  }

  /*
   * Unused
   *
  @Override
  public int toInteger() {
  return this.data;
  }/**/
  @Override
  public String toString() {
    return Integer.toBinaryString(data | (1 << endBarcodeDirection)).substring(1);
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
    data = BitwiseOperations.setBit(data, startHasBarcode);
    data = BitwiseOperations.setBits(data, startBarcode, barcodeLength, code);
    data = BitwiseOperations.setBits(data, startBarcodeDirection, 2, direction);
    return this;
  }

  public void removeWall(int pos) {
    data = BitwiseOperations.unsetBit(data, pos);
  }

  public void removeBarcode() {
    data = BitwiseOperations.unsetBit(data, startHasBarcode);
    data = BitwiseOperations.unsetBits(data, startBarcodeDirection, 2);
    data = BitwiseOperations.unsetBits(data, startBarcode, barcodeLength);
  }

  @Override
  public int getSize() {
    return Sector.SIZE;
  }

  @Override
  public Sector clone() {
    return new Sector(data);
  }
  private final TileDraw draw = new SectorDraw(this);

  @Override
  public TileDraw getDrawer() {
    return draw;
  }

  public int getData() {
    return data;
  }
}