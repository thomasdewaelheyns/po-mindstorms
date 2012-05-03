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

import penoplatinum.grid.Agent;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class TurnLeftNavigatorAction implements NavigatorAction {

  GridModelPart grids;

  public TurnLeftNavigatorAction(GridModelPart grids) {
    this.grids = grids;
  }

  public void instruct(Driver driver) {
    ((ManhattanDriver) driver).turnLeft();
  }

  public void complete() {
    Agent myAgent = this.grids.getMyAgent();
    Point myPosition = this.grids.getMyPosition();
    Bearing leftFrom = this.grids.getMyBearing().leftFrom();
    this.grids.getMyGrid().moveTo(myAgent, myPosition, leftFrom);
  }
}
