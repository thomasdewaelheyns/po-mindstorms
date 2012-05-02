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


  private Barcode readingBarcode      = new Barcode();
  private Barcode lastFinishedBarcode = new Barcode();
  private boolean isReading = false;


  public void startNewReading() {
    this.readingBarcode     = new Barcode();
    this.isReading   = true;
  }
  
  public void addReading(LightColor color) {
    if( this.isReading ) {
      this.readingBarcode.addColor(color);
    }
  }

  public void finishReading() {
    this.lastFinishedBarcode = this.readingBarcode;
    stopReading();
  }
  
  private void stopReading(){
    this.isReading = false;
  }
  
  public void discardReading() {
    stopReading();
  }
  
  public boolean isReadingBarcode() {
    return this.isReading;
  }

  /*
   * Not interesting?
   *
  public int getCurrentBarcodeValue() {
    return this.readingBarcode.translate();
  }/**/
  
  public int getLastBarcodeValue() {
    return this.lastFinishedBarcode.translate();
  }
}
