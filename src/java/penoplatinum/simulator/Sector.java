/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator;

import penoplatinum.modelprocessor.BarcodeDataNav;

/**
 *
 * @author Thomas
 */
public class Sector implements Tile {
  
   private int data;
   
   private static int startWalls            = 0;
   private static int startBarcode          = startWalls + 4;
   private static int startIsItABarcode     = startBarcode + 4;
   private static int startBarcodeDirection = startIsItABarcode + 1;
   
   
   // logical measurements of a Sector, these are equal to the actual dimensions
  public static int SIZE               = 40;
  public static int LINE_WIDTH         =  1;
  public static int BARCODE_LINE_WIDTH =  2;
  public static int BARCODE_LINES      =  7;
  public static int BARCODE_WIDTH      = BARCODE_LINES * BARCODE_LINE_WIDTH;
  
  
  // Lines and corners are divided into two sets for white and black
  public static int NO_COLOR = -1;
  public static int WHITE    = 0;
  public static int BLACK    = 4;
  
  public void Sector(){
    this.data = 0;
  }

  @Override
  public int getBarcode() {
    if(this.hasBit(startIsItABarcode)){
      return BarcodeDataNav.expand[this.getBits(Sector.startBarcode,4)];
    }
    return -1;
  }

  @Override
  public int getColorAt(int x, int y) {
    if (IsOnLine(x, y)) 
      return Sector.WHITE;
    
    int color = this.getBarcodeColor (x,y);
    if(color != Sector.NO_COLOR) return color;
    
    color = Sector.NO_COLOR;
    return color;
  }
  
  
    // return the Color from the Barcode
  public int getBarcodeColor(int x, int y) {
    if( ! this.robotIsOnBarcode(x,y) ) { return Sector.NO_COLOR; }
    int line = 0;
    switch(this.getBarcodeLocation()){
      case 0:
        // north to south
        line = y - ((this.SIZE/2) - (this.BARCODE_WIDTH/2));        
        break;
      case 1:
        // south to north
        line = (this.SIZE/2)+(this.BARCODE_WIDTH/2) - y;        
        break;        
      case 2:
        //east to west
        line = (this.SIZE/2)+(this.BARCODE_WIDTH/2) - x;
        break;        
      case 3:
        // west to east
        line = x - ((this.SIZE/2) - (this.BARCODE_WIDTH/2));
        break;
    }
    line /= Sector.BARCODE_LINE_WIDTH;
    return getBarcodeLine(line);
  }
  
    // check if the robot is on a barcode
  private boolean robotIsOnBarcode(int x, int y) {
    
    if(this.hasBit(startIsItABarcode)){
      int tempBarcodeSize = BARCODE_WIDTH/2;
      int tempSize = Sector.SIZE/2;
      switch(this.getBits(startBarcodeDirection, 2)){
        case 1: return (x > (tempSize-tempBarcodeSize)) && (x < (tempSize+tempBarcodeSize));
        case 2: return (y > (tempSize-tempBarcodeSize)) && (y < (tempSize+tempBarcodeSize));
      }
    }
    return false;
  }
  
  public int getBarcodeLocation() {
    return this.getBits(Sector.startBarcodeDirection, 2);
  }
  
  public int getBarcodeLine(int line){
      return (this.getBarcode() & (1<<(Sector.BARCODE_LINES-line-1)) ) == 0 ?
                    Sector.BLACK : Sector.WHITE;
  }
  
  private boolean IsOnLine(int x, int y){
    int start = Sector.SIZE - Sector.LINE_WIDTH;
    int end = Sector.SIZE;
    
    if (hasWall(1) && start <= x && x < end )
      return true;
    if (hasWall(2) && start <= y && y < end )
      return true;
    if(hasWall(3) && x<=Sector.LINE_WIDTH){
      return true;
    }
    if(hasWall(0) && y<=Sector.LINE_WIDTH){
      return true;
    }
    
    return false;
  }

  @Override
  public Boolean hasWall(int location) {
    return this.hasBit(startWalls+location);
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
  public Sector addWall(int pos){
    this.setBit(startWalls+pos);
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
  public Sector addBarcode(int code, int direction){
    this.setBit(startIsItABarcode);
    this.setBits(startBarcode, 4, code);
    this.setBits(startBarcodeDirection, 2, direction);
    return this;
  }
  
  public void removeWall(int pos){
    this.unsetBit(pos);
  }
  
  public void removeBarcode(){
    this.unsetBit(startIsItABarcode);
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
  
  
  public int getSize(){
    return this.SIZE;
  }
}
