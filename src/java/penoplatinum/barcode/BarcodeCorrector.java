/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

import penoplatinum.modelprocessor.BufferSubset;

/**
 *
 * @author Rupsbant
 */
public interface BarcodeCorrector {

  int correct(int in);

  byte[] getExpand();

  int translate(BufferSubset list);
  
}
