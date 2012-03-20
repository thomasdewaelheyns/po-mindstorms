package penoplatinum.model;

import penoplatinum.util.Buffer;
import penoplatinum.util.Utils;

/**
 * Contains information about the last barcode scanned, and current barcode
 * scanner status
 * 
 * @author MHGameWork
 */
public class BarcodeModelPart {

  private int bufferSize = 1000;
  private int barcode = -1;
  private Buffer lightValueBuffer = new Buffer(bufferSize);

  public int getBarcode() {
    return this.barcode;
  }
  private int lastBarcode = -1;

  public void setBarcode(int barcode) {
    if (barcode == 0) {
      barcode = -1;
    }
    if (barcode != -1) {
      lastBarcode = barcode;

      Utils.Log(barcode + "");

      // Barcode update is sent on next position send!!

    }
    this.barcode = barcode;

  }

  /**
   * Todo: maybe move this to the LightModelPart?
   * @return 
   */
  public Buffer getLightValueBuffer() {
    return this.lightValueBuffer;
  }

  public void setReadingBarcode(boolean b) {
    this.isReadingBarcode = b;
  }

  public boolean isReadingBarcode() {
    return this.isReadingBarcode;
  }

  boolean isReadingBarcode = false;

  public int getLastBarcode() {
    return lastBarcode;
  }
  
  
}
