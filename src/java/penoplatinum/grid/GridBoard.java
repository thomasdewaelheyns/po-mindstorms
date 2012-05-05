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

import penoplatinum.util.Bearing;
import penoplatinum.util.Position;
import penoplatinum.util.Point;


public class GridBoard extends JPanel {
  private int width = 0, height = 0;
  private BufferedImage sectors, walls, values, agents, barcodes;
  private Graphics2D sectorsG, wallsG, valuesG, agentsG, barcodesG;

  // colors on the board
  public static final Color BLACK = new Color(0, 0, 0);
  public static final Color WHITE = new Color(255, 255, 255);
  public static final Color YELLOW = new Color(255, 255, 0);
  public static final Color BROWN = new Color(205, 165, 100);
  
  private int sectorSize = 40;
  
  private double ratio = 1.0;

  
  public GridBoard resizeTo(int width, int height) {
    this.width = width;
    this.height = height;
    this.setupCanvas();
    return this;
  }
  
  public GridBoard setSectorSize(int size) {
    this.sectorSize = size;
    return this;
  }
  
  public int getSectorSize() {
    return this.sectorSize;
  }
  
  private void setupCanvas() {
    this.setBackground(BROWN);
    this.setDoubleBuffered(true);
    this.clearSectors();
  }
  
  public void clearSectors() {
    this.sectors = this.createBuffer();
    this.sectorsG = this.sectors.createGraphics();
    this.clearWalls();
  }
  
  public void clearWalls() {
    this.walls = this.createBuffer();
    this.wallsG = this.walls.createGraphics();
  }
  
  public void clearValues() {
    this.values = this.createBuffer();
    this.valuesG = this.values.createGraphics();
  }
  
  public void clearAgents() {
    this.agents = this.createBuffer();
    this.agentsG = this.agents.createGraphics();
  }
  
  public void clearBarcodes() {
    this.barcodes = this.createBuffer();
    this.barcodesG = this.barcodes.createGraphics();
  }
  
  public void addSector(Point position) {
    this.sectorsG.setColor(BLACK);
    this.sectorsG.fill(new Rectangle(this.sectorSize * position.getX(),
                                     this.sectorSize * position.getY(),
                                     this.sectorSize, this.sectorSize));
  }
  
  public void addWall(Point position, Bearing location) {
    this.wallsG.setColor(Color.WHITE);
    this.wallsG.fill(this.createWall(position, location));
  }

  public void addUnknownWall(Point position, Bearing location) {
    this.wallsG.setColor(Color.RED);
    this.wallsG.fill(this.createWall(position, location));
  }

  private Rectangle createWall(Point position, Bearing location) {
    switch (location) {
      case N: return new Rectangle(position.getX() * this.sectorSize, 
                                   position.getY() * this.sectorSize - 3,
                                   this.sectorSize, 6);
      case W: return new Rectangle(position.getX() * this.sectorSize - 3,
                                   position.getY() * this.sectorSize,
                                   6, this.sectorSize);
      case E: return new Rectangle((position.getX() + 1) * this.sectorSize - 6,
                                    position.getY() * this.sectorSize,
                                    6, this.sectorSize);
      case S: return new Rectangle(position.getX() * this.sectorSize,
                                  (position.getY() + 1) * this.sectorSize - 6,
                                  this.sectorSize, 6);
    }
    throw new RuntimeException( "Can't create wall at bearing = " + location );
  }
  
  public void addValue(Point position, int value) {
    if (value > 0) {
      Color color = this.mapToHeatColor(value);
      this.valuesG.setColor(color);
      this.valuesG.fill(new Rectangle(this.sectorSize * position.getX() + 3, 
                                      this.sectorSize * position.getY() + 3,
                                      this.sectorSize - 6, this.sectorSize - 6));
    }
  }
  
  public void addAgent(Point position, Bearing orientation, String name,
                       Color color)
  {
    int left = position.getX() * this.sectorSize,
        top  = position.getY() * this.sectorSize;
    this.agentsG.setColor(color);
    
    Polygon triangle = new Polygon();
    triangle.addPoint(left + this.sectorSize / 2, top + 3);
    triangle.addPoint(left + this.sectorSize - 3, top + this.sectorSize - 3);
    triangle.addPoint(left + 3, top + this.sectorSize - 3);
    double angle = 0;
    switch (orientation) {
      case N: angle = 0;                 break;
      case E: angle = Math.PI / 2;       break;
      case S: angle = Math.PI;           break;
      case W: angle = Math.PI * (3 / 2); break;
    }
    
    this.agentsG.rotate(angle, left + this.sectorSize / 2, top + this.sectorSize / 2);
    this.agentsG.fillPolygon(triangle);
    // reset
    this.agentsG.rotate(-1 * angle, left + this.sectorSize / 2, top + this.sectorSize / 2);

    // add label
    FontMetrics fm = this.agentsG.getFontMetrics();
    this.agentsG.setColor(BLACK);
    this.agentsG.setFont(this.agentsG.getFont().deriveFont(12F));
    int w = fm.charWidth(name.charAt(0));
    int h = fm.getHeight();
    this.agentsG.drawString(name.substring(0, 1), left + this.sectorSize / 2 - w / 2, top + this.sectorSize - h / 2);
  }
  
