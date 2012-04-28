package penoplatinum.util;

import junit.framework.TestCase;
import org.junit.Test;

public class PositionTest extends TestCase {

  /**
   * Test of moveLeft method, of class Position.
   */
  @Test
  public void testMoveLeft() {
    assertEquals(123, Position.moveLeft(Bearing.N, 123));
    assertEquals(124, Position.moveLeft(Bearing.E, 123));
    assertEquals(123, Position.moveLeft(Bearing.S, 123));
    assertEquals(122, Position.moveLeft(Bearing.W, 123));
    
    assertEquals(124, Position.moveLeft(Bearing.NE, 123));
    assertEquals(124, Position.moveLeft(Bearing.SE, 123));
    assertEquals(122, Position.moveLeft(Bearing.SW, 123));
    assertEquals(122, Position.moveLeft(Bearing.NW, 123));
  }

  /**
   * Test of moveTop method, of class Position.
   */
  @Test
  public void testMoveTop() {
    assertEquals(122, Position.moveTop(Bearing.N, 123));
    assertEquals(123, Position.moveTop(Bearing.E, 123));
    assertEquals(124, Position.moveTop(Bearing.S, 123));
    assertEquals(123, Position.moveTop(Bearing.W, 123));
    
    assertEquals(122, Position.moveTop(Bearing.NE, 123));
    assertEquals(124, Position.moveTop(Bearing.SE, 123));
    assertEquals(124, Position.moveTop(Bearing.SW, 123));
    assertEquals(122, Position.moveTop(Bearing.NW, 123));
  }
}
