package penoplatinum.navigator;

import penoplatinum.navigator.ColorInterpreter.Color;
import penoplatinum.simulator.Barcode;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.ModelProcessor;

/**
 * FrontPushModelProcessor
 * 
 * Implements a ModelProcessor that detects frontpushes based on a central
 * front pushsensor.
 * 
 * Author: Team Platinum
 */
public class BarcodeModelProcessor extends ModelProcessor {
  public static final int END_OF_BARCODE_BROWN_COUNT = 19;

  private static final int WAITING = 0;
  private static final int RECORDING = 1;
  private static final int INTERPRET = 2;
  private BarcodeDataNav interpreter;
  private int brownCounter = 0;
  int state = WAITING;
  private ColorInterpreter colorInterpreter;

  public BarcodeModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter();
    interpreter = new BarcodeDataNav(colorInterpreter);
  }

  public BarcodeModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  public void setModel(Model model) {
    super.setModel(model);
    colorInterpreter.setModel(model);
  }

  @Override
  protected void work() {
    Buffer tempBuffer = this.model.getLightValueBuffer();
    model.setBarcode(Barcode.None); //TODO: incorporate in navigator  
    model.setLine(Line.NONE);
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
          if (brownCounter > 5 && tempBuffer.getCheckpointSize() < 3) {
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
        //System.out.println("Buffersize: " + tempBuffer.getBufferSubset(END_OF_BARCODE_BROWN_COUNT).size());
        int barcode = interpreter.translate(tempBuffer.getBufferSubset(END_OF_BARCODE_BROWN_COUNT));
        //System.out.println("Barcode: " + barcode);
        state = WAITING;
        tempBuffer.unsetCheckPoint();

        int corrected = barcode;
        if (barcode != Barcode.None) {
          corrected = interpreter.correct(barcode);
        }
        if (corrected == 0) {
          model.setLine(Line.BLACK);
        } else if (corrected == 15) {
          model.setLine(Line.WHITE);
        } else {
          model.setBarcode(corrected);
        }

    }
  }
}
