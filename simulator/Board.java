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
import javax.swing.*; 

import java.net.URL;

public class Board extends JPanel {

  private Image robot;
  private Map map;
  private List<Point> path;
  private int direction = 0;
  
  public Board() {
    this.setupCanvas();
    this.setupImages();
    this.setupPathTracking();
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
  
  private void setupPathTracking() {
    this.path = new ArrayList<Point>();
  }

  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;
    
    this.renderMap(g2d);
    this.renderPath(g2d);
    this.renderRobot(g2d);

    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }    
  
  private void renderMap(Graphics2D g2d) { 
    int width  = this.map.getWidth();
    int height = this.map.getHeight();
    for( int top=1; top <= height; top++ ) {
      for( int left=1; left <= width; left++ ) {
        this.renderTile(g2d, this.map.get(left, top), left, top);
      }
    }
  }
  
  private void renderTile(Graphics2D g2d, Tile tile, int left, int top ) {
    // tile = 80 cm, scale = 2px/cm
    // background
    g2d.setColor(new Color(205,165,100));
    g2d.fill(new Rectangle(160*(left-1), 160*(top-1), 160, 160));
    
    // walls
    // walls are 2cm width = 4px
    g2d.setColor(new Color(100,53,38));
    if( tile.hasWall(Tile.N) ) {
      g2d.fill(new Rectangle(160*(left-1), 160*(top-1), 160, 4));
    }
    if( tile.hasWall(Tile.E) ) {
      g2d.fill(new Rectangle(160*(left)-4, 160*(top-1), 4, 160));
    }
    if( tile.hasWall(Tile.S) ) {
      g2d.fill(new Rectangle(160*(left-1), 160*(top)-4, 160, 4));
    }
    if( tile.hasWall(Tile.W) ) {
      g2d.fill(new Rectangle(160*(left-1), 160*(top-1), 4, 160));
    }
    
    // lines
    // TODO
    
    // barcode
    // TODO
    
    // narrowing
    // TODO
  }

  private void renderPath(Graphics2D g2d) { 
    // render path
    synchronized(this) {
      for( Point point : this.path ) {
        g2d.setColor(Color.blue);
        g2d.drawLine( (int)point.getX(), (int)point.getY(), 
                      (int)point.getX(), (int)point.getY() );
      }
    }
  }

  private void renderRobot(Graphics2D g2d) { 
    // render robot
    int x = (int)(this.path.get(this.path.size()-1).getX());
    int y = (int)(this.path.get(this.path.size()-1).getY());
    AffineTransform affineTransform = new AffineTransform(); 
    affineTransform.setToTranslation( x-20, y-20 );
    affineTransform.rotate( -1 * Math.toRadians(this.direction), 20, 20 ); 
    g2d.drawImage( this.robot, affineTransform, this );
  }
  
  public void showMap( Map map ) {
    this.map = map;
  }

  public void updateRobot( int x, int y, int direction ) {
    synchronized(this) {
      this.path.add(new Point(x,y));
    }
    this.direction = direction;
    //System.err.println( "Robot @ " + x + "," + y + " / " + direction );
    this.repaint();
  }

}
