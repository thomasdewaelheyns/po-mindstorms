package penoplatinum.simulator.view;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

/**
 * ViewRobot
 * 
 * Defines interface for simulated entities to provide an object that renders
 * a robot on a Graphics 2D interface.
 * 
 * @author Team Platinum
 */

public interface ViewRobot {
  public void trackMovement(Graphics2D g2d);

  void renderRobot(Graphics2D g2d, ImageObserver board);

  void renderSonar(Graphics2D g2d);

  int getX();

  int getY();

  int getDirection();
}
