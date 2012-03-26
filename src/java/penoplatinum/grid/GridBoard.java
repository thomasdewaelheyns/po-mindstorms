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

import penoplatinum.simulator.Bearing;

public class GridBoard extends JPanel {

  private int width = 0, height = 0;
  private BufferedImage sectors, walls, values, agents, barcodes;
  private Graphics2D sectorsG, wallsG, valuesG, agentsG, barcodesG; // TODO: improve name
  // colors on the board
  public static final Color BLACK = new Color(0, 0, 0);
  public static final Color WHITE = new Color(255, 255, 255);
  public static final Color YELLOW = new Color(255, 255, 0);
  public static final Color BROWN = new Color(205, 165, 100);
  private int sectorSize = 40;

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

  public void addSector(int left, int top) {
    this.sectorsG.setColor(BLACK);
    this.sectorsG.fill(new Rectangle(this.sectorSize * left,
            this.sectorSize * top,
            this.sectorSize, this.sectorSize));
  }

  public void addWall(int left, int top, int location) {
    Rectangle r;
    switch (location) {
      case Bearing.N:
        r = new Rectangle(left * this.sectorSize, top * this.sectorSize - 3, this.sectorSize, 6);
        break;
      case Bearing.W:
        r = new Rectangle(left * this.sectorSize - 3, top * this.sectorSize, 6, this.sectorSize);
        break;
      case Bearing.E:
        r = new Rectangle((left + 1) * this.sectorSize - 6, top * this.sectorSize, 6, this.sectorSize);
        break;
      case Bearing.S:
        r = new Rectangle(left * this.sectorSize, (top + 1) * this.sectorSize - 6, this.sectorSize, 6);
        break;
      default:
        r = new Rectangle(0, 0, 0, 0);
    }
    this.wallsG.setColor(WHITE);
    this.wallsG.fill(r);
  }

  public void addValue(int left, int top, int value) {
    if (value > 0) {
      Color color = this.mapToHeatColor(value);
      this.valuesG.setColor(color);
      this.valuesG.fill(new Rectangle(this.sectorSize * left + 3, this.sectorSize * top + 3, this.sectorSize - 6, this.sectorSize - 6));
    }
  }

  public void addAgent(int left, int top, int orientation, String name,
          Color color) {
    left *= this.sectorSize;
    top *= this.sectorSize;
    this.agentsG.setColor(color);

    Polygon triangle = new Polygon();
    triangle.addPoint(left + this.sectorSize / 2, top + 3);
    triangle.addPoint(left + this.sectorSize - 3, top + this.sectorSize - 3);
    triangle.addPoint(left + 3, top + this.sectorSize - 3);

    double angle = orientation * Math.PI / 2;

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

  public void addBarcode(int left, int top, int orientation, int code) {
    left *= this.sectorSize;
    top *= this.sectorSize;

    // add label
    FontMetrics fm = this.agentsG.getFontMetrics();
    this.barcodesG.setColor(WHITE);
    this.barcodesG.setFont(this.agentsG.getFont().deriveFont(12F));
    int w = fm.stringWidth(Integer.toString(code));
    int h = fm.getHeight();
    this.barcodesG.drawString(Integer.toString(code), left + this.sectorSize / 2 - w / 2, top + this.sectorSize - h / 2);
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
    sectorsG.drawRect(minLeft*this.sectorSize +1, minTop*this.sectorSize+1, this.sectorSize-1, this.sectorSize-1);
  }
}
