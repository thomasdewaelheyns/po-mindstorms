package penoplatinum.modelprocessor;

import java.awt.Color;
import penoplatinum.Utils;
import penoplatinum.barcode.BarcodeBlackBlack;
import penoplatinum.barcode.BarcodeCorrector;
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
public class BarcodeBlackModelProcessor extends ModelProcessor {

  public static final int END_OF_BARCODE_BROWN_COUNT = 10; // Was 10
  private static final int WAITING = 0;
  private static final int RECORDING = 1;
  private static final int INTERPRET = 2;
  private BarcodeCorrector interpreter;
  private int brownCounter = 0;
  int state = WAITING;
  public float WIELOMTREK = 0.175f;

  /**
   * The constructor in case the BarcodeModelProcessor is the last in a linked list of ModdelProcessors
   * or when there is only one ModdelProcessor.
   * A colorInterpreter is created.
   */
  public BarcodeBlackModelProcessor() {
    super();
    interpreter = new BarcodeBlackBlack();
  }

  /**
   * The constructor for when the BarcodeModelProcessor is not the last element in a
   * linked list with at least 2 ModelProcessors
   * A colorInterpreter is created.
   * @param nextProcessor 
   *        The next ModelProcessor in the linked list.
   */
  public BarcodeBlackModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
    interpreter = new BarcodeBlackBlack();
  }

  /**
   * Sets the model for all ModelProcessors
   * @param model 
   *        The model we are using.
   */
  @Override
  public void setModel(Model model) {
    super.setModel(model);
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
    updateState(tempBuffer);
    model.setReadingBarcode(state != WAITING);
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
        if (model.getCurrentLightColor() == LightColor.Black) {
          Utils.Log("Barcode Start");
          setState(RECORDING);
          tempBuffer.setCheckPoint();
          brownCounter = 0;
        }
        break;
      case RECORDING:
        if (model.getCurrentLightColor() == LightColor.Brown) {
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
        BufferSubset subset = tempBuffer.getBufferSubset(brownCounter);
        int barcode = interpreter.translate(subset)/2 & ((1<<7)-1);
        Utils.Log("Barcode 2: "+barcode);
        Utils.Log("Size : " + tempBuffer.getCheckpointSize());
        setState(WAITING);
        tempBuffer.unsetCheckPoint();

        int corrected = barcode;
        if (barcode != Barcode.None) {
          corrected = interpreter.correct(barcode);
        }
        model.setBarcode(corrected);

    }
  }

  private void setState(int s) {
    state = s;
  }
}
