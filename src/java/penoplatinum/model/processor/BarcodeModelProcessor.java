package penoplatinum.model.processor;

/**
 * This model processor watches the lightsensordata for barcodes.
 * These barcodes start and end with a black line
 * 
 * Author: Team Platinum
 */

import penoplatinum.model.Model;

import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.LightModelPart;

import penoplatinum.util.LightColor;


public class BarcodeModelProcessor extends ModelProcessor {
  // boilerplate Decorator setup
  public BarcodeModelProcessor() { super(); }
  public BarcodeModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  // the number of readings of brown before we decide we passed the barcode
  private static final int BROWN_AFTER            =  10;
  // the number of brow interference we find acceptable in ...
  private static final int MAX_BROWN_INTERFERENCE =  5;
  // the first minimum amount of readings
  private static final int MIN_READINGS           = 10;

  // keep track of readings, total and brown ones
  private int totalCount = 0, brownCount = 0;
    
  // are we waiting for a new barcode to start (or are we reading it...)
  private boolean isWaiting = true;
  private boolean wasCorrupt = false;

  private BarcodeModelPart barcodePart;
  private SensorModelPart  robot;
  private LightModelPart   sensors;
  
  private int sensorUpdate = 0;


  // override the setModel to setup a reference to the BarcodeModelPart
  public void setModel(Model model) {
    super.setModel(model);
    this.barcodePart = BarcodeModelPart.from(this.getModel());
    this.robot   = SensorModelPart.from(this.getModel());
    this.sensors = LightModelPart.from(this.getModel());
  }

  // actual logic
  protected void work() {
    // we only work with new sensor values...
    if( ! this.newSensorValuesAreAvailable() ) { return; }
  
     if( this.isWaiting ) {
      // BLACK marks the beginning of a new barcode
      if( this.sensors.getCurrentLightColor() == LightColor.BLACK ) {
        this.startReading();
        //Utils.Log("Start reading barcode.");
      }
    } else { // we're reading...
      // turning makes the barcodereading corrupted, discard it
      // reading too much interference also discards the reading
       if (this.robot.isTurning() ) {
        this.discardReading();
        wasCorrupt = true;
        //Utils.Log("Turning, stupid driver, I can't read the barcode.");
        
       } else if( this.readInterference()){
        this.discardReading();
        isWaiting = true;
        wasCorrupt = false;
        //Utils.Log("Too much brown, I can't read it");
        
      } else if( !this.robot.isMoving() ){  // if we are not moving wait until we are moving again.
        
      } else if (this.wasCorrupt) {
         if( this.passedBarcode()){
           wasCorrupt = false;
           isWaiting = true;
         } else {
           addReading();
         }
         
       } else if( this.passedBarcode() ) {   // if we passed the barcode, we can stop
        this.stopReading();
        
      } else {
        this.addReading();
      }
    }
  }
  
  private boolean newSensorValuesAreAvailable() {
    int currentSensorUpdate = this.robot.getValuesID();
    if( currentSensorUpdate == this.sensorUpdate ) { return false; }
    this.sensorUpdate = currentSensorUpdate;
    return true;
  }

  private void startReading() {
    this.isWaiting  = false;
    this.totalCount = 0;
    this.brownCount = 0;
    this.barcodePart.startNewReading();
    this.addReading();
  }
  
  private void discardReading() {
    this.barcodePart.discardReading();
  }
  
  private void stopReading() {
    this.barcodePart.finishReading();
    //Utils.Log("It was: "+this.barcodePart.getLastBarcodeValue());
    //Utils.Log("Without border: "+this.barcodePart.getLastBarcodeValue()/2);
    //Utils.Log("Reversed: " + Barcode.reverse(this.barcodePart.getLastBarcodeValue(), 8));
    //Utils.Log("W/O border: " + Barcode.reverse(this.barcodePart.getLastBarcodeValue(), 8)/2);
    this.isWaiting = true;
  }

  private void addReading() {
    LightColor color = this.sensors.getCurrentLightColor();
    this.totalCount++;
    // count consecutive brown readings
    if( color == LightColor.BROWN ) { this.brownCount++;   }
    else                            { this.brownCount = 0; }
    this.barcodePart.addReading(color);
  }
  
  private boolean readInterference() {
    return this.totalCount < MIN_READINGS &&
           this.brownCount > MAX_BROWN_INTERFERENCE;
  }

  private boolean passedBarcode() {
    return this.brownCount > BROWN_AFTER;
  }
}
