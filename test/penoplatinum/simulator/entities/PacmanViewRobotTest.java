package penoplatinum.simulator.entities;

import java.awt.Color;
import junit.framework.TestCase;
import java.awt.Graphics2D;
import org.junit.Test;
import penoplatinum.simulator.entities.PacmanEntity;
import penoplatinum.simulator.entities.PacmanViewRobot;
import penoplatinum.simulator.view.Board;
import static org.mockito.Mockito.*;

public class PacmanViewRobotTest extends TestCase {

  /**
   * Test of trackMovement method, of class PacmanViewRobot.
   */
  @Test
  public void testTrackMovement() {
    System.out.println("trackMovement");
    Graphics2D g2d = mock(Graphics2D.class);
    PacmanViewRobot instance = new PacmanViewRobot(getPacman());
    instance.trackMovement(g2d);
    verify(g2d, times(1)).drawLine(12 * Board.SCALE, 13 * Board.SCALE, 12 * Board.SCALE, 13 * Board.SCALE);
    verify(g2d, times(1)).setColor(Color.YELLOW);
  }

  /**
   * Test of getX method, of class PacmanViewRobot.
   */
  @Test
  public void testGetX() {
    PacmanViewRobot instance = new PacmanViewRobot(getPacman());
    assertEquals(12 * Board.SCALE, instance.getX());
  }

  /**
   * Test of getY method, of class PacmanViewRobot.
   */
  @Test
  public void testGetY() {
    PacmanViewRobot instance = new PacmanViewRobot(getPacman());
    assertEquals(13 * Board.SCALE, instance.getY());
  }

  /**
   * Test of getDirection method, of class PacmanViewRobot.
   */
  @Test
  public void testGetDirection() {
    PacmanViewRobot instance = new PacmanViewRobot(getPacman());
    assertEquals(14, instance.getDirection());
  }

  private PacmanEntity getPacman() {
    PacmanEntity mock = mock(PacmanEntity.class);
    when(mock.getPosX()).thenReturn(12.0);
    when(mock.getPosY()).thenReturn(13.0);
    when(mock.getDir()).thenReturn(14.0);
    return mock;
  }
}
