/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import penoplatinum.map.Map;
import penoplatinum.map.MapUtil;
import static org.junit.Assert.*;
import penoplatinum.simulator.entities.SimulatedEntity;

/**
 *
 * @author Rupsbant
 */
public class MapUtilTest {
  
  public MapUtilTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  /**
   * Test of findHitDistance method, of class MapUtil.
   */
  @Test
  public void testFindHitDistance() {
    System.out.println("findHitDistance");
    Map map = null;
    int angle = 0;
    int left = 0;
    int top = 0;
    double x = 0.0;
    double y = 0.0;
    int expResult = 0;
    int result = MapUtil.findHitDistance(map, angle, left, top, x, y);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of hasTile method, of class MapUtil.
   */
  @Test
  public void testHasTile() {
    System.out.println("hasTile");
    Map map = null;
    double positionX = 0.0;
    double positionY = 0.0;
    boolean expResult = false;
    boolean result = MapUtil.hasTile(map, positionX, positionY);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of goesThroughWallX method, of class MapUtil.
   */
  @Test
  public void testGoesThroughWallX() {
    System.out.println("goesThroughWallX");
    Map map = null;
    SimulatedEntity entity = null;
    double dx = 0.0;
    boolean expResult = false;
    boolean result = MapUtil.goesThroughWallX(map, entity, dx);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of goesThroughWallY method, of class MapUtil.
   */
  @Test
  public void testGoesThroughWallY() {
    System.out.println("goesThroughWallY");
    Map map = null;
    SimulatedEntity entity = null;
    double dy = 0.0;
    boolean expResult = false;
    boolean result = MapUtil.goesThroughWallY(map, entity, dy);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}
