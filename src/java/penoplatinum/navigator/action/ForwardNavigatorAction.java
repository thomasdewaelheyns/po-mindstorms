package penoplatinum.navigator.action;

/**
 * ForwardNavigatorAction
 *
 * Implements a NavigatorAction that moves one sector forward.
 *
 * @author Team Platinum
 */

import penoplatinum.driver.Driver;
import penoplatinum.driver.ManhattanDriver;

import penoplatinum.model.part.GridModelPart;



public class ForwardNavigatorAction implements NavigatorAction {

  GridModelPart grids;

  public ForwardNavigatorAction(GridModelPart grids) {
    this.grids = grids;
  }

  public void instruct(Driver driver) {
    ((ManhattanDriver)driver).move();
  }
  
  public void complete() {
    this.grids.getMyAgent().moveForward();
  }
}
