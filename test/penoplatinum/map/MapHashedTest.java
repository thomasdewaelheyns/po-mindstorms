package penoplatinum.map;

import java.util.List;
import org.junit.Test;
import penoplatinum.simulator.tiles.Sector;
import static org.junit.Assert.*;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Point;

public class MapHashedTest {
  
  public MapHashedTest() {
  }

  /**
   * Test of add method, of class MapHashed.
   */
  @Test(expected = RuntimeException.class)
  public void testAdd() {
    MapHashed instance = new MapHashed();
    Map result = instance.add(null);
  }

  /**
   * Test of exists method, of class MapHashed.
   */
  @Test
  public void testExists() {
    System.out.println("exists");
    MapHashed instance = new MapHashed();
    assertFalse(instance.exists(1, 1));
    instance.put(new Sector(), 0, 0);
    assertTrue(instance.exists(1, 1));
    instance.put(new Sector(), -1, 1);
    //Map was moved.
    assertFalse(instance.exists(1, 1));
    assertTrue(instance.exists(1, 2));
  }

  /**
   * Test of get method, of class MapHashed.
   */
  @Test
  public void testGet() {
    System.out.println("get");
    MapHashed instance = new MapHashed();
    assertEquals(null, instance.get(1, 1));
    
    Sector s1 = new Sector();
    instance.put(s1, 0, 0);
    assertEquals(s1, instance.get(1, 1));
    
    Sector s2 = new Sector();
    instance.put(s2, -1, 1);
    assertEquals(null, instance.get(1, 1));
    assertEquals(s2, instance.get(1, 2));
    assertEquals(s1, instance.get(2, 1));
    assertEquals(null, instance.get(2, 2));
  }

  /**
   * Test of getRaw method, of class MapHashed.
   */
  @Test
  public void testGetRaw() {
    System.out.println("getRaw");

    MapHashed instance = new MapHashed();
    assertEquals(null, instance.getRaw(0, 0));
    
    Sector s1 = new Sector();
    instance.put(s1, 0, 0);
    assertEquals(s1, instance.getRaw(0, 0));
    
    Sector s2 = new Sector();
    instance.put(s2, -1, 1);
    assertEquals(s2, instance.getRaw(-1, 1));
    assertEquals(s1, instance.getRaw(0, 0));
  }

  /**
   * Test of getFirst method, of class MapHashed.
   */
  @Test
  public void testGetFirst() {
    System.out.println("getFirst");
    MapHashed instance = new MapHashed();
    Tile expResult = null;
    Tile result = instance.getFirst();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getHeight method, of class MapHashed.
   */
  @Test
  public void testGetHeight() {
    System.out.println("getHeight");
    MapHashed instance = new MapHashed();
    int expResult = 0;
    int result = instance.getHeight();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getWidth method, of class MapHashed.
   */
  @Test
  public void testGetWidth() {
    System.out.println("getWidth");
    MapHashed instance = new MapHashed();
    int expResult = 0;
    int result = instance.getWidth();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getTileCount method, of class MapHashed.
   */
  @Test
  public void testGetTileCount() {
    System.out.println("getTileCount");
    MapHashed instance = new MapHashed();
    int expResult = 0;
    int result = instance.getTileCount();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of put method, of class MapHashed.
   */
  @Test
  public void testPut() {
    System.out.println("put");
    Tile tile = null;
    int left = 0;
    int top = 0;
    MapHashed instance = new MapHashed();
    Map expResult = null;
    Map result = instance.put(tile, left, top);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of addGhostPosition method, of class MapHashed.
   */
  @Test
  public void testAddGhostPosition() {
    System.out.println("addGhostPosition");
    Point position = null;
    MapHashed instance = new MapHashed();
    MapHashed expResult = null;
    MapHashed result = instance.addGhostPosition(position);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getGhostPositions method, of class MapHashed.
   */
  @Test
  public void testGetGhostPositions() {
    System.out.println("getGhostPositions");
    MapHashed instance = new MapHashed();
    List expResult = null;
    List result = instance.getGhostPositions();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setPacmanPosition method, of class MapHashed.
   */
  @Test
  public void testSetPacmanPosition() {
    System.out.println("setPacmanPosition");
    Point position = null;
    MapHashed instance = new MapHashed();
    MapHashed expResult = null;
    MapHashed result = instance.setPacmanPosition(position);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getPacmanPosition method, of class MapHashed.
   */
  @Test
  public void testGetPacmanPosition() {
    System.out.println("getPacmanPosition");
    MapHashed instance = new MapHashed();
    Point expResult = null;
    Point result = instance.getPacmanPosition();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}
