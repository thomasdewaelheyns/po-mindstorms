package penoplatinum.simulator.tiles;

/**
 * Panel
 * 
 * Class representing a Panel. The internal this.data is stored as an integer, which
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
 *  @author: Team Platinum
 */

import java.awt.Graphics2D;
import java.awt.Rectangle;
import penoplatinum.BitwiseOperations;
import penoplatinum.barcode.BarcodeCorrector;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.view.Board;

public class Panel implements Tile {
  // these are the positions in the bitstring where relevant information
  // is stored. these variables allow for easier configuration of the bits
  private static int startWalls           = 0;
  private static int startLines           = startWalls   + 4;
  private static int startCorners         = startLines   + 8;
  private static int startBarcode         = startCorners + 8;
  private static int startBarcodeLocation = startBarcode + 4;
  private static int startNarrowing       = startBarcodeLocation + 3;

  // Lines and corners are divided into two sets for white and black
  public static int NO_COLOR = -1;
  public static int WHITE    = 0;
  public static int BLACK    = 4;
  
  // logical measurements of a Panel, these are equal to the actual dimensions
  public static int SIZE               = 80;
  public static int LINE_OFFSET        = 20;
  public static int LINE_WIDTH         =  1;
  public static int LINE_CROSS_WIDTH   =  2;
  public static int BARCODE_LINE_WIDTH =  2;
  public static int BARCODE_LINES      =  7;
  public static int BARCODE_WIDTH      = BARCODE_LINES * BARCODE_LINE_WIDTH;
  
  // sizes in pixel format
  public static final int DRAW_LINE_START = (Panel.LINE_OFFSET * Board.SCALE);
  public static final int DRAW_LINE_END = (Panel.LINE_OFFSET +LINE_WIDTH )* Board.SCALE;
  public static final int DRAW_TILE_SIZE = Panel.SIZE * Board.SCALE;
  public static final int DRAW_LINE_WIDTH = Panel.LINE_WIDTH * Board.SCALE;
  public static final int DRAW_BARCODE_LINE_WIDTH = Panel.BARCODE_LINE_WIDTH * Board.SCALE;
  public static final int DRAW_WALL_LINE_WIDTH = 2*Board.SCALE;
  
//  private static final BarcodeCorrector barcode = new BarcodeHammingCorrector();

  // internal representation of a Panel using a 32bit int
  private int data;
  
  // by default a Panel is empty
  public Panel() {
    this.data = 0;
  }
  
  public Panel( int data ) {
    this.data = data;
  }
  
  /* Walls */
  public Panel withWall(int location) { 
    this.data = BitwiseOperations.setBit(this.data, Panel.startWalls + location);
    return this;
  }
  
  public Panel withoutWall(int location)  { 
    this.data = BitwiseOperations.unsetBit(this.data, Panel.startWalls + location);
    return this;
  }
  
  @Override
  public Boolean hasWall(int location) {
    switch( location ) {
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
        return BitwiseOperations.hasBit(this.data, Panel.startWalls + location);
    }
  }
  

  /* Lines */
  public Panel withLine(int location, int color) { 
    BitwiseOperations.setBit(this.data, Panel.startLines + location + color);
    return this;
  }
  
  public Panel withoutLine(int location)  { 
    this.data = BitwiseOperations.unsetBit(this.data, Panel.startLines + location + Panel.WHITE);
    this.data = BitwiseOperations.unsetBit(this.data, Panel.startLines + location + Panel.BLACK);
    return this;
  }
  
  public Boolean hasLine(int location) {
    return this.hasLine(location, Panel.WHITE) 
        || this.hasLine(location, Panel.BLACK);
  }

  public Boolean hasLine(int location, int color) {
    return BitwiseOperations.hasBit(this.data, Panel.startLines + location + color);
  }

  /* Corners */
  public Panel withCorner(int location, int color) { 
    this.data = BitwiseOperations.setBit(this.data, Panel.startCorners + (location - 4) + color);
    return this;
  }
  
  public Panel withoutCorner(int location)  { 
    this.data = BitwiseOperations.unsetBit(this.data, Panel.startCorners + (location - 4) + Panel.WHITE);
    this.data = BitwiseOperations.unsetBit(this.data, Panel.startCorners + (location - 4) + Panel.BLACK);
    return this;
  }
  
