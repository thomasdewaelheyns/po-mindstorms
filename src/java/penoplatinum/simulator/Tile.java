package penoplatinum.simulator;

/**
 * Tile
 * 
 * Class representing a Tile. The internal data is stored as an integer, which
 * is treathed as a 32bit string, used to represent different information 
 * about the tile:
 * 
 * NESW NESW 123 123 .........
 * 
 * wall switches
 *   4 bits : N E S W
 * white line switches
 *   4 bits : N E S W
 * black line switches
 *   4 bits : N E S W
 * white line corner switches
 *   4 bits : NE SE SW NW
 * black line corner switches
 *   4 bits : NE SE SW NW
 * barcode
 *   4 bits : barcode is hex number 1..F
 * barcode position
 *   3 bits : none, N, E, S or W
 * narrowing orientation
 *   3 bits : none, N, E, S or W
 * total = 30 bits, 2 spare for future additional information (ramp,...)
 *
 *  Author: Team Platinum
 */

import java.awt.Point;

public class Tile {
  // these are the positions in the bitstring where relevant information
  // is stored. these variables allow for easier configuration of the bits
  private static int startWalls           = 0;
  private static int startLines           = startWalls   + 4;
  private static int startCorners         = startLines   + 8;
  private static int startBarcode         = startCorners + 8;
  private static int startBarcodeLocation = startBarcode + 4;
  private static int startNarrowing       = startBarcodeLocation + 3;

  // Lines and corners are divided into two sets for white and black
  public static int WHITE = 0;
  public static int BLACK = 4;
  
  // logical measurements of a Tile, these are equal to the actual dimensions
  public static int SIZE               = 80;
  public static int LINE_OFFSET        = 20;
  public static int LINE_WIDTH         =  1;
  public static int BARCODE_LINE_WIDTH =  2;

  // internal representation of a Tile using a 32bit int
  private int data;
  
  // by default a Tile is empty
  public Tile() {
    this.data = 0;
  }
  
  public Tile( int data ) {
    this.data = data;
  }
  
  /* Walls */
  public Tile withWall(int location) { 
    this.setBit(Tile.startWalls + location);
    return this;
  }
  
  public Tile withoutWall(int location)  { 
    this.unsetBit(Tile.startWalls + location);
    return this;
  }
  
  public Boolean hasWall(int location) {
    switch( location ) {
      case Baring.NE:
        return this.hasWall(Baring.N) || this.hasWall(Baring.E);
      case Baring.SE:
        return this.hasWall(Baring.S) || this.hasWall(Baring.E);
      case Baring.SW:
        return this.hasWall(Baring.S) || this.hasWall(Baring.W);
      case Baring.NW:
        return this.hasWall(Baring.N) || this.hasWall(Baring.W);
      default:
        // "simple" location, just check the bit
        return this.hasBit(Tile.startWalls + location);
    }
  }
  
  private static double T( double x, double d ) {
    /**
     * Geonometry used:
     *
     *            +
     *          / |
     *        /   |  Y
     *      / a   |
     *    +-------+
     *        X
     *
     *    tan(a) = Y/X    =>   Y = tan(a) * X     ||   X = Y / tan(a)
     */
    return x * Math.tan(Math.toRadians(d));
  }
  
  /**
   * calculates the point where given an angle and position, the  robot will 
   * "hit" a wall of this/a tile.
   */
  public static Point findHitPoint( int X, int Y, double angle, int size ) {
    double x, y, dx, dy;

    if( angle <= 90 ) {
      dx = size - X;
      dy = Tile.T( dx, angle );
      if( dy > Y ) {
        dy = Y;
        dx = Tile.T( dy, 90 - angle );
      }
      x = X + dx;
      y = Y - dy;
    } else if( angle > 90 && angle <= 180 ) {
      dx = X;
      dy = Tile.T( dx, 180-angle );
      if( dy > Y ) {
        dy = Y;
        dx = Tile.T( dy, angle - 90 );
      }
      x = X - dx;
      y = Y - dy;
    } else if( angle > 180 && angle <= 270 ) {
      dx = X;
      dy = Tile.T( dx, angle - 180 );
      if( dy > ( size - Y ) ) {
        dy = ( size - Y );
        dx = Tile.T( dy, 270 - angle );
      }
      x = X - dx;
      y = Y + dy;
    } else { 
      // angle > 270 && angle < 360
      dx = size - X;
      dy = Tile.T( dx, 360 - angle );
      if( dy > ( size - Y ) ) {
        dy = ( size - Y );
        dx = Tile.T( dy, angle - 270 );
      }
      x = X + dx;
      y = Y + dy;
    }
    
    return new Point((int)x,(int)y);
  }
  
