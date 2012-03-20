package penoplatinum.model.processor;

/**
 * Responsible for filling the LightValueBuffer
 * 
 * @author MHGameWork
 */
public class HistogramModelProcessor extends ModelProcessor {

  public HistogramModelProcessor() {
    super();
  }

  public HistogramModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  protected void work() {
    //int value = this.model.getSensorValue(this.model.S4);
    this.model.getBarcodePart().getLightValueBuffer().insert(model.getLightPart().getCurrentLightColor().getVal());
  }
}
