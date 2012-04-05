package penoplatinum.driver;

/**
 * An implementation for a Ghost-style Robot. It uses a state-like pattern to 
 * implement its logic.
 * 
 * @author Team Platinum
 */

import penoplatinum.robot.Robot;

import penoplatinum.navigator.Navigator;

import penoplatinum.driver.events.*;
import penoplatinum.driver.actions.*;

import penoplatinum.model.Model;
import penoplatinum.model.GhostModel;


public class GhostDriver implements Driver {

  // a reference to the robot we're driving
  private Robot robot;
  
  // we implement a state pattern using the actions the driver can perform
  private DriverAction idleAction;
  private DriverAction turnAction;
  private DriverAction moveAction;
  // a reference to one of the actions above...
  private DriverAction currentAction = new NullDriverAction();


  public Driver drive(Robot robot) {
    this.robot = robot;
    this.setupActions();
    return this;
  }
  
  private void setupActions() {
    this.idleAction = new IdleDriverAction().
  }
  
  // based on the robot we now can access different resources we need:
  private GhostModel getModel() {
    return (GhostModel)this.robot.getModel();
  }
  
  private RobotAPI getRobotAPI() {
    return this.robot.getRobotAPI();
  }
  
  public GhostDriver move(double distance) {
    this.currentAction = moveAction.move(distance);
    return this;
  }
  
  public GhostDriver turn(int angle) {
    this.currentAction = turnAction.turn(angle);
    return this;
  }

  public boolean isBusy() {
    return this.currentAction.isBusy();
  }

  public void step() {
    this.updateCurrentAction();
    this.currentAction().execute(this.getRobotAPI());
  }
    
  private void updateCurrentAction() {
    if( this.currentAction.isInterruptable() ) {
      this.processEvents();
    }

    if( this.currentAction.isComplete() ) {
      this.queue.dequeue();
    }

    if(this.currentAction() == null) {
      this.onQueueEmpty();
      if( queue.getCurrentAction() == null ) {
        throw new RuntimeException("Algoritm error");
      }
    }

    return this.currentAction().getNextAction();
  }
  
  private void processEvents() {
    
  }

  private void onQueueEmpty() {

    if (navigatorAction == null) {
      queue.add(new StopAction());
      return;
    }

    GhostModel model = (GhostModel) this.model;
    driverState++;

    switch (driverState) {
      case STARTING:
        Integer a = navigatorAction;

        if (model.getWallsPart().getWallFrontDistance() < 10) {
          queue.clearActionQueue();
          queue.add(new MoveAction(model, -0.1f));
          //queue.add(new PerformSweepAction(api, model));
          //queue.add(new GapDetectionRestoreAction(api, model));
          return;
        }

        if (a == GhostAction.NONE) {
          queue.add(new StopAction(100));
        } else if (a == GhostAction.TURN_LEFT) {
          queue.add(new TurnAction(model, 90).setIsNonInterruptable(true));
        } else if (a == GhostAction.TURN_RIGHT) {
          queue.add(new TurnAction(model, -90).setIsNonInterruptable(true));
        } else if (a == GhostAction.FORWARD) {
          queueProximityCorrectionAction();
          queue.add(new MoveAction(model, 0.4f));
        } else {
          throw new RuntimeException("Unknown GhostAction");
        }
        break;
      case COMPLETE:
        Integer prevAction = navigatorAction;

        if (prevAction == GhostAction.NONE) {
        } else if (prevAction == GhostAction.TURN_LEFT) {
          model.getGridPart().turnLeft();
        } else if (prevAction == GhostAction.TURN_RIGHT) {
          model.getGridPart().turnRight();
        } else if (prevAction == GhostAction.FORWARD) {
          model.getGridPart().moveForward();
        } else {
          throw new RuntimeException("Unknown GhostAction");
        }

        navigatorAction = null;

        queue.add(new StopAction());
        break;

      default:
        throw new RuntimeException("Invalid state!!");
    }
  }

  private void queueProximityCorrectionAction() {
    GhostModel m = (GhostModel) model;

    if (m.getWallsPart().getWallLeftDistance() < 18 && !m.getWallsPart().isWallFront()) {
      queue.add(new TurnAction(m, -15).setIsNonInterruptable(true));

    } else if (m.getWallsPart().getWallRightDistance() < 18 && !m.getWallsPart().isWallFront()) {
      queue.add(new TurnAction(m, 15).setIsNonInterruptable(true));

    }
  }

  private void processWorldEvents() {
    this.checkBarcodeEvent();
    this.checkLineEvent();
  }

  private void checkBarcodeEvent(){
    if( ! model.getBarcodePart().isReadingBarcode()){ return; }
    // we're reading a barcode, 
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.05f).setIsNonInterruptable(false));
  }
  
  private void checkLineEvent() {
    if( model.getLightPart().getLine() == Line.NONE ) { return; }
    if( model.getBarcodePart().isReadingBarcode())    { return; }

    // we're crossing a line -> start avoiding it
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.02f));
    queue.add(new AlignPerpendicularLine(model, true).setIsNonInterruptable(true));
    queue.add(new MoveAction(model, 0.18f + 0.03f));
  }

  private void newEvent(String eventName, String source, String action) {
    this.event       = eventName;
    this.eventSource = source;
    this.eventAction = action;
    queue.clearActionQueue();
  }


  public double getDistance() {
    return queue.getCurrentAction() == null ? 1 : 
           queue.getCurrentAction().getDistance();
  }

  public double getAngle() {
    return queue.getCurrentAction() == null ? 0 :
           queue.getCurrentAction().getAngle();
  }
  
  String event;
  String eventSource;
  String eventAction;

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    String actionQueue = queue.toString();

    String currentAction = "";
    String currentActionArgument = "";

    if (queue.getCurrentAction() != null) {
      currentAction = queue.getCurrentAction().getKind();
      currentActionArgument = queue.getCurrentAction().getArgument();
      if (currentActionArgument == null) {
        currentActionArgument = "";
      }
    }
    builder.delete(0, builder.length());
    builder.append('\"').append(event).append("\",\"").append(eventSource).append("\",\"").append(eventAction).append("\",\"").append(actionQueue).append("\",\"").append(currentAction).append("\",\"").append(currentActionArgument).append('\"');
    return builder.toString();
  }

}