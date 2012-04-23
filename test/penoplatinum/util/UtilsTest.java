/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * TODO: maybe split
 * @author Thomas
 */
@RunWith(JUnit4.class)
public class UtilsTest extends TestCase {

  public UtilsTest(String name) {
    super(name);
  }
  @Rule
  public ExpectedException exception = ExpectedException.none();

  /**
   * Test of ClampLooped method, of class Utils.
   */
  @Test
  public void testClampLooped_3args_1() {
    assertEquals(30, Utils.ClampLooped(130, 10, 110), 0.05);
    assertEquals(80, Utils.ClampLooped(-20, 10, 110), 0.05);
    assertEquals(100, Utils.ClampLooped(100, 10, 110), 0.05);
    Utils.ClampLooped(-1, 10, 110);
    assertEquals(-1, Utils.ClampLooped(-1, -10, 110), 0.05);
    assertEquals(-80, Utils.ClampLooped(130, -100, 110), 0.05);
    assertEquals(100, Utils.ClampLooped(120, 100, 110), 0.05);
    assertEquals(100, Utils.ClampLooped(500, 10, 110), 0.05);
    assertEquals(110 - 11.5, Utils.ClampLooped(-1.5, 10, 110), 0.05);
    assertEquals(110 - 9.2, Utils.ClampLooped(0.8, 10, 110), 0.05);
    assertEquals(96, Utils.ClampLooped(96, 10, 110), 0.05);
    assertEquals(100, Utils.ClampLooped(-1000, 10, 110), 0.05);
    assertEquals(90.5, Utils.ClampLooped(95.5, 90, 91), 0.05);
    assertEquals(30, Utils.ClampLooped(130, 10, 9), 0.05);
    assertEquals(30, Utils.ClampLooped(130, 110, 110), 0.05);

  }

  public void testReverse() {
    List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5);
    Utils.reverse(numberList);
    assertEquals((Integer) 5, numberList.get(0));
    assertEquals((Integer) 4, numberList.get(1));
    assertEquals((Integer) 3, numberList.get(2));
    assertEquals((Integer) 2, numberList.get(3));
    assertEquals((Integer) 1, numberList.get(4));
  }

  public void testSwap() {
    List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5);
    Utils.swap(numberList, 0, 4);
    assertEquals((Integer) 5, numberList.get(0));
    assertEquals((Integer) 1, numberList.get(4));
    assertEquals((Integer) 2, numberList.get(1));
    assertEquals((Integer) 3, numberList.get(2));
    assertEquals((Integer) 4, numberList.get(3));
  }

  public void testClampLooped() {
    assertEquals(30.0, Utils.ClampLooped(130.0, 10, 110));
    assertEquals(80.0, Utils.ClampLooped(-20.0, 10, 110));
  }
}