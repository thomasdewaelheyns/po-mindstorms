package penoplatinum.model.part;

/**
 * Contains information about the last barcode scanned, and current barcode
 * scanner status
 * 
 * @author Team Platinum
 */

import penoplatinum.model.Model;

import penoplatinum.util.LightColor;


public class BarcodeModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static BarcodeModelPart from(Model model) {
    return (BarcodeModelPart)model.getPart(ModelPartRegistry.BARCODE_MODEL_PART);
  }


  private Barcode barcode     = new Barcode(),
                  prevBarcode = new Barcode();
  private boolean isReading = false;


  public void startNewReading() {
    this.prevBarcode = new Barcode(this.barcode);
    this.barcode     = new Barcode();
    this.isReading   = true;
  }
  
  public void addReading(LightColor color) {
    if( this.isReading ) {
      this.barcode.addColor(color);
    }
  }

  public void stopReading() {
    this.isReading = false;
  }
  
  public void discardReading() {
    this.stopReading();
    this.barcode = new Barcode();
  }
  
  public boolean isReadingBarcode() {
    return this.isReading;
  }

  public int getCurrentBarcodeValue() {
    return this.barcode.translate();
  }
  
  public int getPreviousBarcodeValue() {
    return this.prevBarcode.translate();
  }
}
