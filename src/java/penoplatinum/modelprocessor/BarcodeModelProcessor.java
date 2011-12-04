package penoplatinum.modelprocessor;

import penoplatinum.modelprocessor.ColorInterpreter.Color;
import penoplatinum.simulator.Barcode;
import penoplatinum.simulator.Model;

/**
 * FrontPushModelProcessor
 * 
 * Implements a ModelProcessor that detects frontpushes based on a central
 * front pushsensor.
 * 
 * Author: Team Platinum
 */
public class BarcodeModelProcessor extends ModelProcessor {

  public static final int END_OF_BARCODE_BROWN_COUNT = 10; // Was 10
  private static final int WAITING = 0;
  private static final int RECORDING = 1;
  private static final int INTERPRET = 2;
  private BarcodeDataNav interpreter;
  private int brownCounter = 0;
  int state = WAITING;
  private ColorInterpreter colorInterpreter;
  private float startTacho;
  private float endTacho;
  public float WIELOMTREK = 0.175f;
  //the length of a barcode strip
  private float barcodeLength = 0.14f;

  public BarcodeModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter();
    interpreter = new BarcodeDataNav(colorInterpreter);
  }

  public BarcodeModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
    this.colorInterpreter = new ColorInterpreter();
    interpreter = new BarcodeDataNav(colorInterpreter);
  }

  @Override
  public void setModel(Model model) {
    super.setModel(model);
    colorInterpreter.setModel(model);
  }

  @Override
  protected void work() {
    Buffer tempBuffer = this.model.getLightValueBuffer();
    model.setBarcode(Barcode.None);
    if (model.isTurning()) {
//      if (state != WAITING) {
//        Utils.Log("Turning! Disabling measurement");
//      }

      state = WAITING;
      tempBuffer.unsetCheckPoint();
    }
    switch (state) {
      case WAITING:
        if (!colorInterpreter.isColor(Color.Brown)) {
          state = RECORDING;
          tempBuffer.setCheckPoint();
          brownCounter = 0;
        }
        break;
      case RECORDING:
        if (colorInterpreter.isColor(Color.Brown)) {
          brownCounter++;
          if (brownCounter > 5 && tempBuffer.getCheckpointSize() < 10) {
            state = WAITING;
            tempBuffer.unsetCheckPoint();
          } else if (brownCounter > END_OF_BARCODE_BROWN_COUNT) {
            state = INTERPRET;
          }
        } else {
          brownCounter = 0;
        }
        break;
      case INTERPRET:
        int barcodeSize = tempBuffer.getBufferSubset(END_OF_BARCODE_BROWN_COUNT).size();
        int barcode = interpreter.translate(tempBuffer.getBufferSubset(END_OF_BARCODE_BROWN_COUNT));
        state = WAITING;
        tempBuffer.unsetCheckPoint();

        int corrected = barcode;
        if (barcode != Barcode.None) {
          corrected = interpreter.correct(barcode);
        }
        model.setBarcode(corrected);
        
    }
  }

  // returns degrees
  public double calculateAngle() {
    float tachoDiff = this.endTacho - this.startTacho;
    float distanceTraveled = tachoDiff / 360 * WIELOMTREK;
    if (barcodeLength > distanceTraveled)
      distanceTraveled = barcodeLength;
    double degree = Math.acos(barcodeLength / distanceTraveled) * (360 / (2 * Math.PI));
    return degree;
  }
}
