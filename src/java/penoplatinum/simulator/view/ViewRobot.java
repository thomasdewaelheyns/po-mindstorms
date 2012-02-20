package penoplatinum.simulator.view;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import penoplatinum.simulator.SimulatedEntity;

/**
 *
 * @author MHGameWork
 */
public interface ViewRobot {

  void trackMovement(Graphics2D g2d);

  void renderRobot(Graphics2D g2d, ImageObserver board);

  void renderSonar(Graphics2D g2d);

  int getX();

  int getY();

  int getDirection();
}