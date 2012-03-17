package penoplatinum.modelprocessor;

import penoplatinum.util.LightColor;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.OriginalModel;

public class LightCorruptionModelProcessor extends ModelProcessor {


  public LightCorruptionModelProcessor() {
    super();
  }

  public LightCorruptionModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  public void setModel(Model model) {
    super.setModel(model);
  }
  private int lightDataCorruption = 0;

  @Override
  protected void work() {
    OriginalModel model = (OriginalModel)this.model;
    model.setLine(Line.NONE);
    if (model.isTurning() && model.getCurrentLightColor() != LightColor.Brown) {
      lightDataCorruption = 3;

    }
    if (lightDataCorruption > 0) {
      model.setLightCorruption(true);
      if (model.getCurrentLightColor() ==LightColor.Brown) {
        lightDataCorruption--;
      }
    }
    else
    {
      model.setLightCorruption(false);
    }
  }
}
