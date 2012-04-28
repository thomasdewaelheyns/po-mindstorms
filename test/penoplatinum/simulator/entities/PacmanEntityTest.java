package penoplatinum.simulator.entities;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.simulator.entities.PacmanEntity;
import penoplatinum.simulator.entities.PacmanViewRobot;
import static org.junit.Assert.*;
import penoplatinum.simulator.view.Board;
import penoplatinum.simulator.view.ViewRobot;

public class PacmanEntityTest extends TestCase {

  /**
   * Test of step method, of class PacmanEntity.
   */
  @Test
  public void testStep() {
    new PacmanEntity(12, 13, 14).step();
  }

  /**
   * Test of getViewRobot method, of class PacmanEntity.
   */
  @Test
  public void testGetViewRobot() {
    ViewRobot result = new PacmanEntity(12, 13, 14).getViewRobot();
    assertEquals(PacmanViewRobot.class, result.getClass());
    assertEquals(12 * Board.SCALE, result.getX());
  }

  /**
   * Test of getDir method, of class PacmanEntity.
   */
  @Test
  public void testGetDir() {
    PacmanEntity instance = new PacmanEntity(12, 13, 14);
    double result = instance.getDir();
    assertEquals(14, result, 0.0);
  }

  /**
   * Test of getPosX method, of class PacmanEntity.
   */
  @Test
  public void testGetPosX() {
    PacmanEntity instance = new PacmanEntity(12, 13, 14);
    double result = instance.getPosX();
    assertEquals(12, result, 0.0);
  }

  /**
   * Test of getPosY method, of class PacmanEntity.
   */
  @Test
  public void testGetPosY() {
    PacmanEntity instance = new PacmanEntity(12, 13, 14);
    double result = instance.getPosY();
    assertEquals(13, result, 0.0);
  }
}
