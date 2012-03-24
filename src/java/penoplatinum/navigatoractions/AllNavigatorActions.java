package penoplatinum.navigatoractions;

import java.util.HashMap;
import penoplatinum.pacman.GhostAction;

/**
 * This is a static class to hold all singleton actions in.
 * @author Team Platinum
 */
public class AllNavigatorActions {
  private static final NavigatorAction MOVE_FORWARD = new MoveSectorForwardNavigatorAction();
  private static final NavigatorAction TURN_LEFT = new TurnLeftNavigatorAction();
  private static final NavigatorAction TURN_RIGHT = new TurnRightNavigatorAction();
  private static final NavigatorAction WAIT = new WaitNavigatorAction();
  public static final HashMap<Integer, NavigatorAction> actions = getActionHashMap();

  private static HashMap<Integer, NavigatorAction> getActionHashMap() {
    HashMap<Integer, NavigatorAction> out = new HashMap<Integer, NavigatorAction>();
    out.put(GhostAction.FORWARD, MOVE_FORWARD);
    out.put(GhostAction.TURN_LEFT, TURN_LEFT);
    out.put(GhostAction.TURN_RIGHT, TURN_RIGHT);
    out.put(GhostAction.NONE, WAIT);
    return out;
  }
  
  
}
