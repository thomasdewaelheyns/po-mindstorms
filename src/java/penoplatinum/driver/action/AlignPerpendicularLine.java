package penoplatinum.actions;

import penoplatinum.util.Utils;
import penoplatinum.util.LightColor;
import penoplatinum.model.GhostModel;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;

/**
 *
 * @author: Team Platinum
 */
public class AlignPerpendicularLine extends ActionSkeleton {

  public static final int TARGET_ANGLE = 20;
  public static final int SWEEP_ANGLE = 250;
  private int directionModifier;
  private Model model;

  public AlignPerpendicularLine(Model model, boolean CCW) {
    super(model);
    this.model = model;
    directionModifier = CCW ? 1 : -1;
  }
  private int state = -1;
  private Integer leftStart;
  private Integer leftEnd;
  private Integer rightStart;
  private Integer rightEnd;

  @Override
  public int getNextAction() {
    if (getModel().getSensorPart().isTurning()) {
      // processing state
      switch (state) {
        case 0:
          if (getModel().getLightPart().getCurrentLightColor() == LightColor.White && leftStart == null) {
            leftStart = getRelativeAngle();
          }
          if (getModel().getLightPart().getCurrentLightColor() == LightColor.Brown && leftStart != null) {
            leftEnd = getRelativeAngle();
            return Navigator.STOP;
          }
          break;
        case 1:
          break;
        case 2:
          if (getModel().getLightPart().getCurrentLightColor() == LightColor.White && rightStart == null) {
            rightStart = getRelativeAngle();
          }
          if (getModel().getLightPart().getCurrentLightColor() == LightColor.Brown && rightStart != null) {
            rightEnd = getRelativeAngle();
            return Navigator.STOP;
          }
          break;
        case 3:
          break;

      }

      return Navigator.NONE;
    }
    state++;
    return getStateStart();

  }
  private double initialAngle;

  private int getRelativeAngle() {
    return (int) (((GhostModel) model).getSensorPart().getTotalTurnedAngle() - initialAngle);
  }

  private int getStateStart() {

    int sweepSize = 170;

    switch (state) {
      case 0:
        // sweep over line left 
        initialAngle = ((GhostModel) model).getSensorPart().getTotalTurnedAngle();
        setAngle(-sweepSize);
        return Navigator.TURN;
      case 1:
        if (leftEnd == null) {
          //Terminate
          state = 3;
          return Navigator.STOP;
        }
        // turn back over the left line
        setAngle(-getRelativeAngle());
        return Navigator.TURN;
      case 2:
        setAngle(sweepSize);
        // sweep over line right
        return Navigator.TURN;
      case 3:
        // correction turn

        if (leftEnd == null || leftStart == null || rightEnd == null || rightStart == null) {
          Utils.Sleep(5000);
          setAngle(-getRelativeAngle());
          return Navigator.TURN;
        }

        leftEnd = Utils.ClampLooped(leftEnd, -360, 0);
        leftStart = Utils.ClampLooped(leftStart, -360, 0);
        rightEnd = Utils.ClampLooped(rightEnd, 0, 360);
        rightStart = Utils.ClampLooped(rightStart, 0, 360);

        int left = (leftEnd + leftStart) / 2;
        int right = (rightEnd + rightStart) / 2;

        left = leftStart;
        right = rightStart;

        int corr = (left + right) / 2;
        corr -= getRelativeAngle();

        setAngle(corr);

        return Navigator.TURN;
    }

    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return state > 3;
  }

  @Override
  public String getKind() {
    return "Align to line";
  }

  @Override
  public String getArgument() {
    return directionModifier > 0 ? "CCW" : "CW";
  }
}
