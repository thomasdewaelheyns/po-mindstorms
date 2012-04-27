package penoplatinum.model.processor;

/**
 * LightModelProcessor
 * 
 * Responsible for detecting lines in the lightsensor's data
 * 
 * @author Team Platinum
 */

import penoplatinum.model.Model;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.SensorModelPart;

import penoplatinum.util.LightColor;


public class LightModelProcessor extends ModelProcessor {
  // boilerplate Decorator setup
  public LightModelProcessor() { super(); }
  public LightModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  private final int   BROWN_LOW  = 470;
  private final int   BROWN_HIGH = 540;
  private final float BROWN_MID  = (BROWN_HIGH + BROWN_LOW) / 2;

  private final float AVERAGE_EXPONENT = 0.001f;
  private final int   SENSOR_VARIATION = 30;

  private SensorModelPart  robot;
  private LightModelPart   sensors;

  private int sensorUpdate = 0;

  // override the setModel to setup a reference to the BarcodeModelPart
  public void setModel(Model model) {
    super.setModel(model);
    this.robot   = SensorModelPart.from(this.getModel());
    this.sensors = LightModelPart.from(this.getModel());
  }

  protected void work() {
    // we only work with new sensor values...
    if( this.robot.getValuesId() == this.sensorUpdate ) { return; }

    int currentLightValue = this.sensors.getCurrentLightValue();

    float averageLightValue = this.detectAverageLightValue(currentLightValue);
    this.sensors.setAverageLightValue(averageLightValue);

    LightColor color = this.detectColor(currentLightValue, averageLightValue);
    this.sensors.setCurrentLightColor(color);
  }
  
  private float detectAverageLightValue(int currentLightValue) {
    float averageLightValue    = this.sensors.getAverageLightValue();

    // if the average light value is outside the brown space, reset it to
    // the center in between the boundaries
    if( averageLightValue < BROWN_LOW || averageLightValue > BROWN_HIGH ) {
      averageLightValue = BROWN_MID;
    }

    // if the currentLightValue is in the brown space, we enhance our idea
    // of brown by taking into account the current light value
    if( currentLightValue > BROWN_LOW && currentLightValue < BROWN_HIGH) {
      averageLightValue = averageLightValue  * (1 - AVERAGE_EXPONENT) 
                          + (currentLightValue * AVERAGE_EXPONENT);
    }

    return averageLightValue;
  }
  
  private LightColor detectColor(int   currentLightValue, 
                                 float averageLightValue)
  {
    // if the distance from the averageLightValue is outside the variation
    // of the sensor ... detect a new color if outside 
    if( Math.abs(currentLightValue - averageLightValue) > SENSOR_VARIATION ) {
      if( currentLightValue < averageLightValue ) {
        return LightColor.BLACK;
      } else if( currentLightValue > averageLightValue ) {
        return LightColor.WHITE;
      }
    }

    return LightColor.BROWN;
  }
}
