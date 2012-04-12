package penoplatinum.model.part;

/**
 * Contains information about the last barcode scanned, and current barcode
 * scanner status
 * 
 * @author Team Platinum
 */

import penoplatinum.model.Model;


public class BarcodeModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static BarcodeModelPart from(Model model) {
    return (BarcodeModelPart)model.getPart(ModelPartRegistry.BARCODE_MODEL_PART);
  }

  boolean isReadingBarcode = false;
  
  public boolean isReadingBarcode() {
    return this.isReadingBarcode;
  }

}

  /*
  
  TODO:
  
  private int bufferSize = 1000;
  private int barcode = -1;
  private Buffer lightValueBuffer = new Buffer(bufferSize);
  private int lastBarcode = -1;
    
  
  public int getBarcode() {
    return this.barcode;
  }


  public void setBarcode(int barcode) {
    if (barcode == 0) {
      barcode = -1;
    }
    if (barcode != -1) {
      lastBarcode = barcode;

      Utils.Log(barcode + ","+BarcodeTranslator.reverse(barcode, 6));

      // Barcode update is sent on next position send!!

    }
    this.barcode = barcode;

  }

  public Buffer getLightValueBuffer() {
    return this.lightValueBuffer;
  }

  public void setReadingBarcode(boolean b) {
    this.isReadingBarcode = b;
  }


  public int getLastBarcode() {
    return lastBarcode;
  }
  
  // This should be called after the robot has moved to the next tile.
  public void clearLastBarcode()
  {
    lastBarcode = -1;
  }
  

//  public void clearLastBarcode() {
//    lastBarcode = -1;
//  }

  @Override
  public void clearDirty() {
    barcode = -1;
  }
*/
