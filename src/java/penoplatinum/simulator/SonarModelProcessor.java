package penoplatinum.simulator;

/**
 * FrontPushModelProcessor
 * 
 * Implements a modelProcessor that gathers the information of the SonarSweep
 * 
 * Author: Team Platinum
 */

public class SonarModelProcessor extends ModelProcessor {
  private int maximum;
  private int maxAngle;
  private int minimum;
  private int minAngle;
  
  public SonarModelProcessor() {
    super();
    resetSweep();
  }

  public SonarModelProcessor( ModelProcessor nextProcessor ) {
    super( nextProcessor );
    resetSweep();
  }

  public void work() {
    int currentDistance = this.model.getSensorValue(Model.S3);
    int currentAngle    = this.model.getSensorValue(Model.M3);
    
    // only record within -90 to 90 degrees
    if( Math.abs(currentAngle) < 90 ) {
      // keep track of maximum
      if( currentDistance > this.maximum ) {
        this.maximum  = currentDistance;
        this.maxAngle = currentAngle;
      }
      // keep track of minumum
      if( currentDistance < this.minimum ) {
        this.minimum  = currentDistance;
        this.minAngle = currentAngle;
      }
    }
    
    // report at end-points of sweep
    if( Math.abs(currentAngle) >= 135 && this.minimum != Integer.MAX_VALUE ) {
      this.model.setNewSweep( this.minimum, this.minAngle, 
                              this.maximum, this.maxAngle );
      this.resetSweep();
    }
  }

  private void resetSweep(){
    this.minimum = Integer.MAX_VALUE;
    this.maximum = Integer.MIN_VALUE;
  }
}
