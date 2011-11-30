/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import java.util.ArrayList;
import penoplatinum.Utils;

/**
 *
 * @author MHGameWork
 */
public class ActionQueue {

  private int currentActionIndex;
  private ArrayList<BaseAction> actionQueue = new ArrayList<BaseAction>();

  public BaseAction getCurrentAction() {
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
