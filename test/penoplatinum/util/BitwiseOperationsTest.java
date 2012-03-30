package penoplatinum.util;

/**
 * BitwiseOperationsTest
 * 
 * Tests BitwiseOperations functions
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 

import static penoplatinum.util.BitwiseOperations.*;


public class BitwiseOperationsTest extends TestCase {

  public BitwiseOperationsTest(String name) { 
    super(name);
  }

  // sets one bit at position to 1
  public void testSetBit() {
    int data = 0;
    assertEquals(  1, setBit(data,0));
    assertEquals(  2, setBit(data,1));
    assertEquals(  4, setBit(data,2));
    assertEquals(  8, setBit(data,3));

    data = 1;
    assertEquals(  1, setBit(data,0));
    assertEquals(  3, setBit(data,1));
    assertEquals(  5, setBit(data,2));
    assertEquals(  9, setBit(data,3));

    data = 2;
    assertEquals(  3, setBit(data,0));
    assertEquals(  2, setBit(data,1));
    assertEquals(  6, setBit(data,2));
    assertEquals( 10, setBit(data,3));

    data = 3;
    assertEquals(  3, setBit(data,0));
    assertEquals(  3, setBit(data,1));
    assertEquals(  7, setBit(data,2));
    assertEquals( 11, setBit(data,3));

    data = 4;
    assertEquals(  5, setBit(data,0));
    assertEquals(  6, setBit(data,1));
    assertEquals(  4, setBit(data,2));
    assertEquals( 12, setBit(data,3));

    data = 5;
    assertEquals(  5, setBit(data,0));
    assertEquals(  7, setBit(data,1));
    assertEquals(  5, setBit(data,2));
    assertEquals( 13, setBit(data,3));

    data = 6;
    assertEquals(  7, setBit(data,0));
    assertEquals(  6, setBit(data,1));
    assertEquals(  6, setBit(data,2));
    assertEquals( 14, setBit(data,3));

    data = 7;
    assertEquals(  7, setBit(data,0));
    assertEquals(  7, setBit(data,1));
    assertEquals(  7, setBit(data,2));
    assertEquals( 15, setBit(data,3));

    data = 8;
    assertEquals(  9, setBit(data,0));
    assertEquals( 10, setBit(data,1));
    assertEquals( 12, setBit(data,2));
    assertEquals(  8, setBit(data,3));

    data = 9;
    assertEquals(  9, setBit(data,0));
    assertEquals( 11, setBit(data,1));
    assertEquals( 13, setBit(data,2));
    assertEquals(  9, setBit(data,3));

    data = 10;
    assertEquals( 11, setBit(data,0));
    assertEquals( 10, setBit(data,1));
    assertEquals( 14, setBit(data,2));
    assertEquals( 10, setBit(data,3));

    data = 11;
    assertEquals( 11, setBit(data,0));
    assertEquals( 11, setBit(data,1));
    assertEquals( 15, setBit(data,2));
    assertEquals( 11, setBit(data,3));

    data = 12;
    assertEquals( 13, setBit(data,0));
    assertEquals( 14, setBit(data,1));
    assertEquals( 12, setBit(data,2));
    assertEquals( 12, setBit(data,3));

    data = 14;
    assertEquals( 15, setBit(data,0));
    assertEquals( 14, setBit(data,1));
    assertEquals( 14, setBit(data,2));
    assertEquals( 14, setBit(data,3));

    data = 15;
    assertEquals( 15, setBit(data,0));
    assertEquals( 15, setBit(data,1));
    assertEquals( 15, setBit(data,2));
    assertEquals( 15, setBit(data,3));
  }

  public void testUnsetBit() {
    int data = 15;
    assertEquals( 14, unsetBit(data,0));
    assertEquals( 13, unsetBit(data,1));
    assertEquals( 11, unsetBit(data,2));
    assertEquals(  7, unsetBit(data,3));
  }
  
  public void testHasBit() {
    int data = 5;
    assertTrue( hasBit(data,0));
    assertFalse( hasBit(data,1));
    assertTrue(  hasBit(data,2));
    assertFalse( hasBit(data,3));
  }

  public void testSetBits() {
    int data = 3;
    assertEquals( 11, setBits(data, 2, 2, 2));
  }

  public void testUnsetBits() {
    int data = 15;
    assertEquals( 9, unsetBits(data, 1, 2));
  }

  public void testGetBits() {
    int data = 15;
    assertEquals( 3, getBits(data, 2, 2));
  }
}
