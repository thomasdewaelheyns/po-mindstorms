/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

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
public class CantorDiagonalTest {
  
  public CantorDiagonalTest() {
  }

  @Test 
  public void transformTest() {
   
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


}
