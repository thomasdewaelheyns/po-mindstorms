package penoplatinum.simulator;

/**
 * Board
 * 
 * Board is the graphics canvas where we render our SimulationEnvironment on.
 * It understand the concept of a robot at a location and with a direction.
 * 
 * Author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import java.awt.*; 
import java.awt.geom.*; 
import java.awt.event.*; 
import java.awt.image.*; 
import javax.swing.*; 

import java.net.URL;

public class Board extends JPanel {
  static final int LINE_ORIGIN           = 40;
  static final int TILE_WIDTH_AND_LENGTH = 160;
  static final int LINE_PIXEL_WIDTH      = 2;
  static final int BARCODE_PIXEL_WIDTH   = 4;

  private static Color BLACK = new Color(100,100,100);
  private static Color WHITE = new Color(200,200,200);

  private Image robot;
  private Map map;
  private int direction = 0;
  private int x = 0;
  private int y = 0;
  
  private BufferedImage background;
  private BufferedImage trail;
  
  public Board() {
    this.setupCanvas();
    this.setupImages();
  }
  
  private void setupCanvas() {
    this.setBackground(Color.WHITE);
    this.setDoubleBuffered(true);
  }
    
  private void setupImages() {
    URL resource = this.getClass().getResource("./images/robot40.png");
    ImageIcon ii = new ImageIcon(resource);
    this.robot = ii.getImage();
  }
  
  public void showMap( Map map ) {
    this.map = map;
    this.prepareBackground();
    this.prepareTracking();
  }
  
  private void prepareBackground() {
    this.background = this.createBuffer();
    Graphics2D g2d = this.background.createGraphics();
    int width  = this.map.getWidth();
    int height = this.map.getHeight();
    for( int top=1; top <= height; top++ ) {
      for( int left=1; left <= width; left++ ) {
        this.renderTile(g2d, this.map.get(left, top), left, top);
      }
    }
  }
  
  private void prepareTracking() {
    this.trail = this.createBuffer();
  }

  private BufferedImage createBuffer() {
    int w = this.map.getWidth() * TILE_WIDTH_AND_LENGTH;
    int h = this.map.getHeight() * TILE_WIDTH_AND_LENGTH;
    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
  }

  public void updateRobot( int x, int y, int direction ) {
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.extendTrail(x, y);
    this.repaint();
  }
  
  private void extendTrail(int x, int y) { 
    Graphics2D g2d = this.trail.createGraphics();
    g2d.setColor(Color.blue);
    g2d.drawLine( x, y, x, y );
  }

  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;
    
    if( this.background != null ) {
      g2d.drawImage( this.background, null, 0, 0 );
    }
    if( this.trail != null ) {
      g2d.drawImage( this.trail, null, 0, 0 );
    }
    this.renderRobot(g2d);

    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }    
  
  private void renderTile(Graphics2D g2d, Tile tile, int left, int top) {
    // background
    g2d.setColor(new Color(205,165,100));
    g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH * (left-1), 
                           TILE_WIDTH_AND_LENGTH * (top-1),
                           TILE_WIDTH_AND_LENGTH,
                           TILE_WIDTH_AND_LENGTH));
    
    this.renderLines    ( g2d, tile, left, top );
    this.renderBarcode  ( g2d, tile, left, top );
    this.renderNarrowing( g2d, tile, left, top );
    this.renderWalls    ( g2d, tile, left, top );
  }

  private void renderLines(Graphics2D g2d, Tile tile, int left, int top) {
    this.renderLine(g2d, tile, left, top, Baring.N);
    this.renderLine(g2d, tile, left, top, Baring.E);
    this.renderLine(g2d, tile, left, top, Baring.S);
    this.renderLine(g2d, tile, left, top, Baring.W);

    this.renderCorner(g2d, tile, left, top, Baring.NE);
    this.renderCorner(g2d, tile, left, top, Baring.SE);
    this.renderCorner(g2d, tile, left, top, Baring.SW);
    this.renderCorner(g2d, tile, left, top, Baring.NW);
  }
  
  // TODO: further refactor this code, the switch is still ugly as hell ;-)
  private void renderLine(Graphics2D g2d, Tile tile, int left, int top, int line) {
    if( tile.hasLine(line) ) {
      g2d.setColor(tile.hasLine(line, Tile.WHITE) ? this.WHITE : this.BLACK);
      int width = 158, offset = 0;
      if( tile.hasLine(Baring.getLeftNeighbour(line) ) ) {
        width  -= LINE_ORIGIN;
        offset += LINE_ORIGIN;
      }
      if( tile.hasLine(Baring.getRightNeighbour(line) ) ) {
        width -= LINE_ORIGIN;
      }
      int dLeft = left - 1, dTop  = top  - 1, dX = 0, dY = 0, w = 0, h = 0;
      switch(line) {
        case Baring.N:
          w = width; h=LINE_PIXEL_WIDTH;
          dX = offset; dY = LINE_ORIGIN;
          break;
        case Baring.S:
          w = width; h=LINE_PIXEL_WIDTH;
          dX = offset; dY = -1 * ( 4 + LINE_ORIGIN );
          dTop = top;          
          break;
        case Baring.E:
          w = LINE_PIXEL_WIDTH; h=width;
          dX = -1 * ( 4 + LINE_ORIGIN );  dY = offset;
          dLeft = left;
          break;
        case Baring.W:
          w = LINE_PIXEL_WIDTH; h=width;
          dX = LINE_ORIGIN; dY = offset;
      }
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH * dLeft + dX,
                             TILE_WIDTH_AND_LENGTH * dTop  + dY,
                             w,
                             h));
    }
  }

  private void renderCorner(Graphics2D g2d, Tile tile, int left, int top, int corner) {
    if( tile.hasCorner(corner) ) {
      g2d.setColor(tile.hasCorner(corner, Tile.WHITE) ? this.WHITE : this.BLACK);
      int offsetLeftH = 0, offsetTopH = 0, 
          offsetLeftV = 0, offsetTopV = 0;
      switch(corner) {
        case Baring.NE: 
          offsetLeftH = offsetLeftV = TILE_WIDTH_AND_LENGTH - LINE_ORIGIN;
          offsetTopH  = LINE_ORIGIN;
          offsetTopV  = 0;
          break;
        case Baring.SE:
          offsetLeftH = offsetLeftV = TILE_WIDTH_AND_LENGTH - LINE_ORIGIN;
          offsetTopH  = offsetTopV  = TILE_WIDTH_AND_LENGTH - LINE_ORIGIN;
          break;
        case Baring.SW:
          offsetLeftH = 0;
          offsetLeftV = LINE_ORIGIN;
          offsetTopH  = offsetTopV  = TILE_WIDTH_AND_LENGTH - LINE_ORIGIN;
          break;
        case Baring.NW:
          offsetLeftH = 0;
          offsetTopH  = LINE_ORIGIN;
          offsetLeftV = LINE_ORIGIN;
          offsetTopV  = 0;
          break;
      }
      int tileLeft = TILE_WIDTH_AND_LENGTH * ( left - 1 );
      int tileTop  = TILE_WIDTH_AND_LENGTH * ( top  - 1 );

      // horizontal
      g2d.fill(new Rectangle( tileLeft + offsetLeftH, tileTop  + offsetTopH,
                              LINE_ORIGIN, LINE_PIXEL_WIDTH ));
      // vertical
      g2d.fill(new Rectangle( tileLeft + offsetLeftV, tileTop  + offsetTopV,
                              LINE_PIXEL_WIDTH, LINE_ORIGIN ));                             
      
    }
  }

  private void renderBarcode(Graphics2D g2d, Tile tile, int left, int top) {
    // every bar of the barcode has a 2cm width = 4px
    for( int line=0; line<7; line++ ) {
      g2d.setColor( (tile.getBarcode() & (1<<line) ) != 0 ?
                    this.BLACK : this.WHITE );

      switch( tile.getBarcodeLocation() ) {
        case Baring.N:
          g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1),
                                 TILE_WIDTH_AND_LENGTH*(top-1)+4*(line),
                                 TILE_WIDTH_AND_LENGTH,
                                 BARCODE_PIXEL_WIDTH));
          break;
        case Baring.E:
          g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left)-4*(line+1),
                                 TILE_WIDTH_AND_LENGTH*(top-1),
                                 BARCODE_PIXEL_WIDTH,
                                 TILE_WIDTH_AND_LENGTH));
          break;
        case Baring.S:
          g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1),
                                 TILE_WIDTH_AND_LENGTH*(top)-4*(line+1),
                                 TILE_WIDTH_AND_LENGTH,
                                 BARCODE_PIXEL_WIDTH));
          break;
        case Baring.W:
          g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1)+4*line,
                                 TILE_WIDTH_AND_LENGTH*(top-1)+4,
                                 BARCODE_PIXEL_WIDTH,
                                 TILE_WIDTH_AND_LENGTH));
          break;
      }
    }
  }

  private void renderNarrowing(Graphics2D g2d, Tile tile, int left, int top) {
    // TODO
  }

  private void renderWalls(Graphics2D g2d, Tile tile, int left, int top) {
    // walls are 2cm width = 4px
    g2d.setColor(new Color(100,53,38));
    if( tile.hasWall(Baring.N) ) {
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1),
                             TILE_WIDTH_AND_LENGTH*(top-1),
                             TILE_WIDTH_AND_LENGTH,
                             4));
    }
    if( tile.hasWall(Baring.E) ) {
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left)-4,
                             TILE_WIDTH_AND_LENGTH*(top-1),
                             4,
                             TILE_WIDTH_AND_LENGTH));
    }
    if( tile.hasWall(Baring.S) ) {
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1),
                             TILE_WIDTH_AND_LENGTH*(top)-4,
                             TILE_WIDTH_AND_LENGTH,
                             4));
    }
    if( tile.hasWall(Baring.W) ) {
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1),
                             TILE_WIDTH_AND_LENGTH*(top-1),
                             4,
                             TILE_WIDTH_AND_LENGTH));
    }
  }

  private void renderRobot(Graphics2D g2d) { 
    // render robot
    AffineTransform affineTransform = new AffineTransform(); 
    affineTransform.setToTranslation( this.x - 20, this.y - 20 );
    affineTransform.rotate( -1 * Math.toRadians(this.direction), 20, 20 ); 
    g2d.drawImage( this.robot, affineTransform, this );
  }
  
}
