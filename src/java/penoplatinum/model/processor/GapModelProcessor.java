package penoplatinum.model.processor;

/**
 * Implements a modelProcessor that gathers the information of the SonarSweep
 * It records a complete sweep and at the end, pushes the information to the
 * model.
 * 
 * Author: Team Platinum
 */
import java.util.List;
import penoplatinum.simulator.Model;

public class GapModelProcessor extends ModelProcessor {

  private Boolean direction;
  private int prevAngle = 0;
  public static final int MIN_GAP_ANGLE = 20;
  public static final int MAX_GAP_ANGLE = 90;
  public static final int MIN_GAP_DISTANCE = 40;
  public static final int MAX_GAP_INGORE_ANGLE = 1;
  private boolean gapUnique = true;

  public GapModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  public void setGapMustBeUnique(boolean value) {
    gapUnique = value;
  }

  public void work() {
    
    if (!model.getSensorPart().hasNewSensorValues()) return;
    
    model.getGapPart().setGapFound(false);
    // if we changed direction
    if (this.changedDirection()) {
      // Reached the end of this gap detection pass
      endGapDetectionPass();
      // prepare for next sweep
      startGapDetectionPass();
    }

    if (model.getSensorPart().isTurning()) {
      fail = true; // Fail when robot turns
    }

    // now record the new ping
    this.record(getDistance(), getAngle());

    this.prevAngle = this.getAngle();
  }

  private void endGapDetectionPass() {
    this.direction = this.getDirection();

    // close a possible open gap
    closeGap(getAngle());

    // push a copy of the info to model
    model.getGapPart().setGapFound(!fail && uniqueGapFound);
    if (model.getGapPart().isGapFound()) {
      model.getGapPart().setGapEndAngle(gapEndAngle);
      model.getGapPart().setGapStartAngle(gapStartAngle);
    }

  }

  private void startGapDetectionPass() {

    fail = false;
    uniqueGapFound = false;
    gapStartAngle = Integer.MAX_VALUE;
    gapEndAngle = Integer.MAX_VALUE;
    gapStartFound = false;
  }

  private Boolean changedDirection() {
    if (this.direction == null) {
      this.direction = this.getDirection();
    }
    if (this.getAngle() == this.prevAngle) {
      return false;
    }
    return this.direction != this.getDirection();
  }

  // true  = -135 -> 135
  // false = -135 <- 135
  private boolean getDirection() {
    return this.getAngle() > this.prevAngle;
  }
  private boolean gapStartFound = false;
  private int gapStartAngle;
  private int gapEndAngle;
  private boolean fail = false;
  private boolean uniqueGapFound = false;

  private void record(int distance, int angle) {

    if (fail) {
      return;
    }

    if (distance > MIN_GAP_DISTANCE) {
      growGap(angle);
    } else {
      closeGap(angle);
    }
  }

  private void growGap(int angle) {
    if (gapStartAngle == Integer.MAX_VALUE) {
      // start of gap
      gapStartAngle = angle;
      gapStartFound = true;
      return;
    }

    // just grow the gap, (do nothing)
  }

  private void closeGap(int angle) {
    if (!gapStartFound) {
      return;
    }
    if (fail) {
      return;
    }

    int endAngle = angle;
    int size = Math.abs(endAngle - gapStartAngle);

    if (size < MAX_GAP_INGORE_ANGLE) {
      // Gap is too small for the robot anyways, just ignore
      return;
    }

    if (uniqueGapFound) {
      // Another gap found, invalid data or no walls
      if (gapUnique) {
        fail = true;
      }
      return;
    }


    if (size > MIN_GAP_ANGLE && size < MAX_GAP_ANGLE) {
      // correct gap found
      uniqueGapFound = true;
      gapEndAngle = endAngle;
      gapStartFound = false;
    } else {
      // incorrect gap found (invalid data or no walls), fail
      fail = true;
    }
  }

  /**
   * Detects a gap in the current sonar buffer 
   */
  public void performGapDetectionOnBuffer() {
    List<Integer> distances = model.getSonarPart(). getDistances();
    List<Integer> angles = model.getSonarPart().getAngles();

    startGapDetectionPass();
    for (int i = 0; i < distances.size(); i++) {
      record(distances.get(i), angles.get(i));
    }
    endGapDetectionPass();
  }

  private int getDistance() {
    return model.getSensorPart().getSonarDistance();
  }

  private int getAngle() {
    return model.getSensorPart().getSonarAngle();
  }
}
