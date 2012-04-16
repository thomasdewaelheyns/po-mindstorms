/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Thomas
 */
public class UtilsTest extends TestCase{
  
  public UtilsTest(String name) {
    super(name);
  }

  public void testReverse() {
    List<Integer> numberList = Arrays.asList(1,2,3,4,5);
    Utils.reverse(numberList);
    assertEquals((Integer)5, numberList.get(0));
    assertEquals((Integer)4, numberList.get(1));
    assertEquals((Integer)3, numberList.get(2));
    assertEquals((Integer)2, numberList.get(3));
    assertEquals((Integer)1, numberList.get(4));
  }


  public void testSwap() {
    List<Integer> numberList = Arrays.asList(1,2,3,4,5);
    Utils.swap(numberList, 0, 4);
    assertEquals((Integer)5, numberList.get(0));
    assertEquals((Integer)1, numberList.get(4));
    assertEquals((Integer)2, numberList.get(1));
    assertEquals((Integer)3, numberList.get(2));
    assertEquals((Integer)4, numberList.get(3));
  }
}
