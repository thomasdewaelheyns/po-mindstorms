package penoplatinum.navigators;

import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.DriveForwardAction;
import penoplatinum.actions.MoveAction;
import penoplatinum.actions.TurnAction;
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
  private ActionQueue queue = new ActionQueue();

  public static void main(String[] args) {
    String lalala = "-n penoplatinum.navigators.BehaviourNavigator -p 30,30,180";
    SimulationRunner.main(lalala.split(" "));
  }
  public BehaviourNavigator() {
    // Fill with initial action

    queue.add(new DriveForwardAction());

  }
  private Model model;
  private GoalDecider controler = new GoalDecider() {

    public Boolean reachedGoal() {
      return false;
    }
  };

  private void processWorldEvents() {
    checkLineEvent();
    checkObstacleEvent();
    checkBarcodeEvent();
  }

  private void checkBarcodeEvent() {
    if (0 == 0) {
      return;
    }

    if (model.getBarcode() != Barcode.None) {
      // Barcode detected
      queue.clearActionQueue();

      //TODO: Create the correct barcode actions
//      actionQueue.add(new TurnAction(model, -180));

      queue.add(new DriveForwardAction());
    }
  }

  private void checkLineEvent() {
    if (model.getLine() != Line.NONE) {

      // Line detected
      queue.clearActionQueue();
      if (model.getLine() == Line.BLACK) {
        queue.add(new MoveAction(model, -0.10f));
        queue.add(new TurnAction(model, -30));
      }
      if (model.getLine() == Line.WHITE) {
        queue.add(new MoveAction(model, -0.10f));
        queue.add(new TurnAction(model, 30));
      }

      queue.add(new DriveForwardAction());

      //TODO: line timeout?
    }
  }

  private void checkObstacleEvent() {
    if (numWallWarnings > 3) {
      // Obstacle detected
      queue.clearActionQueue();
      int[] sonarValues = model.getSonarValues();
      //sonarValues[3]
      int angle = model.getSensorValue(Model.M3);
      int targetAngle = 90;

      int diff = (sonarValues[3] - sonarValues[1] + 360) % 360;
      int rotation = diff > 180 ? -5 : 5;

      queue.add(new TurnAction(model, rotation));
      //TODO: Create the correct barcode actions
      //      actionQueue.add(new TurnAction(model, -180));

      queue.add(new DriveForwardAction());

    }
  }

  public BehaviourNavigator setModel(Model model) {
    this.model = model;
    return this;
  }

  public BehaviourNavigator setControler(GoalDecider controler) {
    this.controler = controler;
    return this;
  }

  public Boolean reachedGoal() {
    return this.controler.reachedGoal();
  }
  int numWallWarnings;
  private static final int WALL_AVOID_DISTANCE = 30;

  public int nextAction() {

    updateWallWarnings(); // process sensory data, if more complex should be modelprocessor
    processWorldEvents();

    return queue.nextNavigatorAction();
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

  public double getDistance() {
    return queue.getCurrentAction() == null ? 1 : queue.getCurrentAction().getDistance();
  }
  public double getAngle() {
    return queue.getCurrentAction() == null ? 0 : queue.getCurrentAction().getAngle();
  }
}
