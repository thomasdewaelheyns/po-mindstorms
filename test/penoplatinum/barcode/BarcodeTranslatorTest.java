/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import junit.framework.TestCase;
import penoplatinum.util.Buffer;

/**
 *
 * @author Thomas
 */
public class BarcodeTranslatorTest extends TestCase{
  
  public BarcodeTranslatorTest(String name) {
    super(name);
  }
  
  public void testReverse(){
    assertEquals(6, BarcodeTranslator.reverse(6, 4));
    assertEquals(96, BarcodeTranslator.reverse(6, 8));
    
    assertEquals(10,BarcodeTranslator.reverse(5, 4));
    assertEquals(2,BarcodeTranslator.reverse(4, 4));
  }
  
  public void testTranslate(){
    Buffer exact = new Buffer(100);
    for(int i = 0; i<12; i++)
      exact.add();
  }
}
