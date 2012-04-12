package penoplatinum.navigator.action;

/**
 * NavigatorAction
 *
 * Interface for the implementation of NavigatorActions, which are used to
 * implement a strategy pattern, using a Generic Navigator and Concrete
 * Planner.
 *
 * @author Team Platinum
 */

import penoplatinum.driver.Driver;


public interface NavigatorAction {
  // delegation of the instructing behaviour of the Navigator.
  // this should perform one action on the Driver.
  public void instruct(Driver driver);
  
  // perform all that is needed to complete this action on our side.
  // (e.g.: update internal position information)
  public void complete();
}
