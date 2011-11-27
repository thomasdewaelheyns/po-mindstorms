package penoplatinum.simulator;

/**
 * FrontPushModelProcessor
 * 
 * Implements a modelProcessor that gathers the information of the SonarSweep
 * It records a complete sweep and at the end, pushes the information to the
 * model.
 * 
 * Author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

public class SonarModelProcessor extends ModelProcessor {
  private Boolean direction;
  private int prevAngle     = 0;
  
  private List<Integer> distances = new ArrayList<Integer>();
  private List<Integer> blurred   = new ArrayList<Integer>();
  private List<Integer> angles    = new ArrayList<Integer>();
  private int blurFactor = 1;
  
  public void work() {
    // if we changed direction
    if( this.changedDirection() ) {
      this.direction = this.getDirection();
      this.applyBlur();
      this.reportExtrema();
      // push a copy of the info to model
      this.model.updateDistances( new ArrayList<Integer>(this.distances),
                                  new ArrayList<Integer>(this.angles) );
      // prepare for next sweep
      this.distances.clear();
      this.angles.clear();
    }
    // now record the new ping
    this.record();
    
    this.prevAngle = this.getAngle();
  }
  
  private Boolean changedDirection() {
    if( this.direction == null ) { this.direction = this.getDirection(); }
    return this.direction != this.getDirection();
  }

  // true  = -135 -> 135
  // false = -135 <- 135
  private Boolean getDirection() {
    return this.getAngle() > this.prevAngle;
  }

  private void record() {
    int distance = this.getDistance();
    int angle    = this.getAngle();
    this.distances.add(distance);
    this.angles.add(angle);
  }
  
  private int getDistance() {
    return this.model.getSensorValue(Model.S3);
  }

  private int getAngle() {
    return this.model.getSensorValue(Model.M3);
  }
  
  private void applyBlur() {
    this.blurred.clear();
    for( int i=0; i<this.distances.size() - this.blurFactor; i++ ) {
      int sum = 0;
      for( int s=0;s<this.blurFactor; s++ ){
        sum += this.distances.get(i+s);
      }
      this.blurred.add(sum/this.blurFactor);
    }
  }
  
  private void reportExtrema() {
    int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
    int minIdx = -1, maxIdx = -1;
    
    for( int i=0; i<this.blurred.size(); i++ ) {
      int value = this.blurred.get(i);
      if( value < min ) { min = value; minIdx = i; }
      if( value > max ) { max = value; maxIdx = i; }
    }
    
    // System.out.println("sweep :"  );
    // System.out.println( this.blurred );
    // System.out.println( this.angles );
    // System.out.println( " == " + min + "(" + this.angles.get(minIdx) + ") / "+
    //                              max + "(" + this.angles.get(maxIdx) + ")" );
    
    this.model.setNewSweep( min, this.angles.get(minIdx),
                            max, this.angles.get(maxIdx) );
  }

}
