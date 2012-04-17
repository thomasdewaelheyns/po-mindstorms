/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import junit.framework.TestCase;
import penoplatinum.util.LightColor;

/**
 *
 * @author Thomas
 */
public class BarcodeTest extends TestCase{
  
  public BarcodeTest(String name) {
    super(name);
  }
  
  public void testReverse(){
    assertEquals(6, Barcode.reverse(6, 4));
    assertEquals(96, Barcode.reverse(6, 8));
    
    assertEquals(10,Barcode.reverse(5, 4));
    assertEquals(2,Barcode.reverse(4, 4));
  }
  
  public void testTranslate(){
    Barcode code = new Barcode();
    for(int i = 0; i<12; i++) //barcode 01101010
      code.addColor(LightColor.BLACK);
    for(int i = 0; i<12; i++)
      code.addColor(LightColor.WHITE);
    for(int i = 0; i<12; i++)
      code.addColor(LightColor.WHITE);
    for(int i = 0; i<12; i++)
      code.addColor(LightColor.BLACK);
    for(int i = 0; i<12; i++)
      code.addColor(LightColor.WHITE);
    for(int i = 0; i<12; i++)
      code.addColor(LightColor.BLACK);
    for(int i = 0; i<12; i++)
      code.addColor(LightColor.WHITE);
    for(int i = 0; i<12; i++)
      code.addColor(LightColor.BLACK);
    assertEquals(106, code.translate());
    
    code = new Barcode();
    for(int i = 0; i<96; i++) //barcode 00000000
      code.addColor(LightColor.BLACK);
    assertEquals(0, code.translate());
    
    code = new Barcode();
    for(int i = 0; i<96; i++) //barcode 11111111
      code.addColor(LightColor.WHITE);
    assertEquals(255, code.translate());
  }
}