  /**
   * based on a hit determine the wall that has been hit
   */
  public static int getHitWall(Point hit, int size) {
    int wall;
    if( hit.y == 0 ) {                          // North
      wall = hit.x == 0 ? Baring.NW : ( hit.x == size ? Baring.NE : Baring.N );
    } else if( hit.y == size ) {                  // South
      wall = hit.x == 0 ? Baring.SW : ( hit.x == size ? Baring.SE : Baring.S );
    } else {                                    // East or West
      wall = hit.x == 0 ? Baring.W : Baring.E;
    }
    return wall;
  }
  
  // simple application of a^2 + b^2 = c^2
  public static double getDistance( int x, int y, Point hit ) {
    return Math.sqrt( Math.pow(hit.x - x, 2 ) + Math.pow(hit.y - y, 2 ) );    
  }

  /* Lines */
  public Tile withLine(int location, int color) { 
    this.setBit(Tile.startLines + location + color);
    return this;
  }
  
  public Tile withoutLine(int location)  { 
    this.unsetBit(Tile.startLines + location + Tile.WHITE);
    this.unsetBit(Tile.startLines + location + Tile.BLACK);
    return this;
  }
  
  public Boolean hasLine(int location) {
    return this.hasLine(location, Tile.WHITE) 
        || this.hasLine(location, Tile.BLACK);
  }

  public Boolean hasLine(int location, int color) {
    return this.hasBit(Tile.startLines + location + color);
  }

  /* Corners */
  public Tile withCorner(int location, int color) { 
    this.setBit(Tile.startCorners + (location - 4) + color);
    return this;
  }
  
  public Tile withoutCorner(int location)  { 
    this.unsetBit(Tile.startCorners + (location - 4) + Tile.WHITE);
    this.unsetBit(Tile.startCorners + (location - 4) + Tile.BLACK);
    return this;
  }
  
  public Boolean hasCorner(int location) {
    return this.hasCorner(location, Tile.WHITE) 
        || this.hasCorner(location, Tile.BLACK);
  }

  public Boolean hasCorner(int location, int color) {
    return this.hasBit(Tile.startCorners + (location - 4) + color);
  }

  /* Barcode */
  public Tile withBarcode( int code ) {
    this.setBits(Tile.startBarcode, 4, code);
    return this;
  }

  public Tile withoutBarcode() {
    this.unsetBits(Tile.startBarcode, 4);
    return this;
  }

  public int getBarcode() {
    return this.getBits(Tile.startBarcode,4);
  }

  public Tile putBarcodeAt( int location ) {
    return this.withBarcodeLocation(location);
  }
  
  public Tile withBarcodeLocation( int location ) {
    this.setBits(Tile.startBarcodeLocation, 3, location + 1);
    return this;
  }

  public Tile withoutBarcodeLocation() {
    this.unsetBits(Tile.startBarcodeLocation, 3);
    return this;
  }

  public int getBarcodeLocation() {
    return this.getBits(Tile.startBarcodeLocation, 3) - 1;
  }
  
  /* Narrowing */
  public Tile setNarrowingOrientation( int orientation ) { 
    this.setBits(Tile.startNarrowing, 3, orientation + 1 );
    return this;
  }
  
  public Tile unsetNarrowingOrientation()  { 
    this.unsetBits(Tile.startNarrowing, 3);
    return this;
  }
  
  public int getNarrowingOrientation() {
    return this.getBits(Tile.startNarrowing, 3) - 1;
  }

  /* representations */
  public int toInteger() {
    return this.data;
  }

  public String toString() {
    String bits = "";
    for( int i=0; i<32; i++ ) {
      bits += this.hasBit(i) ? "1" : "0";
    }
    return bits;
  }

  /* elementary bitwise operations */

  // sets one bit at position to 1
  private void setBit(int p) {
    this.data |= (1<<p);
  }

  // sets one bit at position to 0
  private void unsetBit(int p) {
    this.data &= ~(1<<p);
  }
  
  // checks if at position the bit is set to 1
  private Boolean hasBit(int p) {
    return ( this.data & (1<<p) ) != 0;
  }

  // sets a range of bits, represented by value
  private void setBits(int start, int length, int value) {
    this.unsetBits( start, length );
    value <<= start;
    this.data |= value;
  }

  // sets a range of bits to 0
  private void unsetBits(int start, int length) {
    int mask = ( ( 1 << length ) - 1 ) << start;
    this.data &= ~(mask);
  }

  // returns a range of length bits starting at start
  private int getBits(int start, int length) {
    int mask = ( 1 << length ) - 1;
    return ( ( this.data >> start ) & mask );
  }
}
