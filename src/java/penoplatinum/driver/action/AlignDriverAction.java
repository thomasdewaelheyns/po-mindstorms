package penoplatinum.driver.action;

/**
 * AlignDriverAction
 *
 * Aligns a robot straight on a line it crossed.
 * The following sub-actions are performed in sequence:
 * - turn left and find a white line
 * - turn right and record the beginning and end of the white line on the 
 *   other side
 * - calculate the angle straight on the recorded white line and turn
 * The sub-actions are implemented using a state/strategy pattern using
 * inner classes.
 *
 * @author: Team Platinum
 */

import penoplatinum.driver.action.align.FindLeftWhiteLine;
import penoplatinum.driver.action.align.SubAction;
import penoplatinum.model.Model;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.robot.RobotAPI;

public class AlignDriverAction implements DriverAction {

  private static final int SWEEP_ANGLE = 170;
  
  private SubAction currentSubAction;

  // a reference to the ModelParts we need to perform the alignment action
  private SensorModelPart sensors;
  private LightModelPart light;


  public AlignDriverAction(Model model) {
    this.sensors = SensorModelPart.from(model);
    this.light   = LightModelPart.from(model);
    // set up the first sub-action/state/strategy
    this.currentSubAction = new FindLeftWhiteLine(this);
  }
  
  public SensorModelPart getSensors() {
    return this.sensors;
  }
  
  public LightModelPart getLight() {
    return this.light;
  }

  public boolean isBusy() {
    return this.currentSubAction.isBusy();
  }

  public boolean canBeInterrupted() {
    return false;
  }

  public boolean interrupts() {
    return false;
  }

  public AlignDriverAction work(RobotAPI api) {
    // let the current sub-action do its part
    this.currentSubAction.work(api);
    // wait until the current sub-action is no longer busy
    if( this.currentSubAction.isBusy() ) { return this; }
    // when the currentAction is done ... STOP!
    api.stop();
    // move to the next state
    this.currentSubAction = this.currentSubAction.getNextSubAction();
    return this;
  }

  // TODO: this hasn't been migrated ...
  // if( leftStart == null || rightStart == null ) {
  //    Utils.Sleep(5000);
  //    setAngle(-getRelativeAngle());
  //    return Navigator.TURN;
  //  }
}
