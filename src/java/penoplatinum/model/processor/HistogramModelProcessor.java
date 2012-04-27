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
    // we only work with new sensor values...
    // if( this.robot.getValuesId() == this.sensorUpdate ) { return; }

    this.model.getBarcodePart().getLightValueBuffer().insert(model.getLightPart().getCurrentLightColor().getVal());
  }
}
