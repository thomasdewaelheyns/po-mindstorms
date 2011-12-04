/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
     if (model.isTurning()) {

      state = WAITING;
    }
    switch (state) {
      case WAITING:
        if (!colorInterpreter.isColor(Color.Brown)) {
          readingColor = colorInterpreter.getCurrentColor();
          //System.out.println("RECORD LINE");
          state = RECORDING;
          brownCounter = 0;
          colorCounter = 0;
        }
        break;
      case RECORDING:
        if (colorInterpreter.isColor(Color.Brown)) {
          brownCounter++;
          if (brownCounter > 5 && colorCounter < 3) {
            state = WAITING;
          } else if (brownCounter > 5 && colorCounter >= 25) {
            state = WAITING;
          } else if (brownCounter > 10) {
            state = INTERPRET;
            System.out.println("INTERPRET"+colorCounter);
          }
        } else {
          colorCounter++;
          brownCounter = 0;
        }
        break;
      case INTERPRET:
        state = WAITING;
        System.out.println(readingColor);
        if (readingColor.equals(Color.Black)) {
          model.setLine(Line.BLACK);
        } else {
          model.setLine(Line.WHITE);
        }
        break;
    }
  }
}
