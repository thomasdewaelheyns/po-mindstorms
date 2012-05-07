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

import penoplatinum.model.Model;

import penoplatinum.navigator.mode.DiscoverHillClimbingNavigatorMode;
import penoplatinum.navigator.mode.ChaseHillClimbingNavigatorMode;


public class GhostNavigator extends MultiModeNavigator {

  // as soon as we get a Model, we can setup our Modes
  public GhostNavigator useModel(Model model) {
    this.firstUse(new DiscoverHillClimbingNavigatorMode(model));
    //this.thenUse (new ChaseHillClimbingNavigatorMode   (model));
    return this;
  }

}
