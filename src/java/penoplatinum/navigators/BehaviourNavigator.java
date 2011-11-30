package penoplatinum.navigators;

import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.AlignPerpendicularAction;
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
    if (queue.getCurrentAction().isNonInterruptable()) {
      return;
    }
    //checkLineEvent();
    //checkProximityEvent();
    //checkBarcodeEvent();
    checkSonarCollisionEvent();
    
  }
  
  private void checkBarcodeEvent() {
    if (model.getBarcode() != Barcode.None) {
      // Barcode detected
      queue.clearActionQueue();

      //TODO: Create the correct barcode actions
//      actionQueue.add(new TurnAction(model, -180));
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
      
      //TODO: line timeout?
    }
  }
  
  private void checkProximityEvent() {
    if (numProximityWallWarnings > OBSTACLE_DETECTION_THRESHOLD) {
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
    }
  }
  
  private void checkSonarCollisionEvent() {
    if (numCollisionWallWarnings > OBSTACLE_DETECTION_THRESHOLD) {
      // Obstacle detected
      queue.clearActionQueue();
      
      queue.add(new MoveAction(model, -.05f).setIsNonInterruptable(true));
      
      int[] sonarValues = model.getSonarValues();
      //sonarValues[3]
      int angle = model.getSensorValue(Model.M3);
      int targetAngle = 90;
      
      int diff = (sonarValues[3] - sonarValues[1] + 360) % 360;
      int rotation = diff > 180 ? -5 : 5;
      
      queue.add(new AlignPerpendicularAction(model).setIsNonInterruptable(true));
      //TODO: Create the correct barcode actions
      //      actionQueue.add(new TurnAction(model, -180));
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
  int numProximityWallWarnings;
  private static final int PROXIMITY_WALL_AVOID_DISTANCE = 30;
  int numCollisionWallWarnings;
  private static final int COLLISION_WALL_AVOID_DISTANCE = 15;
  final int OBSTACLE_DETECTION_THRESHOLD = 3;
  
  public int nextAction() {
    
    updateWallWarnings(); // process sensory data, if more complex should be modelprocessor
    processWorldEvents();
    
    
    if (queue.getCurrentAction().isComplete())    
      queue.dequeue();
    
    if (queue.getCurrentAction() == null) {
      // Add a driveForward action
      queue.add(new DriveForwardAction());
    }
    
    
    return queue.getCurrentAction().getNextAction();
  }
  
  private void updateWallWarnings() {
    if (model.getSensorValue(Model.S3) < PROXIMITY_WALL_AVOID_DISTANCE) {
      numProximityWallWarnings++;
    } else {
      numProximityWallWarnings--;
    }
    if (model.getSensorValue(Model.S3) < COLLISION_WALL_AVOID_DISTANCE) {
      numCollisionWallWarnings++;
    } else {
      numCollisionWallWarnings--;
    }
    if (numProximityWallWarnings < 0) {
      numProximityWallWarnings = 0;
    }
    if (numCollisionWallWarnings < 0) {
      numCollisionWallWarnings = 0;
    }
  }
  
  public double getDistance() {
    return queue.getCurrentAction() == null ? 1 : queue.getCurrentAction().getDistance();
  }
  
  public double getAngle() {
    return queue.getCurrentAction() == null ? 0 : queue.getCurrentAction().getAngle();
  }
}
