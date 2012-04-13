package penoplatinum.util;

/**
 * LightColorTest
 * 
 * Tests LightColor enumeration
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 


public class LightColorTest extends TestCase {

  public LightColorTest(String name) { 
    super(name);
  }
 
  public void testToString() {
    assertEquals( "BLACK", LightColor.BLACK.toString() );
    assertEquals( "WHITE", LightColor.WHITE.toString() );
    assertEquals( "BROWN", LightColor.BROWN.toString() );
  }
  
}
