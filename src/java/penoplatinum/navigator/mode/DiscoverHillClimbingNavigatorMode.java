package penoplatinum.navigator.mode;

/**
 * DiscoverHillClimbingNavigatorMode
 *
 * Implements a NavigatorMode that uses Hill Climbing to discover all Sectors
 * on a Grid.
 *
 * @author Team Platinum
 */

import penoplatinum.grid.GridUtils;
import penoplatinum.grid.Sector;
import penoplatinum.model.Model;

public class DiscoverHillClimbingNavigatorMode extends HillClimbingNavigatorMode {

  public DiscoverHillClimbingNavigatorMode(Model model) {
    super(model);
  }

  @Override
  public void activate() {
    this.grids.applyDiffusionFlags(true, false);
  }
  

  // we reached our goal if there are no unknown sectors in our grid anymore
  public boolean reachedGoal() {
    for(Sector sector : this.grids.getMyGrid().getSectors()) {
      if(!GridUtils.isFullyKnown(sector)) { return false; }
    }
    return true;
  }
}
