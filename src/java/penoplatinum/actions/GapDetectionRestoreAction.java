/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.model.processor.GapModelProcessor;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.RobotAPI;

/**
 *
 * @author: Team Platinum
 */
public class GapDetectionRestoreAction extends BaseAction {

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

  @Override
  public int getNextAction() {

    if (model.isTurning()) {
      return Navigator.NONE;
    }



    state++;

    return getStateStart();

  }

  private int getStateStart() {

    if (state == 0) {

      proc.performGapDetectionOnBuffer();
      if (model.isGapFound()) {
        int diff = (model.getGapStartAngle() + model.getGapEndAngle()) / 2;

        setAngle(diff);

        return Navigator.TURN;
      }
    }
    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return state > 0;
  }

  @Override
  public String getKind() {
    return "Align to line";
  }

  @Override
  public String getArgument() {
    return "";
  }
}
