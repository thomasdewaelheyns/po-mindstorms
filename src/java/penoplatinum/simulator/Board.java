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
    int w = this.map.getWidth() * 160;
    int h = this.map.getHeight() * 160;
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
  
  private void renderTile(Graphics2D g2d, Tile tile, int left, int top ) {
    // tile = 80 cm, scale = 2px/cm
    // background
    g2d.setColor(new Color(205,165,100));
    g2d.fill(new Rectangle(160*(left-1), 160*(top-1), 160, 160));
    
    // walls
    // walls are 2cm width = 4px
    g2d.setColor(new Color(100,53,38));
    if( tile.hasWall(Baring.N) ) {
      g2d.fill(new Rectangle(160*(left-1), 160*(top-1), 160, 4));
    }
    if( tile.hasWall(Baring.E) ) {
      g2d.fill(new Rectangle(160*(left)-4, 160*(top-1), 4, 160));
    }
    if( tile.hasWall(Baring.S) ) {
      g2d.fill(new Rectangle(160*(left-1), 160*(top)-4, 160, 4));
    }
    if( tile.hasWall(Baring.W) ) {
      g2d.fill(new Rectangle(160*(left-1), 160*(top-1), 4, 160));
    }
    
    // lines
    // TODO
    
    // barcode
    // TODO
    
    // narrowing
    // TODO
  }

  private void renderRobot(Graphics2D g2d) { 
    // render robot
    AffineTransform affineTransform = new AffineTransform(); 
    affineTransform.setToTranslation( this.x - 20, this.y - 20 );
    affineTransform.rotate( -1 * Math.toRadians(this.direction), 20, 20 ); 
    g2d.drawImage( this.robot, affineTransform, this );
  }
  
}
