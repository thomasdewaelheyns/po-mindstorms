package penoplatinum.navigator.mode;

/**
 * DiscoverNavigatorMode
 *
 * Implements a NavigatorMode that uses Hill Climbing to discover all Sectors
 * on a Grid.
 *
 * @author Team Platinum
 */

import java.util.List;
import java.util.Arrays;

import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;

import penoplatinum.navigator.action.NavigatorAction;


public class DiscoverNavigatorMode implements NavigatorMode {

  public DiscoverNavigatorMode(Model model) {
    super(model);
    // TODO : this.grids.onlyApplyCollaborateDiffusionOnUnknownSectors();
  }

  // we reached our goal if there are no unknown sectors in our grid anymore
  public boolean reachedGoal() {
    for(Sector sector : this.grids.getMyGrid().getSectors()) {
      if(! sector.isKnown()) { return false; }
    }
    return true;
  }
}
