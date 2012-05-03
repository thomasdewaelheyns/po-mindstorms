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

import penoplatinum.grid.Agent;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Position;

public class ForwardNavigatorAction implements NavigatorAction {

  GridModelPart grids;

  public ForwardNavigatorAction(GridModelPart grids) {
    this.grids = grids;
  }

  public void instruct(Driver driver) {
    ((ManhattanDriver) driver).move();
  }

  public void complete() {
    Agent myAgent = this.grids.getMyAgent();
    Bearing b = this.grids.getMyBearing();
    Point myPosition = new Point(Position.moveLeft(b, this.grids.getMyPosition().getX()), Position.moveTop(b, this.grids.getMyPosition().getY()));

    this.grids.getMyGrid().moveTo(myAgent, myPosition, b);
  }
}