  public Boolean hasCorner(int location) {
    return this.hasCorner(location, Panel.WHITE) 
        || this.hasCorner(location, Panel.BLACK);
  }

  public Boolean hasCorner(int location, int color) {
    return BitwiseOperations.hasBit(this.data, Panel.startCorners + (location - 4) + color);
  }

  /* Barcode */
  public Panel withBarcode( int code ) {
    this.data = BitwiseOperations.setBits(this.data, Panel.startBarcode, 4, code);
    return this;
  }

  public Panel withoutBarcode() {
    this.data = BitwiseOperations.unsetBits(this.data, Panel.startBarcode, 4);
    return this;
  }

  @Override
  public int getBarcode() {
    throw new UnsupportedOperationException();
//    return barcode.getExpand()[BitwiseOperations.getBits(this.data, Panel.startBarcode,4)];
  }
  public int getBarcodeLine(int line){
      return (this.getBarcode() & (1<<(Panel.BARCODE_LINES-line-1)) ) == 0 ?
                    Panel.BLACK : Panel.WHITE;
  }

  public Panel putBarcodeAt( int location ) {
    return this.withBarcodeLocation(location);
  }
  
  public Panel withBarcodeLocation( int location ) {
    this.data = BitwiseOperations.setBits(this.data, Panel.startBarcodeLocation, 3, location + 1);
    return this;
  }

  public Panel withoutBarcodeLocation() {
    BitwiseOperations.unsetBits(this.data, Panel.startBarcodeLocation, 3);
    return this;
  }

  public int getBarcodeLocation() {
    return BitwiseOperations.getBits(this.data, Panel.startBarcodeLocation, 3) - 1;
  }
  
  /* Narrowing */
  public Panel setNarrowingOrientation( int orientation ) { 
    BitwiseOperations.setBits(this.data, Panel.startNarrowing, 3, orientation + 1 );
    return this;
  }
  
  public Panel unsetNarrowingOrientation()  { 
    BitwiseOperations.unsetBits(this.data, Panel.startNarrowing, 3);
    return this;
  }
  
  public int getNarrowingOrientation() {
    return BitwiseOperations.getBits(this.data, Panel.startNarrowing, 3) - 1;
  }

