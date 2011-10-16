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
    URL resource = this.getClass().getResource("robot40.png");
    ImageIcon ii = new ImageIcon(resource);
    this.robot = ii.getImage();
  }
  
  private void setupPathTracking() {
    this.path = new ArrayList<Point>();
  }

  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;

    // render path
    for( Point point : this.path ) {
      g2d.setColor(Color.blue);
      g2d.drawLine( (int)point.getX(), (int)point.getY(), 
                    (int)point.getX(), (int)point.getY() );
    }
    
    // render robot
    int x = (int)(this.path.get(this.path.size()-1).getX());
    int y = (int)(this.path.get(this.path.size()-1).getY());
    AffineTransform affineTransform = new AffineTransform(); 
    affineTransform.setToTranslation( x-20, y-20 );
    affineTransform.rotate( -1 * Math.toRadians(this.direction), 20, 20 ); 
    g2d.drawImage( this.robot, affineTransform, this );
    
    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }

  public void updateRobot( int x, int y, int direction ) {
    this.path.add(new Point(x,y));
    this.direction = direction;
    System.out.println( x + "," + y + " / " + direction );
    this.repaint();
  }

}
