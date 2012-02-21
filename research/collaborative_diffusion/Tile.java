/**
 * Tile
 * 
 * Class representing a Tile. The internal data is stored as a byte
 * is treathed as a bit-field of 8 bits, used to represent the active walls
 * on the tile
 * 
 * wall switches
 *   4 bits : N E S W
 * 
 *  @author: Team Platinum
 */

public class Tile {
  // internal representation of a Tile using a 8bit int, first 4 bits
  // are used to store the wall information (0=no wall, 1=wall). the next 4
  // bits are used to store if we actually know if the walls is there or not
  private char data;

  // by default a Tile is empty
  public Tile() {
    this.clear();
  }
  
  public Tile clear() {
    this.data = 0;
    return this;
  }

  public Tile( char data ) {
    this.data = data;
  }

  /* Walls */
  public Tile withWall(int location) { 
    this.setBit(location);
    return this;
  }

  public Tile withWalls(char walls) {
    this.data = walls;
    return this;
  }

  public Tile withoutWall(int location)  { 
    this.unsetBit(location);
    return this;
  }

  public boolean hasWall(int location) {
    return this.hasBit(location);
  }

  public char getWalls() {
    return this.data;
  }

  public String toString() {
    String bits = "";
    for( int i=0; i<8; i++ ) {
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

  // checks if at position the bit is set to 1
  private Boolean hasBit(int p) {
    return ( this.data & (1<<p) ) != 0;
  }
}
