package penoplatinum.navigator.mode;

/**
 * NavigatorMode
 *
 * Interface for the implementation of NavigatorModes, which are used to
 * implement a strategy pattern, using the Generic MultiModeNavigator.
 *
 * @author Team Platinum
 */

import java.util.List;

import penoplatinum.navigator.action.NavigatorAction;


public interface NavigatorMode {

  // the Mode indicates when it has reached its personal goal
  public boolean reachedGoal();

  // the Model builds plans to acchieve its goal. a plan consists of a series
  // of NavigatorActions
  public List<NavigatorAction> createNewPlan();

}
