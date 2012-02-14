package penoplatinum.simulator;

/**
 * Board
 * 
 * Board is the graphics canvas where we render our SimulationEnvironment on.
 * It understand the concept of a robot at a location and with a direction.
 * 
 * @author: Team Platinum
 */

import java.util.List;

import java.awt.*; 
import java.awt.image.*; 
import javax.swing.*; 

import java.net.URL;
import java.util.ArrayList;

public class Board extends JPanel {
  // Tiles are defined in logical dimensions, comparable to cm in reality
  public static final int SCALE = 2;
  
  // sizes in pixel format
  public static final int LINE_OFFSET        = (Tile.LINE_OFFSET * Board.SCALE);
  public static final int TILE_SIZE          = Tile.SIZE        * Board.SCALE;
  public static final int LINE_OFFSET2        = (Tile.LINE_OFFSET * Board.SCALE)+2;
  public static final int LINE_WIDTH         = Tile.LINE_WIDTH  * Board.SCALE;
  public static final int BARCODE_LINE_WIDTH = Tile.BARCODE_LINE_WIDTH * Board.SCALE;

  // colors on the board
  public static final Color BLACK      = new Color(100,100,100);
  public static final Color WHITE      = new Color(200,200,200);
  public static final Color BROWN      = new Color(205,165,100);
  public static final Color DARK_BROWN = new Color(100, 53, 38);

  // a Map of Tiles
  private Map map;

  private BufferedImage background;
  private BufferedImage trail;
  
