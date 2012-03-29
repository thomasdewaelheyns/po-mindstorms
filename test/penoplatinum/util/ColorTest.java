package penoplatinum.util;

/**
 * ColorTest
 * 
 * Tests Color class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 


public class ColorTest extends TestCase {

  public ColorTest(String name) { 
    super(name);
  }

  public void testIntConstructor() {
    Color color = new Color((int)12, (int)34, (int)56);

    assertEquals(12, color.getR());
    assertEquals(34, color.getG());
    assertEquals(56, color.getB());
  }

  public void testByteConstructor() {
    Color color = new Color((byte)12, (byte)34, (byte)56);

    assertEquals(12, color.getR());
    assertEquals(34, color.getG());
    assertEquals(56, color.getB());
  }
  
}