package penoplatinum.navigators;

import java.util.ArrayList;
import penoplatinum.simulator.Barcode;
import penoplatinum.simulator.GoalDecider;
import penoplatinum.simulator.Line;
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
  private int currentActionIndex;
  private ArrayList<BaseAction> actionQueue = new ArrayList<BaseAction>();

  public static void main(String[] args) {
    String lalala = "-n penoplatinum.navigators.BehaviourNavigator -p 30,30,180";
    SimulationRunner.main(lalala.split(" "));
  }

  public BehaviourNavigator() {
  }
  private Model model;
  private GoalDecider controler = new GoalDecider() {

    public Boolean reachedGoal() {
      return false;
    }
  };

  private BaseAction getCurrentAction() {
    // -1 means that the first action has not yet started

    if (currentActionIndex >= actionQueue.size()) {
      return null;
    }

    if (currentActionIndex == -1) {
      currentActionIndex++;

      if (currentActionIndex >= actionQueue.size()) {
        return null;
      }
      actionQueue.get(currentActionIndex).start();
    }
    return actionQueue.get(currentActionIndex);
  }

  private void clearActionQueue() {
    currentActionIndex = -1; // Note: this -1 is a cheat, check getcurrentaction
    actionQueue.clear();
  }

  private void moveToNextAction() {
    getCurrentAction().end();
    currentActionIndex++;
    if (getCurrentAction() == null) {
      return;
    }
    getCurrentAction().start();
  }

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
  int numWallWarnings;
  private static final int WALL_AVOID_DISTANCE = 30;

  public int nextAction() {

    updateWallWarnings();

    if (model.getLine() != Line.NONE ) {

      // Line detected
      clearActionQueue();
      if (model.getLine() == Line.BLACK) {
        actionQueue.add(new MoveAction(model, -0.10f));
        actionQueue.add(new TurnAction(model, -30));
      }
      if (model.getLine() == Line.WHITE) {
        actionQueue.add(new MoveAction(model, -0.10f));
        actionQueue.add(new TurnAction(model, 30));
      }
      //TODO: line timeout?
    }
    if (numWallWarnings > 3 ) {
      // Obstacle detected
      clearActionQueue();
      int[] sonarValues = model.getSonarValues();
      //sonarValues[3]
      int angle = model.getSensorValue(Model.M3);
      int targetAngle = 90;

      int diff = (sonarValues[3] - sonarValues[1] + 360) % 360;
      int rotation = diff > 180 ? -5 : 5;

      actionQueue.add(new TurnAction(model, rotation));
      //TODO: Create the correct barcode actions
      //      actionQueue.add(new TurnAction(model, -180));

    }
    if (model.getBarcode() != Barcode.None && false) {
      // Barcode detected
      clearActionQueue();
      //TODO: Create the correct barcode actions
//      actionQueue.add(new TurnAction(model, -180));

    }

    if (getCurrentAction() != null && getCurrentAction().isComplete()) {
      moveToNextAction();
    }

    if (getCurrentAction() != null) {
      return getCurrentAction().getNextAction();
    }


    return Navigator.MOVE;
  }

  private void updateWallWarnings() {
    if (model.getSensorValue(Model.S3) < WALL_AVOID_DISTANCE) {
      numWallWarnings++;
    } else {
      numWallWarnings--;
    }
    if (numWallWarnings < 0) {
      numWallWarnings = 0;
    }
  }

//  private void sortPriorities() {
//    for (int i = 0; i < behaviours.size(); i++) {
//      if (i == 0) {
//        continue;
//      }
//
//      if (behaviours.get(i - 1).getPriority() < behaviours.get(i).getPriority()) {
//        Behaviour swap = behaviours.get(i);
//        behaviours.set(i, behaviours.get(i - 1));
//        behaviours.set(i - 1, swap);
//        i--;
//      } else {
//        i++;
//      }
//    }
//
//  }
  public double getDistance() {
    return getCurrentAction() == null ? 1 : getCurrentAction().getDistance();
  }

  public double getAngle() {
    return getCurrentAction() == null ? 0 : getCurrentAction().getAngle();
  }

  enum Behaviours {

    NONE, BARCODE, LINE, WALL, FORWARD
  }

  abstract class BaseAction {

    private Model model;

    public BaseAction(Model model) {
      this.model = model;
    }
    private float distance;
    private int angle;

    public void start() {
    }

    public void end() {
    }

    public abstract int getNextAction();

    public abstract boolean isComplete();

    public int getAngle() {
      return angle;
    }

    protected void setAngle(int angle) {
      this.angle = angle;
    }

    public float getDistance() {
      return distance;
    }

    protected void setDistance(float distance) {
      this.distance = distance;
    }

    protected Model getModel() {
      return model;
    }
  }

  class MoveAction extends BaseAction {

    public MoveAction(Model m, float distance) {
      super(m);
      setDistance(distance);
      setAngle(0);
    }
    private boolean first = true;

    @Override
    public int getNextAction() {
      if (first) {
        first = false;
        return Navigator.MOVE;
      }

      return Navigator.NONE;
    }

    @Override
    public boolean isComplete() {
      return !model.isMoving() && !first;

    }
  }

  class TurnAction extends BaseAction {

    public TurnAction(Model m, int angle) {
      super(m);
      setDistance(0);
      setAngle(angle);
    }
    private boolean first = true;

    @Override
    public int getNextAction() {
      if (first) {
        first = false;
        return Navigator.TURN;
      }


      return Navigator.NONE;
    }

    @Override
    public boolean isComplete() {
      return !model.isMoving() && !first;
    }
  }
}
