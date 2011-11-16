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
    // tile = 80 cm, scale = 2px/cm
    // background
    g2d.setColor(new Color(205,165,100));
    g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1), 
                           TILE_WIDTH_AND_LENGTH*(top-1),
                           TILE_WIDTH_AND_LENGTH,
                           TILE_WIDTH_AND_LENGTH));
    
    this.renderLines    ( g2d, tile, left, top );
    this.renderBarcode  ( g2d, tile, left, top );
    this.renderNarrowing( g2d, tile, left, top );
    this.renderWalls    ( g2d, tile, left, top );
  }

  // TODO: refactor this method, it's way to long and it feels like it 
  //       contains repetitive code
  private void renderLines(Graphics2D g2d, Tile tile, int left, int top) {
    // lines are 1cm wide = 2px
    // lines are 20cm out of the walls = 40px
    int width, offset;
    if( tile.hasLine(Baring.N) ) {
      g2d.setColor(tile.hasLine(Baring.N, Tile.WHITE) ? 
                   this.WHITE : this.BLACK);
      width = 158; offset = 0;
      if( tile.hasLine(Baring.W) ) { 
        width  -= LINE_ORIGIN;
        offset += LINE_ORIGIN;
      }
      if( tile.hasLine(Baring.E) ) {
        width -= LINE_ORIGIN;
      }
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1)+offset,
                             TILE_WIDTH_AND_LENGTH*(top-1)+LINE_ORIGIN,
                             width,
                             LINE_PIXEL_WIDTH));
    }
    if( tile.hasLine(Baring.E) ) {
      g2d.setColor(tile.hasLine(Baring.E, Tile.WHITE) ? 
                   this.WHITE : this.BLACK);
      width = 158; offset = 0;
      if( tile.hasLine(Baring.N) ) {
        width  -= LINE_ORIGIN;
        offset += LINE_ORIGIN;
      }
      if( tile.hasLine(Baring.S) ) {
        width -= LINE_ORIGIN;
      }
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left)-4 -LINE_ORIGIN,
                             TILE_WIDTH_AND_LENGTH*(top-1)+offset,
                             LINE_PIXEL_WIDTH,
                             width));
    }
    if( tile.hasLine(Baring.S) ) {
      g2d.setColor(tile.hasLine(Baring.S, Tile.WHITE) ? 
                   this.WHITE : this.BLACK);
      width = 158; offset = 0;
      if( tile.hasLine(Baring.W) ) {
        width  -= LINE_ORIGIN;
        offset += LINE_ORIGIN; }
      if( tile.hasLine(Baring.E) ) {
        width -= LINE_ORIGIN;
      }
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1)+offset,
                             TILE_WIDTH_AND_LENGTH*(top)-4 -LINE_ORIGIN,
                             width,
                             LINE_PIXEL_WIDTH));
    }
    if( tile.hasLine(Baring.W) ) {
      g2d.setColor(tile.hasLine(Baring.W, Tile.WHITE) ? 
                   this.WHITE : this.BLACK);
      width = 158; offset = 0;
      if( tile.hasLine(Baring.N) ) {
        width -= LINE_ORIGIN;
        offset += LINE_ORIGIN;
      }
      if( tile.hasLine(Baring.S) ) {
        width -= LINE_ORIGIN;
      }
      g2d.fill(new Rectangle(TILE_WIDTH_AND_LENGTH*(left-1)+LINE_ORIGIN,
                             TILE_WIDTH_AND_LENGTH*(top-1)+offset,
                             LINE_PIXEL_WIDTH,
                             width));
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
