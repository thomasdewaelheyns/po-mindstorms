package penoplatinum.actions;

/**
 * AlignDriverAction
 *
 * Aligns a robot straight on a line it crossed.
 * The following sub-actions are performed in sequence:
 * - turn left and record the beginning and end of a white line
 * - turn right and record the beginning and end of the white line on the 
 *   other side
 * - calculate the angle straight on the recorded white line and turn
 * The sub-actions are implemented using a state/strategy pattern using
 * inner classes.
 *
 * @author: Team Platinum
 */

import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.LightModelPart;


public class AlignDriverAction implements DriverAction {

  private static final int SWEEP_ANGLE = 170;

  private interface SubAction {
    public boolean isBusy();
    public void work(RobotAPI api);
    public SubAction getNextSubAction();
  }
  
  // Step 1: turns to the left until a white line is encountered
  private class FindLeftWhiteLine implements SubAction {
    private AlignDriverAction context;

    public FindLeftWhiteLine(AlignDriverAction context) {
      this.context = context;
    }

    public boolean isBusy() {
      return this.context.getLight().getCurrentLightColor() != LightColor.White;
    }

    public void work(RobotAPI api) {
      // if we're not turning ... start turning
      if( ! this.context.getSensors().isTurning() ) {
        api.turn(-SWEEP_ANGLE);
      }
    }
    
    public SubAction getNextSubAction() {
      return new FindRightWhiteLineEnd(this.context);
    }
  }
  
  // Step 2: turns to the right until a white line is encountered. the angle
  //         needed is recorded.
  private class FindRightWhiteLine implements SubAction {
    private AlignDriverAction context;
    private int start;

    public FindRightWhiteLine(AlignDriverAction context) {
      this.context = context;
      this.start = this.context.getSensor().getTotalTurnedAngle();
    }

    public boolean isBusy() {
      // wait until we have turned at least 10 degrees (so we're off the white
      // line again).
      // FIXME: this isn't nice ... but it will do for now ;-)
      return this.getCurrentAngle() < 10 ||
             this.context.getLight().getCurrentLightColor() != LightColor.White;
    }

    public void work(RobotAPI api) {
      // if we're not turning ... start turning
      if( ! this.context.getSensors().isTurning() ) {
        api.turn(-(SWEEP_ANGLE * 2));
      }
    }
    
    public SubAction getNextSubAction() {
      // turn back half the turn we made to get back to a white line
      return new TurnToAlign(-(this.getCurrentAngle()/2));
    }
    
    private int getCurrentAngle() {
      return this.context.getSensor().getTotalTurnedAngle() - this.start;
    }
  }
  
  // Step 3: turns a given angle to align again to the crossed white line
  private class FindRightWhiteLine implements SubAction {
    private AlignDriverAction context;
    private correction

    public FindRightWhiteLine(AlignDriverAction context, int correction) {
      this.context    = context;
      this.correction = correction;
    }

    public boolean isBusy() {
      return this.context.getSensors().isTurning();
    }

    public void work(RobotAPI api) {
      // if we're not turning ... start turning
      if( ! this.isBusy() ) {
        api.turn(this.correction);
      }
    }
    
    public SubAction getNextSubAction() {
      // this is the end ...
      return this;
    }
  }
  
  private SubAction currentSubAction;

  // a referece to the ModelParts we need to perform the alignment action
  private SensorModelPart sensors;
  private LightModelPart light;


  public AlignDriverAction(Model model) {
    this.sensors = SensorModelPart.from(model);
    this.light   = LightModelPart.from(model);
    // set up the first sub-action/state/strategy
    this.currentSubAction = new FindLeftWhiteLine();
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

  public TurnDriverAction work(RobotAPI api) {
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
