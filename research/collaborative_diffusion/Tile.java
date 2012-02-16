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
  // internal representation of a Tile using a 32bit int
  private char data;
  
  // by default a Tile is empty
  public Tile() {
    this.data = 0;
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
  
  public Boolean hasWall(int location) {
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
  
  // checks if at position the bit is set to 1
  private Boolean hasBit(int p) {
    return ( this.data & (1<<p) ) != 0;
  }
  
}
