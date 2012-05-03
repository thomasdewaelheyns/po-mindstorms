package penoplatinum.navigator.action;

/**
 * TurnRightNavigatorAction
 *
 * Implements a NavigatorAction that turns right.
 *
 * @author Team Platinum
 */
import penoplatinum.driver.Driver;
import penoplatinum.driver.ManhattanDriver;

import penoplatinum.model.part.GridModelPart;

public class TurnRightNavigatorAction implements NavigatorAction {

  GridModelPart grids;

  public TurnRightNavigatorAction(GridModelPart grids) {
    this.grids = grids;
  }

  public void instruct(Driver driver) {
    ((ManhattanDriver) driver).turnRight();
  }

  public void complete() {
    this.grids.getMyGrid().moveTo(this.grids.getMyAgent(), this.grids.getMyPosition(), this.grids.getMyBearing().rightFrom());
  }
}
