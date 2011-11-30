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
  private boolean correctionStarted = false;

  @Override
  public int getNextAction() {
    if (first) {
      first = false;
      getModel().getSonarValues(); // clear old sweep
      return Navigator.STOP;
    }
    if (correctionStarted )
      return Navigator.NONE;

    if (!getModel().hasUpdatedSonarValues()) {
      return Navigator.STOP;
    }

    int[] sonarValues = getModel().getSonarValues();
    int targetAngle;
    int currentAngle = sonarValues[1];
    //TODO: set target angle correctly
    targetAngle = 90;
    setAngle(targetAngle - currentAngle);

    correctionStarted = true;

    return Navigator.TURN;
  }

  @Override
  public boolean isComplete() {
    return !getModel().isMoving() && correctionStarted;

  }
}
