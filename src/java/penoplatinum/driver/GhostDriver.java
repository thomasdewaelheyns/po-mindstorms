/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.driver;

import penoplatinum.util.Utils;
import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.AlignPerpendicularLine;
import penoplatinum.actions.MoveAction;
import penoplatinum.actions.StopAction;
import penoplatinum.actions.TurnAction;
import penoplatinum.pacman.GhostAction;
import penoplatinum.model.GhostModel;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.RobotAPI;

/**
 * 
 * @author PenoPlatinum
 */
public class GhostDriver implements Driver {

  private RobotAPI api;

  public GhostDriver(GhostModel model, RobotAPI api) {
    this.api = api;
    this.model = model;
    queue.add(new StopAction());
  }

  public boolean isBusy() {
    return navigatorAction != null;
  }
  private Integer navigatorAction;
  private int driverState;
  private final int INIT = -1;
  private final int STARTING = 0;
  private final int COMPLETE = 1;

  private void abortDriving() {
    navigatorAction = null;
  }

  public void step() {
    
//    if (model.isReadingBarcode())
//    {
//      api.setSpeed(Model.M1, 250);
//      api.setSpeed(Model.M2, 250);
//    }
//    else
//    {
//      api.setSpeed(Model.M1, 500);
//      api.setSpeed(Model.M2, 500);
//    }
    // ask the navigator what to do next
    switch (nextAction()) {
      case Navigator.MOVE:
        this.api.move(this.getDistance());
        break;
      case Navigator.TURN:
        this.api.turn((int) this.getAngle());
        break;
      case Navigator.STOP:
        this.api.stop();
        break;
      case Navigator.NONE:
      default:
      // do nothing
    }
  }
  private ActionQueue queue = new ActionQueue();
  private Model model;

  public int nextAction() {
    processWorldEvents();

    if (queue.getCurrentAction().isComplete()) {
      queue.dequeue();

    }
    if (queue.getCurrentAction() == null) {
      onQueueEmpty();
      if (queue.getCurrentAction() == null) {
        throw new RuntimeException("Algoritm error");
      }
    }

    return queue.getCurrentAction().getNextAction();
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
          Utils.Log("TOOO CLOOOSE");
          queue.clearActionQueue();
          queue.add(new MoveAction(model, -0.1f));
          //queue.add(new PerformSweepAction(api, model));
          //queue.add(new GapDetectionRestoreAction(api, model));
          return;
        }

        if (a == GhostAction.NONE) {
          queue.add(new StopAction(500));
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
    if (queue.getCurrentAction().isNonInterruptable()) {
      return;
    }

    //if (!proximityBlocked) {
    //  checkProximityEvent();
    //}
    //checkBarcodeEvent();
    checkBarcodeEvent();
    checkLineEvent();
    //checkSonarCollisionEvent();
    //checkCollisionEvent();
  }

  private void newEvent(String eventName, String source, String action) {
    event = eventName;
    eventSource = source;
    eventAction = action;
//    Utils.Log("Event: " + eventName);
    queue.clearActionQueue();
  }

  private void checkBarcodeEvent(){
    if(!model.getBarcodePart().isReadingBarcode()){
      return;
    }
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.05f).setIsNonInterruptable(false));
    
  }
  
  private void checkLineEvent() {  //Dit werkt goed
    if (model.getLightPart().getLine() == Line.NONE) {
      return;
    }
    if (model.getBarcodePart().isReadingBarcode()) return;
    // Line detected
    //newEvent("Line " + (model.getLine() == Line.BLACK ? "Black" : "White"), "Lightsensor", "Align and evade"); //TODO: maybe
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.02f));
    queue.add(new AlignPerpendicularLine(model, true).setIsNonInterruptable(true));
    queue.add(new MoveAction(model, 0.18f + 0.03f));
//    Utils.Log("LINE!!");
  }

  public GhostDriver setModel(Model model) {
    this.model = model;
    return this;
  }

  public double getDistance() {
    return queue.getCurrentAction() == null ? 1 : queue.getCurrentAction().getDistance();
  }

  public double getAngle() {
    return queue.getCurrentAction() == null ? 0 : queue.getCurrentAction().getAngle();
  }
  String event;
  String eventSource;
  String eventAction;
  StringBuilder builder = new StringBuilder();

  @Override
  public String toString() {

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

//    return "\"" + event + "\",\"" + eventSource + "\", \"" + eventAction + "\", \"" + actionQueue + "\", \"" + currentAction + "\", \"" + currentActionArgument + "\"";
  }

  @Override
  public Driver useRobotAPI(RobotAPI api) {
    this.api = api;
    return this;
  }

  @Override
  public void perform(int action) {
    navigatorAction = action;
    driverState = INIT;
  }
}