  /* representations */
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
    return Integer.toBinaryString(this.data);
  }
  
  // get the logical color at position x,y
  @Override
  public int getColorAt(int x, int y) {
    
    if (isOnCenterCrossLine(x, y)) 
      return Panel.WHITE;
    
    
//    int color = Panel.NO_COLOR;
    int color = this.getBarcodeColor (x,y);
    if( color == Panel.NO_COLOR ) { color = this.getLineColor  (x,y); }
    if( color == Panel.NO_COLOR ) { color = this.getCornerColor(x,y); }
    return color;
  }
  
  private boolean isOnCenterCrossLine(int x, int y){
    int start = Panel.SIZE/2 - Panel.LINE_CROSS_WIDTH/2;
    int end = Panel.SIZE/2 + Panel.LINE_CROSS_WIDTH/2;
    
    if (start < x && x < end )
      return true;
    if (start < y && y < end )
      return true;
    
    return false;
  }

  // return the Color from the Barcode
  public int getBarcodeColor(int x, int y) {
    if( ! this.robotIsOnBarcode(x,y) ) { return Panel.NO_COLOR; }
    int line = 0;
    switch(this.getBarcodeLocation()){
      case Bearing.N: line = y;             break;
      case Bearing.S: line = Panel.SIZE - y; break;
      case Bearing.E: line = Panel.SIZE - x; break;
      case Bearing.W: line = x;             break;
    }
    line /= Panel.BARCODE_LINE_WIDTH;
    return getBarcodeLine(line);
  }

  // check if the robot is on a barcode
  private boolean robotIsOnBarcode(int x, int y) {
    switch(this.getBarcodeLocation()){
      case Bearing.N: return y < Panel.BARCODE_WIDTH;
      case Bearing.E: return x > Panel.SIZE - Panel.BARCODE_WIDTH;
      case Bearing.S: return y > Panel.SIZE - Panel.BARCODE_WIDTH;
      case Bearing.W: return x < Panel.BARCODE_WIDTH;
    }
    return false;
  }

  private int getLineColor(int x, int y) {
    int color = Panel.NO_COLOR;

    // determine which line might be hit
    int position = Bearing.NONE;
    if( y == Panel.LINE_OFFSET && this.hasLine(Bearing.N) ) { 
      position = Bearing.N;
    }
    if( x == Panel.SIZE - Panel.LINE_OFFSET && this.hasLine(Bearing.E) ) { 
      position = Bearing.E;
    }
    if( y == Panel.SIZE - Panel.LINE_OFFSET && this.hasLine(Bearing.S) ) { 
      position = Bearing.S;
    }
    if( x == Panel.LINE_OFFSET && this.hasLine(Bearing.W) ) { 
      position = Bearing.W;
    }    
    
    // if the line forms a corner with a related line, we need to limit the 
    // scope of the line
    if (position == Bearing.N || position == Bearing.S){
      if(this.hasLine(Bearing.W) && x < Panel.LINE_OFFSET){
        position = Bearing.NONE;
      }        
      if(this.hasLine(Bearing.E) && x > Panel.SIZE - Panel.LINE_OFFSET) {
        position = Bearing.NONE;
      }
    }
    if ((position == Bearing.E || position == Bearing.W)) {
      if (this.hasLine(Bearing.N) && y < Panel.LINE_OFFSET){
        position = Bearing.NONE;
      }
      if (this.hasLine(Bearing.S) && y > Panel.SIZE - Panel.LINE_OFFSET) {
        position = Bearing.NONE;
      }
    }
    
    // check color of hit
    if(position != Bearing.NONE && this.hasLine(position)) {
      if(this.hasLine(position, Panel.WHITE))      { color = Panel.WHITE; }
      else if(this.hasLine(position, Panel.BLACK)) { color = Panel.BLACK; }
    }
      
    return color;
  }

  private int getCornerColor(int x, int y) {
    int color = Panel.NO_COLOR;

    // determine which corner might be hit
    int position = Bearing.NONE;
    if (x < Panel.LINE_OFFSET) {
      
      if (onLine(y, Panel.LINE_OFFSET))                                      {position = Bearing.NW;}
      else if (onLine(y, Panel.SIZE - Panel.LINE_OFFSET - Panel.LINE_WIDTH ))  {position = Bearing.SW;}
      
    } else if (x > Panel.SIZE - Panel.LINE_OFFSET) {
      
      if (onLine(y, Panel.LINE_OFFSET))                                      {position = Bearing.NE;}
      else if (onLine(y, Panel.SIZE - Panel.LINE_OFFSET - Panel.LINE_WIDTH ))  {position = Bearing.SE;}
      
    } else if (onLine(x, Panel.LINE_OFFSET)) {
      
      if (y < Panel.LINE_OFFSET || onLine(y, Panel.LINE_OFFSET))              {position = Bearing.NW;}
      else if (y >= Panel.SIZE - Panel.LINE_OFFSET 
              || onLine(y, Panel.SIZE - Panel.LINE_OFFSET-Panel.LINE_WIDTH))   {position = Bearing.SW;}
      
    } else if (onLine(x, Panel.SIZE - Panel.LINE_OFFSET)) {
      
      if (y < Panel.LINE_OFFSET || onLine(y, Panel.LINE_OFFSET))              {position = Bearing.NE;}
      else if (y >= Panel.SIZE - Panel.LINE_OFFSET 
              || onLine(y, Panel.SIZE - Panel.LINE_OFFSET-Panel.LINE_WIDTH))   {position = Bearing.SE;}
      
    }
    // check color of hit
    if(position != Bearing.NONE && this.hasCorner(position)) {
      if(this.hasCorner(position, Panel.WHITE))      { color = Panel.WHITE; }
      else if(this.hasCorner(position, Panel.BLACK)) { color = Panel.BLACK; }
    }
      
    return color;
  }

  public boolean onLine(int y, int offset) {
    return y >= offset
               && y < offset+Panel.LINE_WIDTH;
  }
  
  @Override
  public int getSize(){
    return this.SIZE;
  }

  private final TileDraw draw = new PanelDraw(this);
  @Override
  public TileDraw getDrawer() {
    return draw;
  }
}
