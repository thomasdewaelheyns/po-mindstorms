/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.ghost;

import java.util.List;
import penoplatinum.Utils;
import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.AlignPerpendicularLine;
import penoplatinum.actions.GapDetectionRestoreAction;
import penoplatinum.actions.MoveAction;
import penoplatinum.actions.PerformSweepAction;
import penoplatinum.actions.StopAction;
import penoplatinum.actions.TurnAction;
import penoplatinum.modelprocessor.ColorInterpreter;
import penoplatinum.simulator.Barcode;
import penoplatinum.simulator.Line;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.RobotAPI;

/**
 *
 * @author MHGameWork
 */
public class GhostDriver {

  private final RobotAPI api;

  GhostDriver(GhostModel model, RobotAPI api) {
    this.api = api;
    this.model = model;
    interpreter = new ColorInterpreter();
    queue.add(new StopAction());
  }

  boolean isBusy() {
    if (navigatorActions == null) {
      return false;
    }
    return currentNavigatorAction < navigatorActions.size();
  }
  private List<Integer> navigatorActions;
  private int currentNavigatorAction = -1;

  void perform(List<Integer> actions) {
    navigatorActions = actions;
    currentNavigatorAction = -1;
  }

  private void abortDriving() {
    currentNavigatorAction = navigatorActions.size() + 4;// 4 or whathever
  }

  void step() {
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
  ColorInterpreter interpreter;
  private Model model;

  public int nextAction() {
    processWorldEvents();

    if (queue.getCurrentAction().isComplete()) {
      queue.dequeue();

    }
    if (queue.getCurrentAction() == null) {
      onQueueEmpty();
    }

    return queue.getCurrentAction().getNextAction();
  }

  private void onQueueEmpty() {
    if (currentNavigatorAction + 1 >= navigatorActions.size()) {
      currentNavigatorAction++;
      queue.add(new StopAction());
      return;
    }
    currentNavigatorAction++;
    Utils.Log(model.getWallFrontDistance() + "");
    if (model.getWallFrontDistance() < 10) {
      Utils.Log("TOOO CLOOOSE");
      queue.clearActionQueue();
      queue.add(new MoveAction(model, -0.1f));
      queue.add(new PerformSweepAction(api, model));
      queue.add(new GapDetectionRestoreAction(api, model));
      return;

    }


    Integer a = navigatorActions.get(currentNavigatorAction);


    if (a == GhostNavigator.TURNLEFT) {
      queue.add(new TurnAction(model, 90).setIsNonInterruptable(true));
    } else if (a == GhostNavigator.TURNRIGHT) {
      queue.add(new TurnAction(model, -90).setIsNonInterruptable(true));
    } else if (a == GhostNavigator.TURNAROUND) {
      queue.add(new TurnAction(model, 180).setIsNonInterruptable(true));
    } else if (a == GhostNavigator.MOVE) {
      queueProximityCorrectionAction();
      queue.add(new MoveAction(model, 0.4f));
    }

  }

  private void queueProximityCorrectionAction() {
    Model m = model;

    if (m.getWallLeftDistance() < 18 && !m.isWallFront()) {
      queue.add(new TurnAction(m, -20).setIsNonInterruptable(true));

    } else if (m.getWallRightDistance() < 18 && !m.isWallFront()) {
      queue.add(new TurnAction(m, 20).setIsNonInterruptable(true));

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

  private void checkBarcodeEvent() {
    if (model.getBarcode() == Barcode.None) {
      return;
    }
  }

  private void checkLineEvent() {  //Dit werkt goed
    if (model.getLine() == Line.NONE) {
      return;
    }
    // Line detected
    //newEvent("Line " + (model.getLine() == Line.BLACK ? "Black" : "White"), "Lightsensor", "Align and evade"); //TODO: maybe
    queue.clearActionQueue();
    queue.add(new MoveAction(model, 0.02f));
    queue.add(new AlignPerpendicularLine(model, true).setIsNonInterruptable(true));
    queue.add(new MoveAction(model, 0.18f + 0.03f));
    Utils.Log("LINE!!");
  }

  public GhostDriver setModel(Model model) {
    this.model = model;
    interpreter.setModel(model);
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
}
