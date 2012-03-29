package penoplatinum.util;

/**
 * BitwiseOperation
 * 
 * Some utility functions to encode and decode bitfields.
 * 
 * @author: Team Platinum
 */


public class BitwiseOperations {
  
  // sets one bit at position to 1
  public static int setBit(int data, int p) {
    return data | (1<<p);
  }

  // sets one bit at position to 0
  public static int unsetBit(int data, int p) {
    return data & ~(1<<p);
  }
  
  // checks if at position the bit is set to 1
  public static Boolean hasBit(int data, int p) {
    return ((data >> p) & 1) != 0;
  }

  // sets a range of bits, represented by value
  public static int setBits(int data, int start, int length, int value) {
    data = unsetBits(data, start, length );
    value <<= start;
    return data | value;
  }

  // sets a range of bits to 0
  public static int unsetBits(int data, int start, int length) {
    int mask = ( ( 1 << length ) - 1 ) << start;
    return data & ~(mask);
  }

  // returns a range of length bits starting at start
  public static int getBits(int data, int start, int length) {
    int mask = ( 1 << length ) - 1;
    return ( ( data >> start ) & mask );
  }
}
