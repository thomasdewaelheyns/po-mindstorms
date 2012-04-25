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

  private Barcode barcode     = null,
                  prevBarcode = new Barcode();


  public void startNewReading() {
    this.barcode = new Barcode();
  }
  
  public void addReading(LightColor color) {
    this.barcode.addColor(color);
  }

  public void stopReading() {
    this.prevBarcode = new Barcode(this.barcode);
    this.barcode = null;
  }
  
  public boolean isReadingBarcode() {
    return this.barcode != null;
  }

  public int getCurrentBarcodeValue() {
    return this.barcode.translate();
  }
  
  public int getPreviousBarcodeValue() {
    return this.prevBarcode.translate();
  }
}
