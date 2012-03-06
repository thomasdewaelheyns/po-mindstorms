
package penoplatinum.modelprocessor;

import penoplatinum.simulator.Model;

/**
 *
 * @author Daniel
 */
public class ColorInterpreter {

  public static int blackBorder = 30;
  public static int whiteBorder = 85;
  private Model model;

  public ColorInterpreter() {
  }

  public ColorInterpreter setModel(Model m) {
    this.model = m;
    return this;
  }

  public int getLightValue() {
    return model.getSensorValue(model.S4);
  }

  public byte readValue() {
    return (byte) getLightValue();
  }

  /**
   * Checks if the light value in the arguments is the Color from the
   * arguments.
   * 
   * @param col
   *        The Color we want to compare to the light value
   * @param val
   *        The light value we compare to the color
   * @return 
   *        Returns a true boolean if the val is the Color col
   *        Else returns false boolean.
   */
  public boolean isColor(LightColor col, double val) {

    switch (col) {
      case Brown:
        return val >= blackBorder && val <= whiteBorder;
      case Black:
        return val < blackBorder;
      case White:
        return val > whiteBorder;


    }
    throw new AssertionError("Unknown op: " + this);
  }

  /**
   * Checks if the current light value is the Color from the arguments.
   * @param col
   *        The Color we want to compare to the light value
   * @return 
   *        Returns a true boolean if the current light value is the Color col
   *        Else returns false boolean.
   */
  public boolean isColor(LightColor col) {
    return isColor(col, getLightValue());
  }

  /**
   * Checks what color the current light value is.
   * @return 
   *        Returns an enumeration value corresponding with the correct color.
   */
  public LightColor getCurrentColor() {
    return getCurrentColor(getLightValue());
  }

  /**
   * Checks what color the given value is and returns it.
   * @param val
   *        The value we want to check.
   * @return 
   *        Returns an enumeration value corresponding with the correct color.
   */
  public LightColor getCurrentColor(int val) {
    if (isColor(LightColor.Brown, val)) {
      return LightColor.Brown;
    }
    if (isColor(LightColor.Black, val)) {
      return LightColor.Black;
    }
    return LightColor.White;
  }

  /**
   * Checks if the value is closest to white or black.
   * @param value
   *        The value we want to check
   * @return 
   *        Returns 0 when the val is closest to black.
   *        Else returns 1 (closest to white)
   */
  public int isBlackOrWhite(int value) {
    return value < ((blackBorder + whiteBorder) / 2) ? 0 : 1;
  }
}
