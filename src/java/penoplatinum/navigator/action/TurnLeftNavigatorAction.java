package penoplatinum.navigator.action;

/**
 * TurnLeftNavigatorAction
 *
 * Implements a NavigatorAction that turns left.
 *
 * @author Team Platinum
 */

import penoplatinum.driver.Driver;
import penoplatinum.driver.ManhattanDriver;

import penoplatinum.model.part.GridModelPart;


public class TurnLeftNavigatorAction implements NavigatorAction {

  GridModelPart grids;

  public TurnLeftNavigatorAction(GridModelPart grids) {
    this.grids = grids;
  }

  public void instruct(Driver driver) {
    ((ManhattanDriver)driver).turnLeft();
  }
  
  public void complete() {
    this.grids.getMyAgent().turnLeft();
  }
}
