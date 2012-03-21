/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import penoplatinum.util.BufferSubset;

/**
 *
 * @author Rupsbant
 */
public interface BarcodeCorrector {

  int correct(int in);

  int translate(BufferSubset list);
  
  public byte expandBarcode(int compressed);
  
}
