/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import java.util.ArrayList;

/**
 *
 * @author: Team Platinum
 */
public class ActionQueue {

  private int currentActionIndex;
  private ArrayList<BaseAction> actionQueue = new ArrayList<BaseAction>();

  public BaseAction getCurrentAction() {
    // -1 means that the first action has not yet started
    if (currentActionIndex >= actionQueue.size()) {
      return null;
    }

    return actionQueue.get(currentActionIndex);
  }

  public void clearActionQueue() {
    currentActionIndex = 0; // Note: this -1 is a cheat, check getcurrentaction
    actionQueue.clear();
  }

  public void dequeue() {
    currentActionIndex++;
  }

  public void add(BaseAction action) {
    actionQueue.add(action);
  }

  @Override
  public String toString() {
    String ret = "";
    boolean first = true;
    for (int i = currentActionIndex; i < actionQueue.size(); i++) {
      if (!first) {
        ret += "|";
      }
      ret += actionQueue.get(i).toString();
    }
    return ret;
  }
}
