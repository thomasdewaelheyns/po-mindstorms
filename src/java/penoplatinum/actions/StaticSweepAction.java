/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 *
 * @author MHGameWork
 */
public class StaticSweepAction extends BaseAction {

  private final Model model;

  public StaticSweepAction(Model model) {
    setDistance(0);
    setAngle(0);
    this.model = model;
  }
  int sweepCount = 0;

  @Override
  public int getNextAction() {
    if (model.isSweepComplete()) {
      sweepCount++;
    }
    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return sweepCount >= 2;
  }

  @Override
  public String getKind() {
    return "StaticSweep";
  }

  @Override
  public String getArgument() {
    return "";
  }
}