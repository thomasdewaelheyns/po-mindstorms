package penoplatinum.simulator.view;

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
import penoplatinum.map.Map;
import penoplatinum.simulator.RemoteViewRobot;
import penoplatinum.simulator.SimulatedViewRobot;
import penoplatinum.simulator.tiles.Tile;

public class Board extends JPanel {
  // Tiles are defined in logical dimensions, comparable to cm in reality

  public static final int SCALE = 2;
  // colors on the board
  public static final Color BLACK = new Color(100, 100, 100);
  public static final Color WHITE = new Color(200, 200, 200);
  public static final Color BROWN = new Color(205, 165, 100);
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
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
  }

  private void setupImages() {
    URL resource = this.getClass().getResource("../images/robot40.png");
    ImageIcon ii = new ImageIcon(resource);
    SimulatedViewRobot.robot = ii.getImage();
  }

  public void showMap(Map map) {
    this.map = map;
    this.prepareBackground();
    this.prepareTracking();
  }

  private void prepareBackground() {
    this.background = this.createBuffer();
    Graphics2D g2d = this.background.createGraphics();
    int width = this.map.getWidth();
    int height = this.map.getHeight();
    for (int top = 1; top <= height; top++) {
      for (int left = 1; left <= width; left++) {
        Tile t = this.map.get(left, top);
        if (t == null) {
          // No tile set, keep black
        } else {
          t.drawTile(g2d, left, top);
        }
      }
    }
  }

  private void prepareTracking() {
    this.trail = this.createBuffer();
  }

  private BufferedImage createBuffer() {

    int drawSize = 10; // Magic!


    if (map.getTileCount() != 0) {
      drawSize = this.map.getFirst().drawSize();
    }

    int w = this.map.getWidth() * drawSize;
    int h = this.map.getHeight() * drawSize;
    
    if (h == 0) h = w; // cheat
    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
  }

  public Board addRobot(ViewRobot robot) {
    robots.add(robot);
    return this;
  }

  public void updateRobots() {
    for (ViewRobot robot : robots) {
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
    Graphics2D g2d = (Graphics2D) g;

    if (this.background != null) {
      g2d.drawImage(this.background, null, 0, 0);
    }
    if (this.trail != null) {
      g2d.drawImage(this.trail, null, 0, 0);
    }

    for (ViewRobot robot : robots) {
      robot.renderRobot(g2d, this);
      robot.renderSonar(g2d);
    }
    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }
}
