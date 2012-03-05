package penoplatinum.navigators;

/**
 * SonarNavigator
 * 
 * This Navigator implementation uses the sonar to avoid obstacles in front
 * of him.
 * 
 * @author: Team Platinum
 */
import penoplatinum.Utils;
import penoplatinum.simulator.GoalDecider;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.OriginalModel;

public class SonarNavigator implements Navigator {

  // a reference to the Model that contains all information about our world
  private OriginalModel model;
  // after giving instructions, we also provide a distance to drive or angle
  // to turn
  private double distance = 0;
  private int angle = 0;

  // we use the model to read out raw sensor values for the sonar :
  // distance and angle
  public SonarNavigator setModel(Model model) {
    this.model = (OriginalModel) model;
    return this;
  }

  public SonarNavigator setControler(GoalDecider controler) {
    return this;
  }

  // keep on going, forever ;-)
  public Boolean reachedGoal() {
    return false;
  }

  public int nextAction() {
    // always finish turning before doing anything else
    if (this.model.isTurning()) {
      return Navigator.NONE;
    }

    // moving can be interrupted by a turn
    // a turn is made when we detect a better direction to a far away place
    // a possible better direction comes from a new set of sonar values
    if (this.model.hasUpdatedSonarValues()) {
      int[] values = this.model.getSonarValues();
      Utils.Log(values[0] + "," + values[1] + "," + values[2] + "," + values[3]);

      // if we're close to a frontal object, avoid with big turn
      if (values[0] < 40 && Math.abs(values[1]) < 45) {
        int diff = (values[3] - values[1] + 360) % 360 - 180;
        this.angle = diff > 0 ? values[1] - 90 : values[1] + 90;
        //System.out.println( "AVOID: -> " + this.angle + "(min: " + values[0] + " / " + values[1] + ")" + "(max: " + values[2] + " / " + values[3] + ")" );
        System.out.println("Avoid");
        return Navigator.TURN;
      }
      // normally we keep try to turn and move in the direction of the farest
      // point that has been detected by the sonar
      // but only when the difference between our current angle and the new
      // one is bigger than 25
      // if( Math.abs(values[3]-this.angle) > 25 ) {
      //   this.angle    = values[3] > 25 ? 25 : values[3];
      //   this.distance = values[2];
      //   System.out.println( "IMPROVE: " + this.angle );
      //   return Navigator.TURN;
      // }
    }

    // if everything else fails ...
    // we keep moving ahead
    return Navigator.MOVE;
  }

  public double getDistance() {
    return 0.05;
  }

  public double getAngle() {
    return this.angle;
  }
}
