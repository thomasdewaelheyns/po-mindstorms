package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import junit.framework.TestCase;
import org.junit.Test;

public class NameTest extends TestCase {

  /**
   * Test of interpret method, of class Name.
   */
  @Test
  public void testInterpret() {
    Name instance = new Name();
    instance.interpret(null, new Scanner("version"));
  }

  /**
   * Test of getName method, of class Name.
   */
  @Test
  public void testGetName() {
    Name instance = new Name();
    String expResult = "NAME";
    String result = instance.getName();
    assertEquals(expResult, result);
  }
}
