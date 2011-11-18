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

  @Override
  public void work() {
    if(Math.abs(this.model.getSensorValue(Model.M3))>90){
      if(Math.abs(this.model.getSensorValue(Model.M3))==135){
        this.model.setNewSweep(minimum, minAngle, maximum, maxAngle);
        resetSweep();
      }
      return;
    }
    if(maximum<this.model.getSensorValue(Model.S3)){
      maximum = this.model.getSensorValue(Model.S3);
      maxAngle = this.model.getSensorValue(Model.M3);
    }
    if(minimum>this.model.getSensorValue(Model.S3)){
      minimum = this.model.getSensorValue(Model.S3);
      minAngle = this.model.getSensorValue(Model.M3);
    }

  }

  private void resetSweep(){
    minimum = Integer.MAX_VALUE;
    maximum = Integer.MIN_VALUE;
  }
}
