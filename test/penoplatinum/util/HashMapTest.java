/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import java.util.List;
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
public class HashMapTest {

  HashMap<Object, Object> testMap;
  Object keyObject;
  Object keyObject2;
  Object keyObject3;
  Object valueObject;
  Object valueObject2;
  Object valueObject3;

  public HashMapTest() {
  }

  @Before
  public void setUp() {
    keyObject = new Object();
    keyObject2 = new Object();
    keyObject3 = new Object();
    valueObject = new Object();
    valueObject2 = new Object();
    valueObject3 = new Object();
    testMap = new HashMap<Object, Object>();
  }

  /**
   * Test of get method, of class HashMap.
   */
  @Test
  public void testAllMethods() {
    testMap.put(keyObject, valueObject);
    assertEquals(testMap.get(keyObject), valueObject);
    testMap.put(keyObject2, valueObject2);
    assertEquals(testMap.get(keyObject), valueObject);
    testMap.put(keyObject3, valueObject3);
    assertEquals(testMap.get(keyObject), valueObject);
    assertEquals(keyObject, testMap.findKey(valueObject));
    assertEquals(keyObject2, testMap.findKey(valueObject2));
    assertEquals(keyObject3, testMap.findKey(valueObject3));
    List valuesList = testMap.values();
    assertEquals(valuesList.get(0), valueObject);
    assertEquals(valuesList.get(1), valueObject2);
    assertEquals(valuesList.get(2), valueObject3);
    assertFalse(testMap.isEmpty());
    assertEquals(3, testMap.size());
  }
}
