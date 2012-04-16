package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.map.MapHashed;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Bearing;

public class DiscoverTest extends TestCase {
  
  /**
   * Test of interpret method, of class Discover.
   */
  @Test
  public void testInterpret() {
    System.out.println("interpret");
    MapHashed m = new MapHashed();
    Scanner sc = new Scanner("0 0 0 0 1 1");
    Discover instance = new Discover();
    instance.interpret(m, sc);
    Tile s = m.get(1, 1);
    assertFalse(s.hasWall(Bearing.N));
    assertFalse(s.hasWall(Bearing.E));
    assertTrue(s.hasWall(Bearing.S));
    assertTrue(s.hasWall(Bearing.W));
    
    sc = new Scanner("1 0 1 0 1 0");
    instance.interpret(m, sc);
    s = m.get(2,1);
    assertTrue(s.hasWall(Bearing.N));
    assertFalse(s.hasWall(Bearing.E));
    assertTrue(s.hasWall(Bearing.S));
    assertFalse(s.hasWall(Bearing.W));
    
    sc = new Scanner("1 -1 1 1 1 0");
    instance.interpret(m, sc);
    s = m.get(2,2);
    assertTrue(s.hasWall(Bearing.N));
    assertTrue(s.hasWall(Bearing.E));
    assertTrue(s.hasWall(Bearing.S));
    assertFalse(s.hasWall(Bearing.W));
  }

  /**
   * Test of getName method, of class Discover.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    Discover instance = new Discover();
    String expResult = "DISCOVER";
    String result = instance.getName();
    assertEquals(expResult, result);
  }
}
