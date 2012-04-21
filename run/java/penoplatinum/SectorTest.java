/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import org.junit.*;
import penoplatinum.simulator.tiles.Sector;

/**
 *
 * @author MHGameWork
 */
public class SectorTest {
  
  @Test
  public void testBarcode()
  {
    Sector s = new Sector();
    
    s.addBarcode(14, 0);
    
    Assert.assertEquals(14,s.getBarcode8Bit());
    
  }
}
