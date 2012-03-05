package penoplatinum.modelprocessor;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.OriginalModel;

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
    OriginalModel model = (OriginalModel)this.model;
    boolean left = this.model.getSensorValue(Model.S1) > 25; // front push sensor
    boolean right = this.model.getSensorValue(Model.S2) > 25;
    model.markStuck(left, right);
  }

}
