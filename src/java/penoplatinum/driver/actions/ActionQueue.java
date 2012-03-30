package penoplatinum.actions;

/**
 * This class implements a queue for actions
 * 
 * @author: Team Platinum
 */

import java.util.ArrayList;


public class ActionQueue {

  private int currentActionIndex;
  private ArrayList<BaseAction> actionQueue = new ArrayList<BaseAction>();


  public BaseAction getCurrentAction() {
    // -1 means that the first action has not yet started
    if( currentActionIndex >= actionQueue.size() ) {
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
      first = false;
      ret += actionQueue.get(i).toString();
    }
    return ret;
  }
}
