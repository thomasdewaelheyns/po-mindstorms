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
public class MoveRandomSectorAction extends BaseAction {

  private Model model;

  public MoveRandomSectorAction(Model m) {
    super(m);
    model = m;
  }
  private boolean first = true;

  @Override
  public int getNextAction() {
    Model m = model;
    if (first) {
      first = false;

      setDistance(0.40f);

      if (!m.isWallLeft()) {
        setAngle(90);
      } else if (!m.isWallFront()) {
        setAngle(0);
      } else if (!m.isWallRight()) {
        setAngle(-90);
      } else {
        setAngle(180);
        setDistance(0);
      }
      
      return getStartNavigatorAction();
    }

    if (getAngle() != 0) {
      if (model.isTurning()) {
        return Navigator.NONE;
      }
      setAngle(0);
      return getStartNavigatorAction();
        
    }
    if (getDistance() != 0) {
      if (model.isMoving()) {
        return Navigator.NONE;
      }
      setDistance(0);
      return getStartNavigatorAction();
    }

    return Navigator.STOP;
  }
  
  private int getStartNavigatorAction()
  {
    if (getAngle() != 0)
      return Navigator.TURN;
    if (getDistance() != 0)
      return Navigator.MOVE;
    return Navigator.STOP;
  }

  @Override
  public boolean isComplete() {
    return getAngle() == 0 && getDistance() == 0;

  }

  @Override
  public String getKind() {
    return "MoveRandomSector";
  }

  @Override
  public String getArgument() {
    return "";
  }
}
