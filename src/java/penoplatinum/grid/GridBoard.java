package penoplatinum.grid;

/**
 * GridBoard
 * 
 * A JPanel extension to render a Grid.
 * 
 * @author: Team Platinum
 */

import java.awt.*; 
import java.awt.geom.*; 
import java.awt.event.*; 
import java.awt.image.*; 
import javax.swing.*; 

import penoplatinum.simulator.mini.Bearing;

public class GridBoard extends JPanel {
  private int width = 0, height = 0;

  private BufferedImage sectors, walls, values, agents;
  private Graphics2D sectorsG, wallsG, valuesG, agentsG; // TODO: improve name

  // colors on the board
  public static final Color BLACK  = new Color(0,0,0);
  public static final Color WHITE  = new Color(255,255,255);
  public static final Color YELLOW = new Color(255,255,0);
  public static final Color BROWN  = new Color(205,165,100);
  
  public static final int SECTOR_SIZE = 20;

  public GridBoard resizeTo(int width, int height) {
    this.width  = width;
    this.height = height;
    this.setupCanvas();
    return this;
  }
  
  private void setupCanvas() {
    this.setBackground(WHITE);
    this.setDoubleBuffered(true);
    this.clearSectors();
  }
  
  public void clearSectors() {
    this.sectors  = this.createBuffer();
    this.sectorsG = this.sectors.createGraphics();
    this.clearWalls();
  }
  
  public void clearWalls() {
    this.walls    = this.createBuffer();
    this.wallsG   = this.walls.createGraphics();
  }

  public void clearValues() {
    this.values  = this.createBuffer();
    this.valuesG = this.values.createGraphics();
  }

  public void clearAgents() {
    this.agents  = this.createBuffer();
    this.agentsG = this.agents.createGraphics();
  }
  
  public void addSector(int left, int top) {
    this.sectorsG.setColor(BLACK);
    this.sectorsG.fill(new Rectangle(20 * left, 20 * top, 20, 20));
  }
  
  public void addWall(int left, int top, int location) {
    Rectangle r;
    switch(location) {
      case Bearing.N: r = new Rectangle(left*20,       top*20-3,     20,  6); break;
      case Bearing.W: r = new Rectangle(left*20-3,     top*20,        6, 20); break;
      case Bearing.E: r = new Rectangle((left+1)*20-6, top*20,        6, 20); break;
      case Bearing.S: r = new Rectangle(left*20,       (top+1)*20-6, 20,  6); break;
      default:        r = new Rectangle(0,0,0,0);
    }
    this.wallsG.setColor(WHITE);
    this.wallsG.fill(r);
  }
  
  public void addValue(int left, int top, int value) {
    if( value > 0 ) {
      Color color = this.mapToHeatColor(value);
      this.valuesG.setColor(color);
      this.valuesG.fill(new Rectangle(20 * left+3, 20 * top+3, 14, 14 ));
    }
  }

  public void addAgent(int left, int top, int orientation, String name, 
                       Color color)
  {
    left *= 20;
    top  *= 20;
    this.agentsG.setColor(color);

    Polygon triangle = new Polygon();
    triangle.addPoint(left+10, top+3);
    triangle.addPoint(left+17, top+17);
    triangle.addPoint(left+3 , top+17);
    
    double angle = orientation * Math.PI/2;
    
    this.agentsG.rotate(angle, left+10, top+10);
    this.agentsG.fillPolygon(triangle);
    // reset
    this.agentsG.rotate(-1*angle, left+10, top+10);
    
    // add label
    FontMetrics fm = this.agentsG.getFontMetrics();
    this.agentsG.setColor(BLACK);
    this.agentsG.setFont(this.agentsG.getFont().deriveFont(8F));
    int w = fm.charWidth(name.charAt(0));
    int h = fm.getHeight();
    this.agentsG.drawString(name.substring(0,1), left+10-w/2, top+20-h/2);
  }
  
  private Color mapToHeatColor(int value) {
    // TODO: make this relative to a configurable maximum
    // now: 750 ... possibly needs to go up to 10000
    // Quick Fix to test:
    value /= 10;
    
    float r, g, b, v = value;
    v = (v/1000) * 400 + 350;

    if(v>=350 && v<=439) {            // violet
      r = (440-v) * 255 / 90;
      g = 0;
      b = 255;
    } else if( v>=440 && v<=489 ) {   // blue
      r = 0;
      g = (v-440) * 255 / 50;
      b = 255;
    } else if( v>=490 && v<=509 ) {   // cyan
      r = 0;
      g = 255;
      b = (510-v) * 255 / 20;
    } else if ( v>=510 && v<=579 ) {  // green
      r = (v-510) * 255 / 70;
      g = 255;
      b = 0;
    } else if( v>=580 && v<=644 ) {   // yellow
      r = 255;
      g = (645-v) * 255 / 65;
      b = 0;
    } else if( v>=645 ) {   // red
      r = 255;
      g = 0;
      b = 0;
    } else {                          // black
      r = 0;
      g = 0;
      b = 0;
    }
    return new Color((int)r,(int)g,(int)b);
  }

  public void render() {
    this.repaint();
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;

    // draw layers, bottom to top
    g2d.drawImage( this.sectors, null, 0, 0 );
    g2d.drawImage( this.values,  null, 0, 0 );
    g2d.drawImage( this.walls,   null, 0, 0 );
    g2d.drawImage( this.agents,  null, 0, 0 );
    
    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }    

  private BufferedImage createBuffer() {
    int w = this.width * 20;
    int h = this.height * 20;
    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
  }
}
