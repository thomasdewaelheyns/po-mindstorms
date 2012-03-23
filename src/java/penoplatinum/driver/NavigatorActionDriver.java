package penoplatinum.driver;

import penoplatinum.actions.ActionQueue;
import penoplatinum.actions.BaseAction;
import penoplatinum.actions.StopAction;
import penoplatinum.driverevents.BarcodeEvent;
import penoplatinum.driverevents.DriverEvent;
import penoplatinum.driverevents.LineEvent;
import penoplatinum.driverevents.ProximityCorrectionEvent;
import penoplatinum.driverevents.ToCloseDriverEvent;
import penoplatinum.navigatoractions.AllNavigatorActions;
import penoplatinum.navigatoractions.NavigatorAction;
import penoplatinum.pacman.GhostAction;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.RobotAPI;

public class NavigatorActionDriver implements Driver {

  private ActionQueue queue = new ActionQueue();
  private DriverEvent event = new LineEvent(new BarcodeEvent());

  public NavigatorActionDriver() {
    queue.add(new StopAction());
  }

  private Integer navigatorAction;
  private int driverState;
  private final int INIT = -1;
  private final int STARTING = 0;
  private final int COMPLETE = 1;

  @Override
  public void step() {
    BaseAction a = nextAction();
    switch (a.getNextAction()) {
      case Navigator.MOVE:
        this.api.move(a.getDistance());
        break;
      case Navigator.TURN:
        this.api.turn((int) a.getAngle());
        break;
      case Navigator.STOP:
        this.api.stop();
        break;
      case Navigator.NONE:
      default:
      // do nothing
    }
  }

  public BaseAction nextAction() {
    processWorldEvents();
    if (queue.getCurrentAction().isComplete()) {
      queue.dequeue();
    }
    if (queue.getCurrentAction() == null) {
      onQueueEmpty();
    }
    if (queue.getCurrentAction() == null) {
      throw new RuntimeException("Algoritm error");
    }

    return queue.getCurrentAction();
  }

  private void onQueueEmpty() {
    queue.clearActionQueue();
    if (navigatorAction == null) {
      queue.add(new StopAction());
      return;
    }
    driverState++;
    switch (driverState) {
      case STARTING:
        ToCloseDriverEvent.singleton.checkEvent(model, queue);

        Integer a = navigatorAction;
        if (a == GhostAction.FORWARD) { //TODO change this
          ProximityCorrectionEvent.singleton.checkEvent(model, queue);
        }

        NavigatorAction action = AllNavigatorActions.actions.get(a);
        if (action == null) {
          throw new RuntimeException("Unknown GhostAction");
        } else {
          queue.add(action.getBaseAction(model));
        }

        break;
      case COMPLETE:
        NavigatorAction action2 = AllNavigatorActions.actions.get(navigatorAction);
        if (action2 == null) {
          throw new RuntimeException("Unknown GhostAction");
        } else {
          queue.add(action2.getBaseAction(model));
        }

        navigatorAction = null;
        queue.add(new StopAction());

        break;
      default:
        throw new RuntimeException("Invalid state!!");
    }
  }

  private void processWorldEvents() {
    DriverEvent current = event;
    while(current != null){
      current.checkEvent(model, queue);
      current = current.nextEvent();
    }
  }

  private void abortDriving() {
    navigatorAction = null;
  }

  @Override
  public void perform(int action) {
    navigatorAction = action;
    driverState = INIT;
  }

  @Override
  public boolean isBusy() {
    return navigatorAction != null;
  }
  private Model model;

  @Override
  public Driver useModel(Model model) {
    this.model = model;
    return this;
  }
  private RobotAPI api;

  @Override
  public Driver useRobotAPI(RobotAPI api) {
    this.api = api;
    return this;
  }
}
