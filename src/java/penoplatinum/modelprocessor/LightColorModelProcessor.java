/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.modelprocessor;

/**
 *
 * @author Thomas
 */
public class LightColorModelProcessor extends ModelProcessor {

  private final int DISCONTINUITY_THRESHOLD = 20;
  private final float AVERAGE_EXPONENT = 0.001f;
  private float averageLightValue;
  private final float AVERAGE_COLOR_EXPONENT = 0.05f;
  private float averageWhiteValue;
  private float averageBlackValue;
  public static final int BLACK = -1;
  public static final int BROWN = 0;
  public static final int WHITE = 1;

  public LightColorModelProcessor() {
    super();
  }

  public LightColorModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }
  int prevValue;
  int prevColor; // -1 = black 0 = brown 1 = white

  @Override
  protected void work() {
    int value = this.model.getSensorValue(this.model.S4);

    averageLightValue = averageLightValue * (1 - AVERAGE_EXPONENT) + value * AVERAGE_EXPONENT;

    // Detect discontinuity
    if (Math.abs(value - prevValue) > DISCONTINUITY_THRESHOLD) {
      // found a color difference
      if (value + DISCONTINUITY_THRESHOLD < averageLightValue) {
        // Black
        prevColor = -1;
      } else if (value - DISCONTINUITY_THRESHOLD > averageLightValue) {
        // White
        prevColor = 1;
      } else {
        // probably brown
        prevColor = 0;
      }
    }


    if ((averageLightValue - DISCONTINUITY_THRESHOLD < value && value < averageLightValue)
            || (averageLightValue < value && value < averageLightValue + DISCONTINUITY_THRESHOLD)) {
      // Looks too much like brown, so reset to brown
      prevColor = 0;
    }

    switch (prevColor) {
      case WHITE:
        averageWhiteValue = averageWhiteValue * (1 - AVERAGE_COLOR_EXPONENT) + value * AVERAGE_COLOR_EXPONENT;
        break;
      case BROWN:
        break;
      case BLACK:
        averageBlackValue = averageBlackValue * (1 - AVERAGE_COLOR_EXPONENT) + value * AVERAGE_COLOR_EXPONENT;
        break;
    }

//    Utils.Log((int) averageBlackValue + "," + (int) averageLightValue + "," + (int) averageWhiteValue);

    prevValue = value;
  }
}
