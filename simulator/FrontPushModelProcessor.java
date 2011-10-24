/**
 * FrontPushModelProcessor
 * 
 * Implements a ModelProcessor that detects frontpushes based on a central
 * front pushsensor.
 * 
 * Author: Team Platinum
 */

public class FrontPushModelProcessor extends ModelProcessor {
  
  public FrontPushModelProcessor() {
    super();
  }

  public FrontPushModelProcessor( ModelProcessor nextProcessor ) {
    super( nextProcessor );
  }

  public void work() {
    int value = this.model.getSensorValue(Model.S1); // front push sensor
    // value above 25, means it was pushed
    if( value > 25 ) {
      this.model.markStuck();
    } else if( this.model.isStuck() ) {
      this.model.markNotStuck();
    }
  }

}
