package penoplatinum.modelprocessor;

import penoplatinum.Utils;
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

  public LineModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter();
  }

  public LineModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
    this.colorInterpreter = new ColorInterpreter();
  }

  @Override
  public void setModel(Model model) {
    super.setModel(model);
    colorInterpreter.setModel(model);
  }

  @Override
  protected void work() {
    model.setLine(Line.NONE);
    if (model.isLightDataCorrupt()) {
      state = WAITING;
    }
    
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
