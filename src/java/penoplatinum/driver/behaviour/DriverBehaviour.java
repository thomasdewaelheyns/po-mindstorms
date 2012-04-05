package penoplatinum.driver.behaviour;

/**
 * DriverBehaviour
 * 
 * Allows creation of extensions to a Driver. A behaviour checks the model
 * and current action and decides whether it want to change the current
 * action into something else or more.
 *
 * @author: Team Platinum
 */

import penoplatinum.model.Model;

import penoplatinum.driver.action.DriverAction;


public interface DriverBehaviour {
  // determines if a (new) action is required based on the current situation
  // and action
  public boolean requiresAction(Model model, DriverAction currentAction);

  // provides the new next action
  public DriverAction getNextAction();
}
