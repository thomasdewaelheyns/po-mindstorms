package penoplatinum.navigator.mode;

/**
 * DiscoverHillClimbingNavigatorMode
 *
 * Implements a NavigatorMode that uses Hill Climbing to discover all Sectors
 * on a Grid.
 *
 * @author Team Platinum
 */

import penoplatinum.model.Model;

public class FullDiscover extends HillClimbingNavigatorMode {

  public FullDiscover(Model model) {
    super(model);
  }

  @Override
  public void activate() {
    this.grids.applyDiffusionFlags(true, true);
  }
  

  // we reached our goal if there are no unknown sectors in our grid anymore
  public boolean reachedGoal() {
    return false;
    /*
    for(Sector sector : this.grids.getMyGrid().getSectors()) {
      if(! sector.isFullyKnown()) { return false; }
    }
    return false;/**/
  }
}