  public void addBarcode(Point position, Bearing orientation, int code) {
    int left = position.getX() * this.sectorSize,
        top  = position.getY() * this.sectorSize;
    double angle = 0;
    
    switch (orientation) {
      case N:
        angle = 0;
        break;
      case E:
        angle = Math.PI / 2;
        break;
      case S:
        angle = Math.PI;
        break;
      case W:
        angle = Math.PI * (3f / 2);
        break;
    }    
    this.agentsG.rotate(angle, left + this.sectorSize / 2, top + this.sectorSize / 2);
    FontMetrics fm = this.agentsG.getFontMetrics();
    this.agentsG.setColor(WHITE);
    this.agentsG.setFont(this.agentsG.getFont().deriveFont(12F));
    int w = fm.stringWidth(Integer.toString(code));
    int h = fm.getHeight();
    this.agentsG.drawString(Integer.toString(code), left + this.sectorSize / 2 - w / 2, top + this.sectorSize - h / 2);
    this.agentsG.rotate(-1 * angle, left + this.sectorSize / 2, top + this.sectorSize / 2);
  }
  
  private Color mapToHeatColor(int value) {
    // TODO: make this relative to a configurable maximum
    // now: 750 ... possibly needs to go up to 10000
    // Quick Fix to test:
    value /= 10;
    
    float r, g, b, v = value;
    v = (v / 1000) * 400 + 350;
    
    if (v >= 350 && v <= 439) {            // violet
      r = (440 - v) * 255 / 90;
      g = 0;
      b = 255;
    } else if (v >= 440 && v <= 489) {   // blue
      r = 0;
      g = (v - 440) * 255 / 50;
      b = 255;
    } else if (v >= 490 && v <= 509) {   // cyan
      r = 0;
      g = 255;
      b = (510 - v) * 255 / 20;
    } else if (v >= 510 && v <= 579) {  // green
      r = (v - 510) * 255 / 70;
      g = 255;
      b = 0;
    } else if (v >= 580 && v <= 644) {   // yellow
      r = 255;
      g = (645 - v) * 255 / 65;
      b = 0;
    } else if (v >= 645) {   // red
      r = 255;
      g = 0;
      b = 0;
    } else {                          // black
      r = 0;
      g = 0;
      b = 0;
    }
    return new Color((int) r, (int) g, (int) b);
  }
  
  public void render() {
    this.repaint();
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.scale(this.ratio, this.ratio);
    
    
    this.wallsG.setColor(BLACK);
    this.wallsG.drawRect(0, 0, width * this.sectorSize, height * this.sectorSize);

    // draw layers, bottom to top
    g2d.drawImage(this.sectors, null, 0, 0);
    g2d.drawImage(this.values, null, 0, 0);
    g2d.drawImage(this.walls, null, 0, 0);
    g2d.drawImage(this.barcodes, null, 0, 0);
    g2d.drawImage(this.agents, null, 0, 0);
    
    
    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }
  
  private BufferedImage createBuffer() {
    int w = this.width * this.sectorSize;
    int h = this.height * this.sectorSize;
    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
  }
  
  void addOrigin(int minLeft, int minTop) {
    sectorsG.setColor(Color.WHITE);
    sectorsG.setStroke(new java.awt.BasicStroke(2));
    sectorsG.drawRect(minLeft * this.sectorSize + 1, minTop * this.sectorSize + 1, this.sectorSize - 1, this.sectorSize - 1);
  }
  
  public void setRatio(double d) {
    if (d > 1.0) {
      d = 1.0;
    }
    this.ratio = d;
  }
  
  public void calculateRatio(int windowWidth, int windowHeight) {
    this.setRatio(2.0);
    double availableWidth = (((double) windowWidth - 10.0) / 2.0) / 2.0;
    double availableHeight = (((double) windowHeight - 10.0) / 2.0);
    double availableSectorWidth = availableWidth / (double) this.width;
    double availableSectorHeight = availableHeight / (double) this.height;
    double widthRatio = availableSectorWidth / (double) this.sectorSize;
    double heightRatio = availableSectorHeight / (double) this.sectorSize;
    if (widthRatio > heightRatio) {
      this.setRatio(heightRatio);
    } else {
      this.setRatio(widthRatio);
    }
  }
}
