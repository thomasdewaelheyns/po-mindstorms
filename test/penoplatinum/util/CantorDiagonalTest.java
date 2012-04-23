/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Florian
 */
public class CantorDiagonalTest extends TestCase {
  
  public CantorDiagonalTest() {
  }

  @Test 
  public void testTransform() {
   
    boolean win = true;
    win &= 0 == CantorDiagonal.transform(0, 0);
    assertTrue(win);
    win &= 1 == CantorDiagonal.transform(1, 0);
    assertTrue(win);
    win &= 2 == CantorDiagonal.transform(1, 1);
    assertTrue(win);
    win &= 3 == CantorDiagonal.transform(0, 1);
    assertTrue(win);
    win &= 4 == CantorDiagonal.transform(-1, 1);
    assertTrue(win);
    win &= 5 == CantorDiagonal.transform(-1, 0);
    assertTrue(win);
    win &= 6 == CantorDiagonal.transform(-1, -1);
    assertTrue(win);
    win &= 7 == CantorDiagonal.transform(0, -1);
    assertTrue(win);
    win &= 8 == CantorDiagonal.transform(1, -1);
    assertTrue(win);
    win &= 9 == CantorDiagonal.transform(2, -1);
    assertTrue(win);
    win &= 67 == CantorDiagonal.transform(-4, 1);
    assertTrue(win);
    win &= 90 == CantorDiagonal.transform(5,5);
    assertTrue(win);
    win &= 120 == CantorDiagonal.transform(5,-5);
       assertTrue(win);
  }


  
  @Test
  public void testTransformInverse(){
    for(int x = -10; x < 10; x++){
      for(int y = -10; y < 10; y++){
        int trans = CantorDiagonal.transform(x, y);
        assertEquals(new Point(x, y), CantorDiagonal.transform(trans));
      }
    }
    for(int i = 0; i < 400; i++){
      Point p = CantorDiagonal.transform(i);
      assertEquals(i, CantorDiagonal.transform(p.getX(), p.getY()));
    }
  }
}
