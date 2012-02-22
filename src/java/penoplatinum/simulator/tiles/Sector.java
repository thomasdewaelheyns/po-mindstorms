package penoplatinum.simulator.tiles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import penoplatinum.BitwiseOperations;
import penoplatinum.modelprocessor.BarcodeDataNav;
import penoplatinum.simulator.Baring;
import penoplatinum.simulator.view.Board;

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
  
  public static final int DRAW_WALL_LINE_WIDTH = 2*Board.SCALE;
  public static final int DRAW_TILE_SIZE = Sector.SIZE * Board.SCALE;
  public static final int DRAW_LINE_WIDTH = Sector.LINE_WIDTH * Board.SCALE;
  
  
  // Lines and corners are divided into two sets for white and black
  public static int NO_COLOR = -1;
  public static int WHITE    = 0;
  public static int BLACK    = 4;
  
  public void Sector(){
    this.data = 0;
  }

  @Override
  public int getBarcode() {
    if(BitwiseOperations.hasBit(data, startIsItABarcode)){
      return BarcodeDataNav.expand[BitwiseOperations.getBits(data, Sector.startBarcode,4)];
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
    
    if(BitwiseOperations.hasBit(data, startIsItABarcode)){
      int tempBarcodeSize = BARCODE_WIDTH/2;
      int tempSize = Sector.SIZE/2;
      switch(BitwiseOperations.getBits(data, startBarcodeDirection, 2)){
        case 1: return (x > (tempSize-tempBarcodeSize)) && (x < (tempSize+tempBarcodeSize));
        case 2: return (y > (tempSize-tempBarcodeSize)) && (y < (tempSize+tempBarcodeSize));
      }
    }
    return false;
  }

  public int getBarcodeLocation() {
    return BitwiseOperations.getBits(data, Sector.startBarcodeDirection, 2);
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
  public Sector addWall(int pos){
    data = BitwiseOperations.setBit(data, startWalls+pos);
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
    data = BitwiseOperations.setBit(data, startIsItABarcode);
    data = BitwiseOperations.setBits(data, startBarcode, 4, code);
    data = BitwiseOperations.setBits(data, startBarcodeDirection, 2, direction);
    return this;
  }
  
  public void removeWall(int pos){
    data = BitwiseOperations.unsetBit(data, pos);
  }
  
  public void removeBarcode(){
    data = BitwiseOperations.unsetBit(data, startIsItABarcode);
  }

  public int getSize(){
    return this.SIZE;
  }

  @Override
  public void drawTile(Graphics2D g2d, int left, int top) {
    g2d.setColor(Board.BROWN);
    g2d.fill(new Rectangle(DRAW_TILE_SIZE * (left - 1), DRAW_TILE_SIZE * (top - 1),
            DRAW_TILE_SIZE, DRAW_TILE_SIZE));
    //renderLinesCross(g2d, left, top);
    renderWalls(g2d, left, top);
  }

  @Override
  public int drawSize() {
    return Sector.SIZE*Board.SCALE;
  }
  
  private void renderWalls(Graphics2D g2d, int left, int top) {
    // walls are 2cm width = 4px
    g2d.setColor(Board.DARK_BROWN);
    if (hasWall(Baring.N)) {
      g2d.setColor(Board.DARK_BROWN);
    } else {
      g2d.setColor(Board.WHITE);
    }
    g2d.fill(new Rectangle(
            DRAW_TILE_SIZE * (left - 1),
            DRAW_TILE_SIZE * (top - 1),
            DRAW_TILE_SIZE,
            DRAW_WALL_LINE_WIDTH));
    if (hasWall(Baring.E)) {
      g2d.setColor(Board.DARK_BROWN);
    } else {
      g2d.setColor(Board.WHITE);
    }
    g2d.fill(new Rectangle(
            DRAW_TILE_SIZE * left - DRAW_WALL_LINE_WIDTH,
            DRAW_TILE_SIZE * (top - 1),
            4,
            DRAW_TILE_SIZE));
    if (hasWall(Baring.S)) {
      g2d.setColor(Board.DARK_BROWN);
    } else {
      g2d.setColor(Board.WHITE);
    }
    g2d.fill(new Rectangle(
            DRAW_TILE_SIZE * (left - 1),
            DRAW_TILE_SIZE * (top) - DRAW_WALL_LINE_WIDTH,
            DRAW_TILE_SIZE,
            4));
    if (hasWall(Baring.W)) {
      g2d.setColor(Board.DARK_BROWN);
    } else {
      g2d.setColor(Board.WHITE);
    }
    g2d.fill(new Rectangle(
            DRAW_TILE_SIZE * (left - 1),
            DRAW_TILE_SIZE * (top - 1),
            DRAW_WALL_LINE_WIDTH,
            DRAW_TILE_SIZE));
  }

}
