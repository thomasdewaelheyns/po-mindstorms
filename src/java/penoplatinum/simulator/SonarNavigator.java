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

public class SonarNavigator implements Navigator {

  // a reference to the Model that contains all information about our world
  private Model model;
  
  // after giving instructions, we also provide a distance to drive or angle
  // to turn
  private double distance = 0;
  private int    angle = 0;

  // a local cache of the next action
  private int nextInstruction = Navigator.NONE;

  // we use the model to read out raw sensor values for the sonar :
  // distance and angle
  public SonarNavigator setModel(Model model) {
    this.model = model;
    return this;
  }

  // keep on going, forever ;-)
  public Boolean reachedGoal() {
    return false;
  }

  public int nextAction() {
    // always finish moving before doing anything else
    if( this.model.isMoving() ) {
      return Navigator.NONE;
    }
    
    // after turn, start moving forward    
    if( this.nextInstruction == Navigator.MOVE ) {
      this.nextInstruction = Navigator.NONE;
      return Navigator.MOVE;
    }
    
    // after moving forward restart
    if( this.model.hasSweepChanged() ) {
      int[] values = this.model.getSweepValues();
      // normally we keep try to turn and move in the direction of the farest
      // point that has been detected by the sonar
      this.angle    = values[3];
      this.distance = values[2];
      // BUT if straight ahead is closer than 35cm ... TURN AWAY QUICKLY !
      if( values[0] < 35 && Math.abs(values[1]) < 90 ) {
        int diff = ( values[3] - values[1] + 360 ) % 360;
        this.angle = diff > 180 ? -30 : 30;
      }
      this.nextInstruction = Navigator.MOVE;
      return Navigator.TURN;
    }

    // if last sweep is still busy or this took very long and the original
    // sweep restarted, wait.
    return Navigator.NONE;
  }

  public double getDistance() {
    return 5;
  }

  public double getAngle() {
    return this.angle;
  }
}
