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
public class DriveForwardAction extends BaseAction {

  public DriveForwardAction() {
    setDistance(1);
    setAngle(0);
  }

  @Override
  public int getNextAction() {
    return Navigator.MOVE;
  }

  @Override
  public boolean isComplete() {
    return false; // Never complete!
  }
}