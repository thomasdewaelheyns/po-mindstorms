package penoplatinum.driver.behaviour;

import penoplatinum.model.Model;
import penoplatinum.model.processor.GapModelProcessor;
import penoplatinum.navigator.Navigator;
import penoplatinum.robot.RobotAPI;

public class GapDetectionRestoreAction extends ActionSkeleton {

  private final RobotAPI api;
  private Model model;
  private GapModelProcessor proc;

  public GapDetectionRestoreAction(RobotAPI api, Model model) {
    super(model);
    this.api = api;
    this.model = model;

    proc = new GapModelProcessor(null);
    proc.setModel(model);

  }
  int state = -1;

  public int getNextAction() {
    if (model.getSensorPart().isTurning()) {
      return Navigator.NONE;
    }
    state++;
    return getStateStart();
  }

  private int getStateStart() {
    if (state == 0) {
      proc.performGapDetectionOnBuffer();
      if (model.getGapPart().isGapFound()) {
        int diff = (model.getGapPart().getGapStartAngle() + model.getGapPart().getGapEndAngle()) / 2;
        setAngle(diff);
        return Navigator.TURN;
      }
    }
    return Navigator.STOP;
  }

  public boolean isComplete() {
    return state > 0;
  }

  public String getKind() {
    return "Align to line";
  }

  public String getArgument() {
    return "";
  }
}
