/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.simulator.Navigator;

/**
 *
 * @author: Team Platinum
 */
public class DriveForwardAction extends BaseAction {

  public static final DriveForwardAction DRIVE_CONTINUOUS = new DriveForwardAction(false);
  private final boolean singleStep;

  /**
   * When true, this action is complete after one eventloop step
   * @param singleStep 
   */
  public DriveForwardAction(boolean singleStep) {
    setDistance(1);
    setAngle(0);
    this.singleStep = singleStep;
  }

  private boolean executed = false;
  
  @Override
  public int getNextAction() {
    executed = true;
    return Navigator.MOVE;
  }

  public void reset()
  {
    executed = false;
  }
  
  @Override
  public boolean isComplete() {
    if (!singleStep) {
      return false; // Never complete!
    }
    return executed;
  }
}