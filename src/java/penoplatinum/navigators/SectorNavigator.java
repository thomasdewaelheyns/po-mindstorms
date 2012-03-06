//package penoplatinum.navigators;
//
//import penoplatinum.Utils;
//import penoplatinum.actions.ActionQueue;
//import penoplatinum.actions.AlignPerpendicularAction;
//import penoplatinum.actions.AlignPerpendicularLine;
//import penoplatinum.actions.DriveForwardAction;
//import penoplatinum.actions.MoveAction;
//import penoplatinum.actions.MoveRandomSectorAction;
//import penoplatinum.actions.StaticSweepAction;
//import penoplatinum.actions.StopAction;
//import penoplatinum.actions.TurnAction;
//import penoplatinum.modelprocessor.ColorInterpreter;
//import penoplatinum.simulator.Barcode;
//import penoplatinum.simulator.GoalDecider;
//import penoplatinum.simulator.Line;
//import penoplatinum.simulator.Model;
//import penoplatinum.simulator.Navigator;
//
///**
// * TestNavigator
// * 
// * This Navigator implementation does nothing by itself. It can be controlled 
// * externaly. It can be used to test functionality.
// * 
// * @author: Team Platinum
// */
//public class SectorNavigator implements Navigator {
//
//  private boolean proximityBlocked = true;
//  private ActionQueue queue = new ActionQueue();
//  ColorInterpreter interpreter;
//  private DriveForwardAction driveForwardAction = new DriveForwardAction(true);
//
//  public SectorNavigator() {
//    // Fill with initial action
//
//    interpreter = new ColorInterpreter();
//
//  }
//  private Model model;
//  private GoalDecider controler = new GoalDecider() {
//
//    public Boolean reachedGoal() {
//      return false;
//    }
//  };
//  boolean first = true;
//
//  public int nextAction() {
//
//    if (first) {
//      first = false;
//      queue.add(new StaticSweepAction(model));
//      queue.add(new StaticSweepAction(model));
//    }
//
//    //Utils.Log(model.getSensorValue(Model.S4)+"");
//    //updateWallWarnings(); // process sensory data, if more complex should be modelprocessor
//    
//    processWorldEvents();
//
//
//    if (queue.getCurrentAction().isComplete()) {
//      queue.dequeue();
//
//    }
////
////    if (queue.getCurrentAction() == null) {
////      performGapMovement();
////    }
//    if (queue.getCurrentAction() == null) {
//      //newEvent("Idle", "", "Drive");
//      Model m = model;
//      //int diff = (m.getWallLeftClosestAngle() + m.getWallRightClosestAngle()) / 2;
//
////      if (Math.abs(diff) > 0 && m.isWallLeft() && m.isWallRight()) {
////        queue.add(new TurnAction(m, -diff).setIsNonInterruptable(true));
////        queue.add(new StaticSweepAction(model));
////      }
//
//      if (m.getWallLeftDistance() < 15 && !m.isWallFront()) {
//        queue.add(new TurnAction(m, -10).setIsNonInterruptable(true));
//
//      } else if (m.getWallRightDistance() < 15 && !m.isWallFront()) {
//        queue.add(new TurnAction(m, 10).setIsNonInterruptable(true));
//
//      }
//
////      if (diff > 5 && m.isWallLeft() && m.isWallRight()) {
////        queue.add(new SideWallCorrectAction(model, diff * 0.01f * 0.5f));
////        queue.add(new StaticSweepAction(model));
////      }
//
//
//
//
//
//      queue.add(new MoveRandomSectorAction(model));
//
//
//
//
//      queue.add(new StaticSweepAction(model));
//    }
//
//
//    //System.out.println(queue.toString());
//
//    return queue.getCurrentAction().getNextAction();
//  }
//
//  private void performGapMovement() {
//    if (!model.isGapFound()) {
//      // No clear gap data, do nothing
//      return;
//    }
//    if (model.isScanningLightData() || !interpreter.isColor(ColorInterpreter.Color.Brown)) {
//      // Do not gap correct when someone is reading light data
//      return;
//    }
//    // gap data! turn to center of gap
//    int centerAngle = (model.getGapEndAngle() + model.getGapStartAngle()) / 2;
//    if (Math.abs(centerAngle) < 20) {
//      return;
//    }
//    newEvent("Gap correction", "Sonar", "Turn to gap");
//    queue.add(new TurnAction(model, centerAngle));
//
//  }
//
//  private void processWorldEvents() {
//    //Utils.Log(model.getSensorValue(Model.S4) + "");
//    if (queue.getCurrentAction().isNonInterruptable()) {
//      return;
//    }
//
////
//    //if (!proximityBlocked) {
//    //  checkProximityEvent();
//    //}
//    //checkBarcodeEvent();
//    checkLineEvent();
//    //checkSonarCollisionEvent();
////    checkCollisionEvent();
//  }
////  private double lastBarcodeAngle;
//
//  private void newEvent(String eventName, String source, String action) {
//    event = eventName;
//    eventSource = source;
//    eventAction = action;
////    Utils.Log("Event: " + eventName);
//    queue.clearActionQueue();
//    proximityBlocked = false;
//  }
//  private static final float barcodeLength = 0.16f;
//  private static final float totalDriveDistance = 0.35f;
//  private static final float initialDriveDistance = 0.2f;
//  private static final int firstRotateAngle = 30;
//  private static final float diagonalDriveDistance = (totalDriveDistance - barcodeLength - initialDriveDistance)
//          / (float) Math.sin(30 / 180f * Math.PI);
//
//  private void checkBarcodeEvent() {
//    if (model.getBarcode() == Barcode.None) {
//      return;
//    }
//
//
//    switch (model.getBarcode()) {
//      default:
//        Utils.Log("Unknown barcode: " + model.getBarcode());
//        return;
//      case 0:
//      case 15:
//        return;
//      case 3:
//        newEvent("Barcode " + model.getBarcode(), "Lightsensor", "Turn left");
//
//        proximityBlocked = true;
//        queue.add(new MoveAction(model, 0.25f));
//        queue.add(new TurnAction(model, 80));
////        queue.add(new MoveAction(model, initialDriveDistance));
////        queue.add(new TurnAction(model, firstRotateAngle));
////        queue.add(new MoveAction(model, diagonalDriveDistance));
////        queue.add(new TurnAction(model, 90 - firstRotateAngle));
//        break;
//      case 6:
//        newEvent("Barcode " + model.getBarcode(), "Lightsensor", "Turn right");
//
//        proximityBlocked = true;
//        queue.add(new MoveAction(model, 0.25f));
//        queue.add(new TurnAction(model, -80));
////        queue.add(new MoveAction(model, initialDriveDistance));
////        queue.add(new TurnAction(model, -firstRotateAngle));
////        queue.add(new MoveAction(model, diagonalDriveDistance));
////        queue.add(new TurnAction(model, -(90 - firstRotateAngle)));
//        break;
//
//      case 5:
//      case 9:
//      case 10:
//      case 11:
//      case 12:
//      case 13:
//      case 14:
//        newEvent("Barcode " + model.getBarcode(), "Lightsensor", "Turn around");
//
//        queue.add(new MoveAction(model, 0.15f));
//        queue.add(new TurnAction(model, 180));
//        queue.add(new MoveAction(model, 0.30f));
//        break;
//
//      case 7: //Wip
//        newEvent("Barcode " + model.getBarcode(), "Lightsensor", "Turn around");
//
//        queue.add(new MoveAction(model, 0.60f));
//        queue.add(new StopAction(100));
//        break;
//      case 1:
//      case 2:
//      case 4:
//      case 8:
//      case -1:
//        return;
//    }
//  }
//
//  private void checkLineEvent() {  //Dit werkt goed
//    if (model.getLine() == Line.NONE) {
//      return;
//    }
//    // Line detected
////    newEvent("Line " + (model.getLine() == Line.BLACK ? "Black" : "White"), "Lightsensor", "Align and evade"); //TODO: maybe
//    queue.clearActionQueue();
//    queue.add(new MoveAction(model, 0.02f));
//    queue.add(new AlignPerpendicularLine(model, true).setIsNonInterruptable(true));
//    queue.add(new MoveAction(model, 0.18f + 0.03f));
//    queue.add(new StaticSweepAction(model));
//    Utils.Log("LINE!!");
//    //Sound.playNote(Sound.PIANO, 440, 500);
////    queue.add(new MoveAction(model, 0.05f).setIsNonInterruptable(true));
////    queue.add(new DriveForwardAction());
//  }
//
//  private void checkCollisionEvent() {
//    if (!model.isStuck()) {
//      return;
//    }
//    newEvent("Collision", "Touchsensor", "Evade");
//    if (model.isStuckLeft() && model.isStuckRight()) {
//      queue.add(new MoveAction(model, -0.10f).setIsNonInterruptable(true));
//      queue.add(new TurnAction(model, 180).setIsNonInterruptable(true));
//    } else if (model.isStuckLeft()) {
//      queue.add(new MoveAction(model, -0.10f).setIsNonInterruptable(true));
//      queue.add(new TurnAction(model, -80).setIsNonInterruptable(true));
//    } else {
//      queue.add(new MoveAction(model, -0.10f).setIsNonInterruptable(true));
//      queue.add(new TurnAction(model, 80).setIsNonInterruptable(true));
//    }
//  }
//
//  private void checkProximityEvent() {
//    if (!interpreter.isColor(ColorInterpreter.Color.Brown)) {
//      // Ignore when on color
//      return;
//    }
//
//
//    if (!model.isLeftObstacle() && !model.isRightObstacle()) {
//      return; // No obstacles!
//    }
//
//    int directionModifier = 0;
//
//    if (model.isLeftObstacle()) {
//      newEvent("Proximity Left", "Sonar", "Avoid");
//      directionModifier = -1;
//    } else if (model.isRightObstacle()) {
//      newEvent("Proximity Right", "Sonar", "Avoid");
//      directionModifier = 1;
//    } else {
//      Utils.Error("Impossible!!!");
//    }
//
//    queue.add(new TurnAction(model, 5 * directionModifier));
//
////    queue.add(new MoveAction(model, 0.4f));
////    queue.add(new TurnAction(model, 20 * -directionModifier));
//
//  }
//
//  private void checkSonarCollisionEvent() {
//    if (numCollisionWallWarnings <= OBSTACLE_DETECTION_THRESHOLD) {
//      return;
//    }
//    // Obstacle detected
//    newEvent("Sonar Collision", "Sonar", "Align perpendicular");
//
//    queue.clearActionQueue();
//    queue.add(new StaticSweepAction(model));
//    if (0 == 0) {
//      return;
//    }
//
//    queue.add(new MoveAction(model, -.05f).setIsNonInterruptable(true));
//
//    int[] sonarValues = model.getSonarValues();
//    //sonarValues[3]
//    int angle = model.getSensorValue(Model.M3);
//    int targetAngle = 90;
//
//
//    int diff = (sonarValues[3] - sonarValues[1] + 360) % 360;
//    int rotation = diff > 180 ? -5 : 5;
//
//    queue.add(new AlignPerpendicularAction(model).setIsNonInterruptable(true));
//    queue.add(new MoveAction(model, .05f).setIsNonInterruptable(true));
//    //TODO: Create the correct barcode actions
//    //      actionQueue.add(new TurnAction(model, -180));
//  }
//
//  public SectorNavigator setModel(Model model) {
//    this.model = model;
//    interpreter.setModel(model);
//    return this;
//  }
//
//  public SectorNavigator setControler(GoalDecider controler) {
//    this.controler = controler;
//    return this;
//  }
//
//  public Boolean reachedGoal() {
//    return this.controler.reachedGoal();
//  }
//  int numCollisionWallWarnings;
//  private static final int COLLISION_WALL_AVOID_DISTANCE = 15;
//  final int OBSTACLE_DETECTION_THRESHOLD = 3;
//  int proximityOrientTimeout = 0;
//
//  private void updateWallWarnings() {
//
//    if (model.getSensorValue(Model.S3) < COLLISION_WALL_AVOID_DISTANCE) {
//      numCollisionWallWarnings++;
//    } else {
//      numCollisionWallWarnings--;
//    }
//
//    if (numCollisionWallWarnings < 0) {
//      numCollisionWallWarnings = 0;
//    }
//  }
//
//  public double getDistance() {
//    return queue.getCurrentAction() == null ? 1 : queue.getCurrentAction().getDistance();
//  }
//
//  public double getAngle() {
//    return queue.getCurrentAction() == null ? 0 : queue.getCurrentAction().getAngle();
//  }
//  String event;
//  String eventSource;
//  String eventAction;
//  StringBuilder builder = new StringBuilder();
//
//  @Override
//  public String toString() {
//
//    String actionQueue = queue.toString();
//
//    String currentAction = "";
//    String currentActionArgument = "";
//
//    if (queue.getCurrentAction() != null) {
//      currentAction = queue.getCurrentAction().getKind();
//      currentActionArgument = queue.getCurrentAction().getArgument();
//      if (currentActionArgument == null) {
//        currentActionArgument = "";
//      }
//    }
//    builder.delete(0, builder.length());
//    builder.append('\"').append(event).append("\",\"").append(eventSource).append("\",\"").append(eventAction).append("\",\"").append(actionQueue).append("\",\"").append(currentAction).append("\",\"").append(currentActionArgument).append('\"');
//    return builder.toString();
//
////    return "\"" + event + "\",\"" + eventSource + "\", \"" + eventAction + "\", \"" + actionQueue + "\", \"" + currentAction + "\", \"" + currentActionArgument + "\"";
//  }
//}