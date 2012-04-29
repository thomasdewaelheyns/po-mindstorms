/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import junit.framework.TestCase;

/**
 *
 * @author Thomas
 */
public class MD5Test extends TestCase{
  
  public void testGetHashString(){
    assertEquals("9e107d9d372bb6826bd81d3542a419d6", MD5.getHashString("The quick brown fox jumps over the lazy dog"));
    assertEquals("e4d909c290d0fb1ca068ffaddf22cbd0", MD5.getHashString("The quick brown fox jumps over the lazy dog."));
    assertEquals("d41d8cd98f00b204e9800998ecf8427e", MD5.getHashString(""));
  }
}
