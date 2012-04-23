package penoplatinum.map;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Point;

public class MapHashedTest extends TestCase {

  /**
   * Test of add method, of class MapHashed.
   */
  @Test
  public void testAdd() {
    MapHashed instance = new MapHashed();
    try{
      instance.add(null);
      fail("Should throw exception");
    } catch(RuntimeException e){
    }
  }

  /**
   * Test of exists method, of class MapHashed.
   */
  @Test
  public void testExists() {
    System.out.println("exists");
    MapHashed instance = new MapHashed();
    assertFalse(instance.exists(1, 1));
    instance.put(getTile(), 0, 0);
    assertTrue(instance.exists(1, 1));
    instance.put(getTile(), -1, 1);
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

    Tile s1 = getTile();
    instance.put(s1, 0, 0);
    assertEquals(s1, instance.get(1, 1));

    Tile s2 = getTile();
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

    Tile s1 = getTile();
    instance.put(s1, 0, 0);
    assertEquals(s1, instance.getRaw(0, 0));

    Tile s2 = getTile();
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
    assertEquals(null, instance.getFirst());
    Tile s1 = getTile();
    instance.put(s1, 0, 0);
    assertEquals(s1, instance.getFirst());
  }

  /**
   * Test of getHeight method, of class MapHashed.
   */
  @Test
  public void testGetHeight() {
    System.out.println("getHeight");
    MapHashed instance = new MapHashed();
    assertEquals(0, instance.getHeight());
    instance.put(getTile(), 0, 0);
    assertEquals(1, instance.getHeight());
    instance.put(getTile(), 1, 0);
    assertEquals(1, instance.getHeight());
    instance.put(getTile(), 0, 1);
    assertEquals(2, instance.getHeight());
    instance.put(getTile(), 1, 1);
    assertEquals(2, instance.getHeight());
  }

  /**
   * Test of getWidth method, of class MapHashed.
   */
  @Test
  public void testGetWidth() {
    System.out.println("getWidth");
    MapHashed instance = new MapHashed();
    assertEquals(0, instance.getWidth());
    instance.put(getTile(), 0, 0);
    assertEquals(1, instance.getWidth());
    instance.put(getTile(), 0, 1);
    assertEquals(1, instance.getWidth());
    instance.put(getTile(), 1, 0);
    assertEquals(2, instance.getWidth());
    instance.put(getTile(), 1, 1);
    assertEquals(2, instance.getWidth());
  }

  /**
   * Test of getTileCount method, of class MapHashed.
   */
  @Test
  public void testGetTileCount() {
    System.out.println("getTileCount");
    MapHashed instance = new MapHashed();
    assertEquals(0, instance.getTileCount());
    instance.put(getTile(), 0, 1);
    assertEquals(1, instance.getTileCount());
    instance.put(getTile(), 1, 1);
    assertEquals(2, instance.getTileCount());
    instance.put(getTile(), 1, 1);
    assertEquals(2, instance.getTileCount());
  }

  /**
   * Test of addGhostPosition method, of class MapHashed.
   */
  @Test
  public void testAddGhostPosition() {
    System.out.println("addGhostPosition");
    MapHashed instance = new MapHashed();
    instance.put(getTile(), 0, 0);
    assertEquals(0, instance.ghosts.size());
    instance.addGhostPosition(new Point(0, 0));
    assertEquals(1, instance.ghosts.size());
    assertEquals(new Point(0, 0), instance.ghosts.get(0));
  }

  /**
   * Test of getGhostPositions method, of class MapHashed.
   */
  @Test
  public void testGetGhostPositions() {
    System.out.println("getGhostPositions");
    MapHashed instance = new MapHashed();
    assertEquals(instance.ghosts, instance.getGhostPositions());
  }

  /**
   * Test of setPacmanPosition method, of class MapHashed.
   */
  @Test
  public void testSetPacmanPosition() {
    System.out.println("setPacmanPosition");
    MapHashed instance = new MapHashed();
    instance.put(getTile(), -1, -2);

    instance.setPacmanPosition(new Point(3, -1));
    assertEquals(new Point(5, 2), instance.getPacmanPosition());

    instance.setPacmanPosition(new Point(-1, -2));
    assertEquals(new Point(1, 1), instance.getPacmanPosition());
  }

  private Tile getTile() {
    return mock(Tile.class);
  }
}
