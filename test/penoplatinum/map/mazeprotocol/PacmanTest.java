package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.map.MapHashed;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Point;

public class PacmanTest extends TestCase {

  /**
   * Test of interpret method, of class Pacman.
   */
  @Test
  public void testInterpret() {
    MapHashed map = new MapHashed();
    map.put(mock(Tile.class), 0, 0);
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
    Pacman instance = new Pacman();
    String expResult = "PACMAN";
    String result = instance.getName();
    assertEquals(expResult, result);
  }
}
