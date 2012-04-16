package penoplatinum.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CantorDiagonalTest {

  /**
   * Test of transform method, of class CantorDiagonal.
   */
  @Test
  public void testTransform() {
    assertEquals(0, CantorDiagonal.transform(0, 0));
    assertEquals(1, CantorDiagonal.transform(1, 0));
    assertEquals(2, CantorDiagonal.transform(1, 1));
    assertEquals(3, CantorDiagonal.transform(0, 1));
    assertEquals(4, CantorDiagonal.transform(-1, 1));
    assertEquals(5, CantorDiagonal.transform(-1, 0));
    assertEquals(6, CantorDiagonal.transform(-1, -1));
    assertEquals(7, CantorDiagonal.transform(0, -1));
    assertEquals(8, CantorDiagonal.transform(1, -1));
    assertEquals(9, CantorDiagonal.transform(2, -1));

    assertEquals(67, CantorDiagonal.transform(-4, 1));
    assertEquals(90, CantorDiagonal.transform(5, 5));
    assertEquals(120, CantorDiagonal.transform(5, -5));
  }
}
