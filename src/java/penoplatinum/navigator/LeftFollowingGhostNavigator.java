/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

import penoplatinum.model.GhostModel;
import java.util.ArrayList;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 *
 * @author MHGameWork
 */
public class LeftFollowingGhostNavigator implements Navigator {

  private GhostModel m;
  private boolean forwardQueued = false;

  public LeftFollowingGhostNavigator() {
  }
  private ArrayList<Integer> outputBuffer = new ArrayList<Integer>();

  @Override
  public Navigator setModel(Model model) {
    this.m = (GhostModel) model;
    return this;
  }

  @Override
  public int nextAction() {
    if (forwardQueued) {
      forwardQueued = false;
      return GhostAction.FORWARD;
    }
    if (!m.getWallsPart().isWallLeft()) {
      forwardQueued = true;
      return GhostAction.TURN_LEFT;
    } else if (!m.getWallsPart().isWallFront()) {
      return GhostAction.FORWARD;

    } else if (!m.getWallsPart().isWallRight()) {
      forwardQueued = true;

      return GhostAction.TURN_RIGHT;
    } else {
      return GhostAction.TURN_LEFT;
    }
  }

  @Override
  public double getDistance() {
    return 0;
  }

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public Boolean reachedGoal() {
    return false;
  }
}
