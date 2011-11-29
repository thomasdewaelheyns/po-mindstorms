package penoplatinum.navigators;

import penoplatinum.simulator.GoalDecider;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.SimulationRunner;

/**
 * TestNavigator
 * 
 * This Navigator implementation does nothing by itself. It can be controlled 
 * externaly. It can be used to test functionality.
 * 
 * Author: Team Platinum
 */
public class BehaviourNavigator implements Navigator {

  private float distance;
  private float angle;
  private Behaviour currentBehaviour;

  public static void main(String[] args) {
    String lalala = "-n penoplatinum.navigators.BehaviourNavigator -p 20,20,180";
    SimulationRunner.main(lalala.split(" "));

  }
  private Model model;
  private GoalDecider controler = new GoalDecider() {

    public Boolean reachedGoal() {
      return true;
    }
  };

  public BehaviourNavigator setModel(Model model) {
    this.model = model;
    return this;
  }

  public BehaviourNavigator setControler(GoalDecider controler) {
    this.controler = controler;
    return this;
  }

  // the external test-runner can determine the goal
  public Boolean reachedGoal() {
    return this.controler.reachedGoal();
  }

  public int nextAction() {

    updatePriorities();
    currentBehaviour = getHighestPriority();

    performBehaviourAction(Behaviours.NONE);

    return Navigator.NONE;
  }

  private Behaviour getHighestPriority() {
  }

  private void updatePriorities() {
    sortPriorities();
    //TODO
  }

  private void sortPriorities() {
  }

  private void performBehaviourAction(Behaviours b) {
  }

  public double getDistance() {
    return currentBehaviour == null ? 0 : currentBehaviour.getNavigator().getDistance();
  }

  public double getAngle() {
    return currentBehaviour == null ? 0 : currentBehaviour.getNavigator().getAngle();
  }

  enum Behaviours {

    NONE, BARCODE, LINE, WALL, FORWARD
  }

  class Behaviour {

    private int priority;
    private Navigator navigator;

    public Behaviour(Navigator navigator) {
      this.navigator = navigator;
    }

    public Navigator getNavigator() {
      return navigator;
    }

    public int getPriority() {
      return priority;
    }

    public void setPriority(int priority) {
      this.priority = priority;
    }
  }
}
