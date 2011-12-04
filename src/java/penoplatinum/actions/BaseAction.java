/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.actions;

import penoplatinum.simulator.Model;

/**
 *
 * @author: Team Platinum
 */
public abstract class BaseAction {

  private Model model;
  private boolean isNonInterruptable;

  public boolean isNonInterruptable() {
    return isNonInterruptable;
  }

  public BaseAction setIsNonInterruptable(boolean isNonInterruptable) {
    this.isNonInterruptable = isNonInterruptable;
    return this;
  }

  public BaseAction() {
    this.model = model;
  }

  public BaseAction(Model model) {
    this.model = model;
  }
  private float distance;
  private int angle;


  public abstract int getNextAction();

  public abstract boolean isComplete();

  public int getAngle() {
    return angle;
  }

  protected void setAngle(int angle) {
    this.angle = angle;
  }

  public float getDistance() {
    return distance;
  }

  protected void setDistance(float distance) {
    this.distance = distance;
  }

  protected Model getModel() {
    return model;
  }
}