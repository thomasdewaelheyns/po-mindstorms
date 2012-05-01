package penoplatinum.model.part;

/**
 * Stores the current light color interpreted by the robot, current robot
 * light calibration and information about the Lines being detected
 * 
 * @author Team Platinum
 */

import penoplatinum.model.Model;

import penoplatinum.util.LightColor;
import penoplatinum.util.Line;

public class LightModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static LightModelPart from(Model model) {
    return (LightModelPart)model.getPart(ModelPartRegistry.LIGHT_MODEL_PART);
  }

  private LightColor currentLightColor = LightColor.BROWN;
  private int        currentLightValue = 0;
  private float      averageLightValue = 0;

  private Line line = Line.NONE;


  public void setCurrentLightColor(LightColor value) {
    this.currentLightColor = value;
  }

  public LightColor getCurrentLightColor() {
    return this.currentLightColor;
  }

  public void setAverageLightValue(float averageLightValue) {
    this.averageLightValue = averageLightValue;
  }

  public float getAverageLightValue() {
    return this.averageLightValue;
  }

  public boolean isReadingLine() {
    return this.line != Line.NONE;
  }

  public Line getLine() {
    return line;
  }

  public void setLine(Line line) {
    this.line = line;
  }
}
