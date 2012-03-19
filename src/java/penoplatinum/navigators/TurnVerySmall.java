package penoplatinum.navigators;

import penoplatinum.simulator.*;
import penoplatinum.Utils;

/*
 * LineFollowerNavigator
 * 
 * This navigator uses the lightsensor to follow lines accross the track
 * 
 * author: Team Platinum
 */
public class TurnVerySmall implements Navigator {

  private Model model;

  public TurnVerySmall setControler(GoalDecider controler) {
    return this;
  }

  // no goal just drive
  public Boolean reachedGoal() {
    return false;
  }
  boolean direction = false;

  public int nextAction() {
    if (!model.isTurning()) {
      return Navigator.TURN;
    }
    return Navigator.NONE;
  }

  @Override
  public double getDistance() {
    return 0;

  }

  @Override
  public double getAngle() {
    return 5;
  }

  @Override
  // the model supplies the lightsensorValues so we can decide to drive or turn
  public TurnVerySmall setModel(Model model) {
    this.model = model;
    return this;
  }
}
