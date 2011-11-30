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
public class AlignPerpendicularAction extends BaseAction {

  public AlignPerpendicularAction(Model m) {
    super(m);
    setDistance(0);
    setAngle(0);
  }
  private boolean first = true;
  private boolean oldSweepDone = false;
  private boolean correctionStarted = false;

  @Override
  public int getNextAction() {
    if (first) {
      first = false;
      getModel().getSonarValues(); // clear old sweep
      return Navigator.STOP;
    }
    if (correctionStarted) {
      return Navigator.NONE;
    }

    if (!getModel().hasUpdatedSonarValues()) {
      return Navigator.STOP;
    }

    if (!oldSweepDone) {
      oldSweepDone = true;
      return Navigator.STOP;
    }

    int[] sonarValues = getModel().getSonarValues();
    int currentAngle = sonarValues[1];
    //TODO: set target angle correctly
    int diff = (sonarValues[3] - sonarValues[1] + 360) % 360;
    int targetAngle = diff > 180 ? 105 : -105;
    final int rotation = currentAngle - targetAngle;
    if (Math.abs(rotation) < 5) {
      correctionStarted = true;
      return Navigator.STOP;
    }


    setAngle(rotation);











    correctionStarted = true;

    return Navigator.TURN;
  }

  @Override
  public boolean isComplete() {
    return !getModel().isMoving() && correctionStarted;

  }
}
