package penoplatinum.map.mazeprotocol;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.map.Map;

public class ProtocolMapFactoryTest extends TestCase {
  
  /**
   * Test of getMap method, of class ProtocolMapFactory.
   */
  @Test
  public void testGetMap() {
    Scanner sc = new Scanner("ABC BARCODEAT 0 0 3 1 ABC BARCODEAT 1 0 4 1 ");
    ProtocolMapFactory instance = new ProtocolMapFactory();
    Map result = instance.getMap(sc);
    assertEquals(6, result.get(1, 1).getBarcode8Bit());
    assertEquals(8, result.get(2, 1).getBarcode8Bit());
  }
  
  @Test
  public void testCommandos() {
    ProtocolMapFactory instance = new ProtocolMapFactory();
    assertEquals(5, instance.commandos.size());
    Set<String> s = new HashSet<String>();
    s.add("DISCOVER");
    s.add("BARCODEAT");
    s.add("NAME");
    s.add("POSITION");
    s.add("PACMAN");
    assertEquals(s , instance.commandos.keySet());
  }
}
