package penoplatinum.simulator;

/**
 * SonarNavigator
 * 
 * This Navigator implementation uses the sonar to avoid obstacles in front
 * of him.
 * 
 * Author: Team Platinum
 */

import java.util.Random;

public class SonarNavigator1 implements Navigator {  
  private Boolean idle = true;
  private Model   model;
  private int     angle = 0;
  
  // we use the model to read out raw sensor values for the sonar :
  // distance and angle
  public SonarNavigator1 setModel(Model model){
    this.model = model;
    return this;
  }

  // keep on going, forever ;-)
  public Boolean reachedGoal() {
    return false;
  }

  // if the motor of the sonar sensor is currently facing forward/0 degrees:
  // 1) continue moving if the distance is still more than 50
  // 2) if the distance is only 40, turn a little
  // 3) if the distance is only 30, turn more
  // 4) if the distance is only 20, turn 90 degrees
  public int nextAction() {
    // start moving
    if( this.idle ) { 
      this.idle = false;
      return Navigator.MOVE;
    }

    // check is we're blocked in the (near) future and turn away to avoid it
    int direction = model.getSensorValue(Model.M3);
    int distance  = model.getSensorValue(Model.S3);

    // look straight ahead and determine angle to apply to turn away
    if(direction == 0){
      if( distance > 50 ) {
        this.angle = 0;
      } else if( distance > 40 ) {
        this.angle = -20;
      } else if( distance > 20 ) {
        this.angle = 60;
      } else if( distance > 10 ) {
        this.angle = -90;
      } else {
        this.angle = 135;
      }

      // let's introduce some non-determinism:
      // randomly turn left or right
      this.angle *= ( Math.random() < 0.25 ? -1 : 1 );
      if(this.angle == 0){
        return Navigator.MOVE;
      }
      if(model.isMoving()){
        if(model.isTurning()){
          return Navigator.NONE;
        } else {
          return Navigator.STOP;
        }
      } else {
        return Navigator.TURN;
      }
    }


    // if we're driving and we aren't avoiding anything
    return Navigator.NONE;
  }

  public double getDistance() {
    return 0.1;
  }

  public double getAngle() {
    return angle;
  }
}
