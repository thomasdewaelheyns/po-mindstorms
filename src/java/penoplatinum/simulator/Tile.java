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
 * line switches
 *   4 bits : N E S W
 * barcode
 *   4 bits : barcode is hex number 1..F
 * barcode position
 *   3 bits : none, N, E, S or W
 * narrowing orientation
 *   3 bits : none, N, E, S or W
 * total = 18 bits, 14 spare for future additional information (ramp,...)
 *
 *  Author: Team Platinum
 */

import java.awt.Point;

public class Tile {
  private int data;
  
  public Tile() {
    this.data = 0x0;
  }
  
  public Tile( int data ) {
    this.data = data;
  }
  
  /* Walls */
  public Tile setWall(int location) { 
    this.setBit(location);
    return this;
  }
  
  public Tile unsetWall(int location)  { 
    this.unsetBit(location);
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
        return this.hasBit(location);
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
  public Tile setLine(int location) { 
    this.setBit(location + 4);
    return this;
  }
  
  public Tile unsetLine(int location)  { 
    this.unsetBit(location + 4);
    return this;
  }
  
  public Boolean hasLine(int location) {
    return this.hasBit(location + 4);
  }
  
  /* Barcode */
  public Tile withBarcode( int code ) {
    this.setBits( 8, 4, code );
    return this;
  }

  public Tile unwithBarcode() {
    this.unsetBits( 8, 4 );
    return this;
  }

  public int getBarcode() {
    return this.getBits(8,4);
  }
  
  public Tile withBarcodeLocation( int location ) {
    this.setBits( 12, 3, location + 1 );
    return this;
  }

  public Tile unwithBarcodeLocation() {
    this.unsetBits( 12, 3 );
    return this;
  }

  public int getBarcodeLocation() {
    return this.getBits(12,3) - 1;
  }
  
  /* Narrowing */
  public Tile setNarrowingOrientation( int orientation ) { 
    this.setBits( 15, 3, orientation + 1 );
    return this;
  }
  
  public Tile unsetNarrowingOrientation()  { 
    this.unsetBits(15, 3);
    return this;
  }
  
  public int getNarrowingOrientation() {
    return this.getBits(15,3) - 1;
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
