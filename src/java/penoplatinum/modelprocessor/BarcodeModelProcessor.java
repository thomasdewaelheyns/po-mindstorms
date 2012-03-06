package penoplatinum.modelprocessor;

import penoplatinum.barcode.BarcodeHammingCorrector;
import penoplatinum.modelprocessor.LightColor;
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
  private BarcodeHammingCorrector interpreter;
  private ColorInterpreter colorInterpreter;
  private int brownCounter = 0;
  int state = WAITING;
  private float startTacho;
  private float endTacho;
  public float WIELOMTREK = 0.175f;
  //the length of a barcode strip
  private float barcodeLength = 0.14f;

  /**
   * The constructor in case the BarcodeModelProcessor is the last in a linked list of ModdelProcessors
   * or when there is only one ModdelProcessor.
   * A colorInterpreter is created.
   */
  public BarcodeModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter();
    interpreter = new BarcodeHammingCorrector(colorInterpreter);
  }

  /**
   * The constructor for when the BarcodeModelProcessor is not the last element in a
   * linked list with at least 2 ModelProcessors
   * A colorInterpreter is created.
   * @param nextProcessor 
   *        The next ModelProcessor in the linked list.
   */
  public BarcodeModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
    this.colorInterpreter = new ColorInterpreter();
    interpreter = new BarcodeHammingCorrector(colorInterpreter);
  }

  /**
   * Sets the model for all ModelProcessors
   * @param model 
   *        The model we are using.
   */
  @Override
  public void setModel(Model model) {
    super.setModel(model);
    colorInterpreter.setModel(model);
  }

  /**
   * Every time work is called, a step is taken in the recognition of a barcode.
   * The method checks if the Light Value data is corrupted due to (un-)expected
   * movements. If the data is corrupted, the state of the method is set to
   * waiting.
   * 
   * It then calls upon the function updateState(...) to further handle the recognition.
   */
  @Override
  protected void work() {
    Buffer tempBuffer = this.model.getLightValueBuffer();
    model.setBarcode(Barcode.None);
//    if (model.isLightDataCorrupt()) {
////      if (state != WAITING) {
////        Utils.Log("Turning! Disabling measurement");
////      }
//
//      setState(WAITING);
//      tempBuffer.unsetCheckPoint();
//    }
    updateState(tempBuffer);

//    if (state != WAITING)
//    {
//      model.setScanningLightData(true); // Flag that someone is reading light data.
//    }
  }

  /**
   * The switch case checks in which case the method is. The different states are:
   * WAITING: the robot waits for a change in light values that could indicate the start
   *          of a barcode.
   * RECORDING: The robot suspects a barcode and starts recoding the values.
   *            If the input doesn't meet special requirements, too long or unexpected values,
   *            the recording ends and the state is set back to WAITING.
   *            If the end of the line is detected, the state is set to INTERPRET
   * INTERPRET: The recorded values are a barcode. In this state barcode is 
   *            extracted from the information.
   *            The state is set to WAITING.
   * @param tempBuffer 
   */
  private void updateState(Buffer tempBuffer) {
    switch (state) {
      case WAITING:
        if (!colorInterpreter.isColor(LightColor.Brown)) {
          setState(RECORDING);
          tempBuffer.setCheckPoint();
          brownCounter = 0;
        }
        break;
      case RECORDING:
        if (colorInterpreter.isColor(LightColor.Brown)) {
          brownCounter++;
          if (brownCounter > 5 && tempBuffer.getCheckpointSize() < 10) {
            setState(WAITING);
            tempBuffer.unsetCheckPoint();
          } else if (brownCounter > END_OF_BARCODE_BROWN_COUNT) {
            setState(INTERPRET);
          }
        } else {
          brownCounter = 0;
        }
        break;
      case INTERPRET:
        int barcode = interpreter.translate(tempBuffer.getBufferSubset(END_OF_BARCODE_BROWN_COUNT));
        setState(WAITING);
        tempBuffer.unsetCheckPoint();

        int corrected = barcode;
        if (barcode != Barcode.None) {
          corrected = interpreter.correct(barcode);
        }
        model.setBarcode(corrected);

    }
  }

  /**
   * This method calculates the angle at which the robot drives over the barcode.
   * @return 
   *        Returns the angle in degrees as a double.
   */
  public double calculateAngle() {
    float tachoDiff = this.endTacho - this.startTacho;
    float distanceTraveled = tachoDiff / 360 * WIELOMTREK;
    if (barcodeLength > distanceTraveled) {
      distanceTraveled = barcodeLength;
    }
    double degree = Math.acos(barcodeLength / distanceTraveled) * (360 / (2 * Math.PI));
    return degree;
  }

  private void setState(int s) {
    state = s;
  }
}
