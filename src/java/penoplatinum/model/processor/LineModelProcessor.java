package penoplatinum.model.processor;

import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.SensorModelPart;

import penoplatinum.util.LightColor;


public class LineModelProcessor extends ModelProcessor {
  // boilerplate decorator setup
  public LineModelProcessor() { super();}
  public LineModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  // states
  private static final int WAITING       = 0;
  private static final int RECORDING     = 1;
  private static final int END_CORRUPTED = 2;

  private int sensorUpdate = 0;

  private int brownCounter = 0;
  private int state        = WAITING;
  private int colorCounter = 0;


  protected void work() {
    // we only work with new sensor values...
    if( ! this.newSensorValuesAreAvailable() ) { return; }

    
    LightModelPart  lightPart  = LightModelPart.from(this.getModel());

    lightPart.setLine(Line.NONE);

    if( SensorModelPart.from(this.getModel()).isTurning() ) {
      this.state = END_CORRUPTED;
      return;
    }

    LightColor currentColor = lightPart.getCurrentLightColor();

    switch(this.state) {
      case END_CORRUPTED:
        if( currentColor == LightColor.BROWN) {
          this.brownCounter++;
          if( this.brownCounter > 5 ) { this.state = WAITING; }
        } else {
          this.brownCounter = 0;
        }
        break;
      case WAITING:
        if( currentColor == LightColor.WHITE) {
          this.state = RECORDING;
          this.brownCounter = 0;
          this.colorCounter = 0;
        }
        break;
      case RECORDING:
        if( currentColor == LightColor.BROWN ) {
          this.brownCounter++;
          if( this.brownCounter > 5 && this.colorCounter < 2 ) {
            this.state = WAITING;
          } else if( this.brownCounter > 5 ) {
            lightPart.setLine(Line.WHITE);
            this.state = WAITING;
          }
        } else {
          if( currentColor != LightColor.WHITE) {
            this.state = END_CORRUPTED;
            break;
          }
          this.colorCounter++;
          this.brownCounter = 0;
        }
        break;
    }
  }
  
  private boolean newSensorValuesAreAvailable() {
    int currentSensorUpdate = this.robot.getValuesId();
    if( currentSensorUpdate == this.sensorUpdate ) { return false; }
    this.sensorUpdate = currentSensorUpdate;
    return true;
  }
}
