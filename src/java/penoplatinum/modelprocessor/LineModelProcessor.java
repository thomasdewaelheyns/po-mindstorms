package penoplatinum.modelprocessor;

import penoplatinum.modelprocessor.ColorInterpreter.Color;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;

/**
 *
 * @author Thomas
 */
public class LineModelProcessor extends ModelProcessor {

  private static final int WAITING = 0;
  private static final int RECORDING = 1;
  private static final int INTERPRET = 2;
  private static final int END_BARCODE = 3;
  private int brownCounter = 0;
  private ColorInterpreter colorInterpreter;
  int state = WAITING;
  Color readingColor;
  private int colorCounter = 0;

  /**
   * The constructor in case the LineModelProcessor is the last in a linked list of ModdelProcessors
   * or when there is only one ModdelProcessor.
   */
  public LineModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter();
  }

  /**
   * The constructor for when the LineModelProcessor is not the last element in a
   * linked list with at least 2 ModelProcessors
   * @param nextProcessor 
   *        The next ModelProcessor in the linked list.
   */
  public LineModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
    this.colorInterpreter = new ColorInterpreter();
  }

  /**
   * Sets the model for all ModelProcessors
   * @param model 
   *        The model we are using
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
   * The switch case checks in which case the method is. The different states are:
   * WAITING: the robot waits for a change in light values that could indicate the start
   *          of a barcode or a line.
   * RECORDING: The robot suspects a barcode or line and starts recoding the values.
   *            If the input doesn't meet special requirements, too long or unexpected values,
   *            the recording ends and the state is set back to WAITING.
   *            If the end of the line is detected, the state is set to INTERPRET
   * INTERPRET: The recoded values are a line. In this state the color is determined.
   *            The state is set to WAITING
   * END_BARCODE: Its detected that we are on a barcode. This state detects whether
   *              the barcode is still ongoing or ended. If the barcode has ended,
   *              we set the state to WAITING.
   * 
   * 
   */
  @Override
  protected void work() {
    model.setLine(Line.NONE);
//    if (model.isLightDataCorrupt()) {
//      state = WAITING;
//    }
    
    switch (state) {
      case END_BARCODE:
        if (colorInterpreter.isColor(Color.Brown)) {
          brownCounter++;
          if(brownCounter >5){
            state = WAITING;
          }
        } else {
          brownCounter = 0;
        }
        break;
      case WAITING:
        if (!colorInterpreter.isColor(Color.Brown)) {
          readingColor = colorInterpreter.getCurrentColor();
          //Utils.Log("RECORD LINE");
          state = RECORDING;
          brownCounter = 0;
          colorCounter = 0;
        }
        break;
      case RECORDING:
        if (colorInterpreter.isColor(Color.Brown)) {
          brownCounter++;
          if (brownCounter > 5 && colorCounter < 2) {
            //Utils.Log("False alarm");
            state = WAITING;
//          } else if (brownCounter > 5 && colorCounter >= 15) {
//            Utils.Log("Barcode");
//            state = WAITING;
          } else if (brownCounter > 5) {
            state = INTERPRET;
            //Utils.Log("INTERPRET" + colorCounter);
          }
        } else {
          if (!colorInterpreter.isColor(readingColor))
          {
            state = END_BARCODE;
            break;
          }
          colorCounter++;
          brownCounter = 0;
        }
        break;
      case INTERPRET:
        state = WAITING;
        //Utils.Log(readingColor+"");
        if (readingColor.equals(Color.Black)) {
          model.setLine(Line.BLACK);
        } else {
          model.setLine(Line.WHITE);
        }
        break;
    }
  }
}
