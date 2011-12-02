package penoplatinum.navigators;

import penoplatinum.Utils;
import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.AlignNotLineAction;
import penoplatinum.actions.AlignPerpendicularAction;
import penoplatinum.actions.DriveForwardAction;
import penoplatinum.actions.MoveAction;
import penoplatinum.actions.StopAction;
import penoplatinum.actions.TurnAction;
import penoplatinum.simulator.Barcode;
import penoplatinum.simulator.GoalDecider;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 * TestNavigator
 * 
 * This Navigator implementation does nothing by itself. It can be controlled 
 * externaly. It can be used to test functionality.
 * 
 * @author: Team Platinum
 */
public class BehaviourNavigator implements Navigator {

  private float distance;
  private float angle;
  private boolean proximityBlocked = false;
  private ActionQueue queue = new ActionQueue();

  /*public static void main(String[] args) {
  String lalala = "-n penoplatinum.navigators.BehaviourNavigator -p 30,30,180";
  SimulationRunner.main(lalala.split(" "));
  }/**/
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

  private void newEvent() {
    queue.clearActionQueue();
    proximityBlocked = false;
  }

  private void processWorldEvents() {
    //Utils.Log(model.getSensorValue(Model.S4) + "");
    if (queue.getCurrentAction().isNonInterruptable()) {
      return;
    }

//
    if(!proximityBlocked){
      checkProximityEvent();
    }
    checkBarcodeEvent();
    checkLineEvent();
    //checkSonarCollisionEvent();
    checkCollisionEvent();
  }

  private double lastBarcodeAngle;
  
  private void checkBarcodeEvent() {
    final int minCorrectionAngle = 10;
    if (model.getBarcode() == Barcode.None) {
      return;
    }
    Utils.Log("EVENT: Barcode");
    newEvent();

    switch (model.getBarcode()) {
      default:
        Utils.Log("Unknown barcode: " + model.getBarcode());
        break;
      case 0:
      case 15:
        break;
      case 3:
        proximityBlocked = true;
        queue.add(new MoveAction(model, 0.150f));
        queue.add(new TurnAction(model, 45));
        queue.add(new MoveAction(model, 0.300f));
        queue.add(new TurnAction(model, 45));
        break;
      case 6:
        proximityBlocked = true;
        queue.add(new MoveAction(model, 0.150f));
        queue.add(new TurnAction(model, -45));
        queue.add(new MoveAction(model, 0.300f));
        queue.add(new TurnAction(model, -45));
        break;

      case 5:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
        queue.add(new MoveAction(model, 0.15f));
        queue.add(new TurnAction(model, 180));
        break;

      case 7: //Wip
        queue.add(new MoveAction(model, 0.60f));
        queue.add(new StopAction(100));
        break;
      case 1:
      case 2:
      case 4:
      case 8:
      case -1:
        break;
    }
    if (queue.getCurrentAction() == null) {
      queue.add(new DriveForwardAction());
    }
  }

  private void checkLineEvent() {  //Dit werkt goed
    if (model.getLine() == Line.NONE) {
      return;
    }
    Utils.Log("EVENT: Line");
    // Line detected
    newEvent();
    int directionMultiplier = model.getLine() == Line.BLACK ? -1 : 1;
    queue.add(new AlignNotLineAction(model, model.getLine() == Line.BLACK).setIsNonInterruptable(true));
    queue.add(new DriveForwardAction());
  }

  private void checkCollisionEvent() {
    if (!model.isStuck()) {
      return;
    }
    Utils.Log("Collision event!");
    newEvent();
    if (model.isStuckLeft() && model.isStuckRight()) {
      queue.add(new MoveAction(model, -0.10f).setIsNonInterruptable(true));
      queue.add(new TurnAction(model, 180).setIsNonInterruptable(true));
    } else if (model.isStuckLeft()) {
      queue.add(new MoveAction(model, -0.10f).setIsNonInterruptable(true));
      queue.add(new TurnAction(model, -80).setIsNonInterruptable(true));
    } else {
      queue.add(new MoveAction(model, -0.10f).setIsNonInterruptable(true));
      queue.add(new TurnAction(model, 80).setIsNonInterruptable(true));
    }
  }

  private void checkProximityEvent() {
    if (numProximityWallWarnings <= OBSTACLE_DETECTION_THRESHOLD || proximityOrientTimeout > 0) {
      return;
    }
    Utils.Log("EVENT: Proximity");
    // Obstacle detected
    int[] sonarValues = model.getSonarValues();

    //sonarValues[3]

    int angle = model.getSensorValue(Model.M3);
    if (Math.abs(angle) > 15) {
      return;
    }

    int diff = (sonarValues[3] - sonarValues[1] + 360) % 360;
    int rotation = angle > 0 ? 10 : -10;

    newEvent();
    proximityOrientTimeout = 0;


    queue.add(new TurnAction(model, rotation));
    //TODO: Create the correct barcode actions
    //      actionQueue.add(new TurnAction(model, -180));

  }

  private void checkSonarCollisionEvent() {
    if (numCollisionWallWarnings <= OBSTACLE_DETECTION_THRESHOLD) {
      return;
    }
    Utils.Log("EVENT: SonarCollision");
    // Obstacle detected
    newEvent();

    queue.add(new MoveAction(model, -.05f).setIsNonInterruptable(true));

    int[] sonarValues = model.getSonarValues();
    //sonarValues[3]
    int angle = model.getSensorValue(Model.M3);
    int targetAngle = 90;


    int diff = (sonarValues[3] - sonarValues[1] + 360) % 360;
    int rotation = diff > 180 ? -5 : 5;

    queue.add(new AlignPerpendicularAction(model).setIsNonInterruptable(true));
    queue.add(new MoveAction(model, .05f).setIsNonInterruptable(true));
    //TODO: Create the correct barcode actions
    //      actionQueue.add(new TurnAction(model, -180));
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
  private static final int PROXIMITY_WALL_AVOID_DISTANCE = 35;
  int numCollisionWallWarnings;
  private static final int COLLISION_WALL_AVOID_DISTANCE = 10;
  final int OBSTACLE_DETECTION_THRESHOLD = 3;
  int proximityOrientTimeout = 0;

  public int nextAction() {

//    Utils.Log(model.getSensorValue(Model.S4)+"");
    updateWallWarnings(); // process sensory data, if more complex should be modelprocessor
    processWorldEvents();


    if (queue.getCurrentAction().isComplete()) {
      queue.dequeue();
    }

    if (queue.getCurrentAction() == null) {
      // Add a driveForward action
      queue.add(new DriveForwardAction());
    }

    return queue.getCurrentAction().getNextAction();
  }

  private void updateWallWarnings() {
    proximityOrientTimeout--;
    if (proximityOrientTimeout < 0) {
      proximityOrientTimeout = 0;
    }
    //if (model.getSensorValue(Model.S3) < PROXIMITY_WALL_AVOID_DISTANCE) {
    if (model.getSensorValue(Model.S3) < 35) {
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
