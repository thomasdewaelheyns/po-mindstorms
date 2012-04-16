package penoplatinum.model.part;

/**
 * Stores the current light color interpreted by the robot, current robot
 * light calibration and information about the Lines being detected
 * 
 * @author Team Platinum
 */

import penoplatinum.model.Model;
import penoplatinum.simulator.Line;


public class LightModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static LightModelPart from(Model model) {
    return (LightModelPart)model.getPart(ModelPartRegistry.LIGHT_MODEL_PART);
  }

  // TODO: I don't think it is worth having an Enum for this, this can all
  //       be kept internal and exposed using functional methods.
  private Line line = Line.NONE;

  public boolean isReadingLine() {
    return this.line != Line.NONE;
  }

  /*
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

  public Line getLine() {
    return line;
  }

  public void setLine(Line line) {
    this.line = line;
  }

  @Override
  public void clearDirty() {
  }
  */
}
