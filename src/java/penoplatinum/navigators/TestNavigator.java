package penoplatinum.navigators;

import penoplatinum.simulator.GoalDecider;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 * TestNavigator
 * 
 * This Navigator implementation does nothing by itself. It can be controlled 
 * externaly. It can be used to test functionality.
 * 
 * Author: Team Platinum
 */

public class TestNavigator implements Navigator {

  private Model model;
  private GoalDecider controler = new GoalDecider() {
    public Boolean reachedGoal() { return true; }
  };

  public TestNavigator setModel(Model model) {
    this.model = model;
    return this;
  }
  
  public TestNavigator setControler(GoalDecider controler) {
    this.controler = controler;
    return this;
  }

  // the external test-runner can determine the goal
  public Boolean reachedGoal() {
    return this.controler.reachedGoal();
  }

  public int nextAction() {
    return Navigator.NONE;
  }

  public double getDistance() {
    return 0;
  }

  public double getAngle() {
    return 0;
  }
}
