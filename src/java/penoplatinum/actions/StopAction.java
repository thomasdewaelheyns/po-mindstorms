/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.simulator.Navigator;

/**
 *
 * @author MHGameWork
 */
public class StopAction extends BaseAction {

  public StopAction() {
    setDistance(0);
    setAngle(0);
  }

  boolean first = true;
  @Override
  public int getNextAction() {
    first = false;
    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return !first; // Never complete!
  }
}