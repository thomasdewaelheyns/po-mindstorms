/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

import penoplatinum.simulator.Line;
import penoplatinum.util.LightColor;

/**
 * Stores the current light color interpreted by the robot, current robot
 * light calibration and information about the Lines being detected
 * 
 * @author MHGameWork
 */
public class LightModelPart implements IModelPart {

  private LightColor currentLightColor = LightColor.Brown;
  private float averageLightValue;

  public void setAverageLightValue(float averageLightValue) {
    this.averageLightValue = averageLightValue;
  }

  public float getAverageLightValue() {
    return averageLightValue;
  }

  public LightColor getCurrentLightColor() {
    return currentLightColor;
  }

  public void setCurrentLightColor(LightColor value) {
    currentLightColor = value;
  }
  private Line line = Line.NONE;

  public Line getLine() {
    return line;
  }

  public void setLine(Line line) {
    this.line = line;
  }

  @Override
  public void clearDirty() {
  }
}
