/**
 * Tile
 * 
 * Class representing a Baring. The internal data is stored as an integer, which
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
  
  public Boolean hasWall( int location ) {
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
  public Tile setBarcode( int code ) {
    this.setBits( 8, 4, code );
    return this;
  }

  public Tile unsetBarcode() {
    this.unsetBits( 8, 4 );
    return this;
  }

  public int getBarcode() {
    return this.getBits(8,4);
  }
  
  public Tile setBarcodeLocation( int location ) {
    this.setBits( 12, 3, location + 1 );
    return this;
  }

  public Tile unsetBarcodeLocation() {
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
