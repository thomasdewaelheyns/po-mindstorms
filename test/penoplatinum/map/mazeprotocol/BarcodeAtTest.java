package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.map.MapHashed;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Bearing;

public class BarcodeAtTest extends TestCase {
  
  /**
   * Test of interpret method, of class BarcodeAt.
   */
  @Test
  public void testInterpret() {
    System.out.println("interpret");
    MapHashed m = new MapHashed();
    BarcodeAt instance = new BarcodeAt();
    Scanner sc = new Scanner("0 0 2 1");
    instance.interpret(m, sc);
    sc = new Scanner("1 0 3 2");
    instance.interpret(m, sc);
    sc = new Scanner("0 -1 5 3");
    instance.interpret(m, sc);
    sc = new Scanner("1 -1 7 4");
    instance.interpret(m, sc);
    Tile s = m.get(1, 1);
    assertFalse(s.hasWall(Bearing.N));
    assertTrue(s.hasWall(Bearing.E));
    assertFalse(s.hasWall(Bearing.S));
    assertTrue(s.hasWall(Bearing.W));
    assertEquals(4, s.getBarcode8Bit());
    assertEquals(Sector.class, s.getClass());
    
    s = m.get(2, 1);
    assertTrue(s.hasWall(Bearing.N));
    assertFalse(s.hasWall(Bearing.E));
    assertTrue(s.hasWall(Bearing.S));
    assertFalse(s.hasWall(Bearing.W));
    assertEquals(6, s.getBarcode8Bit());
    assertEquals(Sector.class, s.getClass());
    
    s = m.get(1, 2);
    assertFalse(s.hasWall(Bearing.N));
    assertTrue(s.hasWall(Bearing.E));
    assertFalse(s.hasWall(Bearing.S));
    assertTrue(s.hasWall(Bearing.W));
    assertEquals(10, s.getBarcode8Bit());
    assertEquals(Sector.class, s.getClass());
    
    s = m.get(2, 2);
    assertTrue(s.hasWall(Bearing.N));
    assertFalse(s.hasWall(Bearing.E));
    assertTrue(s.hasWall(Bearing.S));
    assertFalse(s.hasWall(Bearing.W));
    assertEquals(14, s.getBarcode8Bit());
    assertEquals(Sector.class, s.getClass());
    
    sc = new Scanner("1 -1 7 4");
    instance.interpret(m, sc);
    assertNotSame(s, m.get(2, 2));
  }

  /**
   * Test of getName method, of class BarcodeAt.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    BarcodeAt instance = new BarcodeAt();
    String expResult = "BARCODEAT";
    String result = instance.getName();
    assertEquals(expResult, result);
  }
}
