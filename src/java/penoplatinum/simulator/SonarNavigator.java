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

  private Boolean special = true;
  private Model model;
  private double distance = 0;
  private int angle = 0;
  private boolean lastSweepChanged = false;
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
    if(this.model.isMoving()){ //finish movement
      return Navigator.NONE;
    }
    if(nextInstruction == Navigator.MOVE){ //after turn start moving forward
      nextInstruction = Navigator.NONE;
      return Navigator.MOVE;
    }
    
    if(lastSweepChanged != this.model.hasSweepChanged()){ //after moving forward restart
      lastSweepChanged = this.model.hasSweepChanged();
      int[] values = this.model.getSweepValues();
      angle = values[3];
      distance  = values[2];
      if(values[0]<35 && Math.abs(values[1])<90){
        int diff = (values[3]-values[1]+360)%360;
        angle = (diff>180? -30: 30);
        nextInstruction = Navigator.MOVE;
        return Navigator.TURN;
      }
      nextInstruction = Navigator.MOVE;
      return Navigator.TURN;
    } 
    //if last sweep is still busy or this took very long and the original sweep restarted, wait.
    return Navigator.NONE;
  }

  public double getDistance() {
    return 0.1;
  }

  public double getAngle() {
    return angle;
  }
}
