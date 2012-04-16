package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.map.MapHashed;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.util.Point;

public class PacmanTest extends TestCase {

  /**
   * Test of interpret method, of class Pacman.
   */
  @Test
  public void testInterpret() {
    System.out.println("interpret");
    MapHashed map = new MapHashed();
    map.put(new Sector(), 0, 0);
    Scanner scanner = new Scanner("0 0");
    Pacman instance = new Pacman();
    instance.interpret(map, scanner);
    assertEquals(new Point(1, 1), map.getPacmanPosition());
  }

  /**
   * Test of getName method, of class Pacman.
   */
  @Test
  public void testGetName() {
    System.out.println("getName");
    Pacman instance = new Pacman();
    String expResult = "PACMAN";
    String result = instance.getName();
    assertEquals(expResult, result);
  }
}
