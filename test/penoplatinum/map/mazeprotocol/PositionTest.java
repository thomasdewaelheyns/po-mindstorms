package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.map.MapHashed;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.util.Point;

public class PositionTest extends TestCase {

  /**
   * Test of interpret method, of class Position.
   */
  @Test
  public void testInterpret() {
    System.out.println("interpret");
    MapHashed map = new MapHashed();
    map.put(new Sector(), 0, 0);
    Scanner scanner = new Scanner("0 0");
    Position instance = new Position();

    instance.interpret(map, scanner);
    assertEquals(new Point(1, 1), map.getGhostPositions().get(0));

    scanner = new Scanner("1 1");
    instance.interpret(map, scanner);
    assertEquals(new Point(2, 0), map.getGhostPositions().get(1));

    scanner = new Scanner("1 -1");
    instance.interpret(map, scanner);
    assertEquals(new Point(2, 2), map.getGhostPositions().get(2));
  }

  /**
   * Test of getName method, of class Position.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    Position instance = new Position();
    String expResult = "POSITION";
    String result = instance.getName();
    assertEquals(expResult, result);
  }
}
