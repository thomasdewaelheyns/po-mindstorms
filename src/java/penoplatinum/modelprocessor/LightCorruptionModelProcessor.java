/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.modelprocessor;

import penoplatinum.modelprocessor.LightColor;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.OriginalModel;

/**
 *
 * @author Thomas
 */
public class LightCorruptionModelProcessor extends ModelProcessor {

  private ColorInterpreter colorInterpreter;

  public LightCorruptionModelProcessor() {
    super();
    this.colorInterpreter = new ColorInterpreter();
  }

  public LightCorruptionModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
    this.colorInterpreter = new ColorInterpreter();
  }

  @Override
  public void setModel(Model model) {
    super.setModel(model);
    colorInterpreter.setModel(model);
  }
  private int lightDataCorruption = 0;

  @Override
  protected void work() {
    OriginalModel model = (OriginalModel)this.model;
    model.setLine(Line.NONE);
    if (model.isTurning() && !colorInterpreter.isColor(LightColor.Brown)) {
      lightDataCorruption = 3;

    }
    if (lightDataCorruption > 0) {
      model.setLightCorruption(true);
      if (colorInterpreter.isColor(LightColor.Brown)) {
        lightDataCorruption--;
      }
    }
    else
    {
      model.setLightCorruption(false);
    }
  }
}
