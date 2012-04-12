package penoplatinum.navigator;

/**
 * GhostNavigator
 * 
 * Completes a ManhattanNavigator implementation by adding modes.
 * The GhostNavigator has two modes:
 * 1. discover the entire maze
 * 2. hunt down Pac-man
 * 
 * @author: Team Platinum
 */

import penoplatinum.navigator.mode.DiscoverNavigatorMode;
import penoplatinum.navigator.mode.ChaseNavigatorMode;


public class GhostNavigator extends MultiModeNavigator {

  // as soon as we get a Model, we can setup our Modes
  public useModel(Model model) {
    this.firstUse(new DiscoverNavigatorMode(model));
    this.thenUse (new ChaseNavigatorMode   (model));
  }

}
