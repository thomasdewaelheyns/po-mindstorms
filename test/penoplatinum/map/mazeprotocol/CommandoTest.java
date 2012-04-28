package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.map.MapHashed;

public class CommandoTest extends TestCase {
  /**
   * Test of interpret method, of class Commando.
   */
  @Test
  public void testInterpret() {
    Commando instance = new CommandoImpl();
    instance.interpret(null, null);
  }

  /**
   * Test of getName method, of class Commando.
   */
  @Test
  public void testGetName() {
    Commando instance = new CommandoImpl();
    assertEquals("", instance.getName());
  }

  public class CommandoImpl implements Commando {

    public void interpret(MapHashed m, Scanner sc) {
    }

    public String getName() {
      return "";
    }
  }
}