  private List<ViewRobot> robots = new ArrayList<ViewRobot>();
  
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
    ViewRobot.robot = ii.getImage();
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
    int w = this.map.getWidth() * TILE_SIZE;
    int h = this.map.getHeight() * TILE_SIZE;
    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
  }
  
  public Board addRobot(ViewRobot robot){
    robots.add(robot);
    return this;
  }

  public void updateRobots() {
    for(ViewRobot robot:robots){
      this.trackMovement(robot);
    }
    this.repaint();
  }
  
  private void trackMovement(ViewRobot robot) {
    Graphics2D g2d = this.trail.createGraphics();
    robot.trackMovement(g2d);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;
    
    if( this.background != null ) {
      g2d.drawImage( this.background, null, 0, 0 );
    }
    if( this.trail != null ) {
      g2d.drawImage( this.trail, null, 0, 0 );
    }
    
    for(ViewRobot robot : robots){
      robot.renderRobot(g2d, this);
      robot.renderSonar(g2d);
    }
    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }    
  
  private void renderTile(Graphics2D g2d, Tile tile, int left, int top) {
    // background
    g2d.setColor(Board.BROWN);
    g2d.fill(new Rectangle(TILE_SIZE * (left-1), TILE_SIZE * (top-1),
                           TILE_SIZE, TILE_SIZE));
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
    if (tile.hasLine(line)) {
      g2d.setColor(tile.hasLine(line, Tile.WHITE) ? this.WHITE : this.BLACK);
      int length = TILE_SIZE, offset = 0;
      if (tile.hasLine(Baring.getLeftNeighbour(line)) || tile.hasLine(Baring.getRightNeighbour(line))) {
        length -= LINE_OFFSET;
      }
      if (tile.hasLine(Baring.getLeftNeighbour(line))) {
        offset += LINE_OFFSET;
      }
      int x = 0, y = 0, dX = 0, dY = 0;
      switch(line) {
        case Baring.N:
          dX = length; dY = LINE_WIDTH;
          x = offset; y = LINE_OFFSET;
          break;
        case Baring.S:
          dX = length; dY = LINE_WIDTH;
          x = offset; y = TILE_SIZE - LINE_OFFSET - LINE_WIDTH;
          break;
        case Baring.E:
          dX = LINE_WIDTH; dY=length;
          x = TILE_SIZE- LINE_OFFSET - LINE_WIDTH;  y = offset;
          break;
        case Baring.W:
          dX = LINE_WIDTH; dY=length;
          x = LINE_OFFSET; y = offset;
      }
      
      int dLeft = left - 1, dTop  = top  - 1;
      g2d.fill(new Rectangle(TILE_SIZE * dLeft + x,
                             TILE_SIZE * dTop  + y,
                             dX,
                             dY));
    }
  }

  private void renderCorner(Graphics2D g2d, Tile tile, int left, int top, int corner) {
    if( tile.hasCorner(corner) ) {
      g2d.setColor( tile.hasCorner(corner, Tile.WHITE) ? 
                      this.WHITE : this.BLACK );
      int offsetLeftH = 0, offsetTopH = 0, 
          offsetLeftV = 0, offsetTopV = 0;
      switch(corner) {
        case Baring.NE: 
          offsetLeftH = offsetLeftV = TILE_SIZE - LINE_OFFSET - LINE_WIDTH;
          offsetTopH  = LINE_OFFSET;  offsetTopV  = 0;
          break;
        case Baring.SE:
          offsetLeftH = offsetLeftV = TILE_SIZE - LINE_OFFSET - LINE_WIDTH;
          offsetTopH  = offsetTopV  = TILE_SIZE - LINE_OFFSET - LINE_WIDTH;
          break;
        case Baring.SW:
          offsetLeftH = 0;  offsetLeftV = LINE_OFFSET;
          offsetTopH  = TILE_SIZE - LINE_OFFSET - LINE_WIDTH;
          offsetTopV = TILE_SIZE - LINE_OFFSET - LINE_WIDTH;
          break;
        case Baring.NW:
          offsetLeftH = 0; offsetLeftV = LINE_OFFSET;
          offsetTopV  = 0; offsetTopH  = LINE_OFFSET;
          break;
      }
      int tileLeft = TILE_SIZE * ( left - 1 );
      int tileTop  = TILE_SIZE * ( top  - 1 );

      // horizontal
      g2d.fill(new Rectangle( tileLeft + offsetLeftH, tileTop  + offsetTopH,
                              LINE_OFFSET+LINE_WIDTH, LINE_WIDTH ));
      // vertical
      g2d.fill(new Rectangle( tileLeft + offsetLeftV, tileTop  + offsetTopV,
                              LINE_WIDTH, LINE_OFFSET+LINE_WIDTH));                             
      
    }
  }

  private void renderBarcode(Graphics2D g2d, Tile tile, int left, int top) {
    // every bar of the barcode has a 2cm width = 4px
    for( int line=0; line<7; line++ ) {
      g2d.setColor(tile.getBarcodeLine(line)==Tile.BLACK ? this.BLACK : this.WHITE);

      switch( tile.getBarcodeLocation() ) {
        case Baring.N:
          g2d.fill(new Rectangle(TILE_SIZE*(left-1),
                                 TILE_SIZE*(top-1)+4*(line),
                                 TILE_SIZE,
                                 BARCODE_LINE_WIDTH));
          break;
        case Baring.E:
          g2d.fill(new Rectangle(TILE_SIZE*(left)-4*(line+1),
                                 TILE_SIZE*(top-1),
                                 BARCODE_LINE_WIDTH,
                                 TILE_SIZE));
          break;
        case Baring.S:
          g2d.fill(new Rectangle(TILE_SIZE*(left-1),
                                 TILE_SIZE*(top)-4*(line+1),
                                 TILE_SIZE,
                                 BARCODE_LINE_WIDTH));
          break;
        case Baring.W:
          g2d.fill(new Rectangle(TILE_SIZE*(left-1)+4*line,
                                 TILE_SIZE*(top-1)+4,
                                 BARCODE_LINE_WIDTH,
                                 TILE_SIZE));
          break;
      }
    }
  }

  private void renderNarrowing(Graphics2D g2d, Tile tile, int left, int top) {
    // TODO
  }

  private void renderWalls(Graphics2D g2d, Tile tile, int left, int top) {
    // walls are 2cm width = 4px
    g2d.setColor(Board.DARK_BROWN);
    if( tile.hasWall(Baring.N) ) {
      g2d.fill(new Rectangle(TILE_SIZE*(left-1),
                             TILE_SIZE*(top-1),
                             TILE_SIZE,
                             4));
    }
    if( tile.hasWall(Baring.E) ) {
      g2d.fill(new Rectangle(TILE_SIZE*(left)-4,
                             TILE_SIZE*(top-1),
                             4,
                             TILE_SIZE));
    }
    if( tile.hasWall(Baring.S) ) {
      g2d.fill(new Rectangle(TILE_SIZE*(left-1),
                             TILE_SIZE*(top)-4,
                             TILE_SIZE,
                             4));
    }
    if( tile.hasWall(Baring.W) ) {
      g2d.fill(new Rectangle(TILE_SIZE*(left-1),
                             TILE_SIZE*(top-1),
                             4,
                             TILE_SIZE));
    }
  }

}
