package penoplatinum.navigators;

import java.util.ArrayList;
import penoplatinum.Utils;
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

  // the external test-runner can determine the goal
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

  private void processWorldEvents() {
    checkLineEvent();
    checkObstacleEvent();
    checkBarcodeEvent();
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

  class ActionQueue {

    private int currentActionIndex;
    private ArrayList<BaseAction> actionQueue = new ArrayList<BaseAction>();

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

    public void clearActionQueue() {
      currentActionIndex = -1; // Note: this -1 is a cheat, check getcurrentaction
      actionQueue.clear();
    }

    private void moveToNextAction() {
      getCurrentAction().end();
      currentActionIndex++;
      if (getCurrentAction() == null) {
        Utils.Error("Can't move to next action, queue is empty!!!");
      }
      getCurrentAction().start();
    }

    public void add(BaseAction action) {
      actionQueue.add(action);
    }

    /**
     * This performs one eventloop step in the queue
     * @return 
     */
    public int nextNavigatorAction() {
      if (getCurrentAction() == null) {
        Utils.Error("Action Queue is empty!!!");
      }
      if (getCurrentAction().isComplete()) {
        moveToNextAction();
      }
      return getCurrentAction().getNextAction();
    }
  }

  abstract class BaseAction {

    private Model model;
    private boolean isNonInterruptable;

    public boolean isNonInterruptable() {
      return isNonInterruptable;
    }

    public BaseAction setIsNonInterruptable(boolean isNonInterruptable) {
      this.isNonInterruptable = isNonInterruptable;
      return this;
    }

    public BaseAction() {
      this.model = model;
    }

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

  class DriveForwardAction extends BaseAction {

    public DriveForwardAction() {
      setDistance(1);
      setAngle(0);
    }

    @Override
    public int getNextAction() {
      return Navigator.MOVE;
    }

    @Override
    public boolean isComplete() {
      return false; // Never complete!
    }
  }
}
