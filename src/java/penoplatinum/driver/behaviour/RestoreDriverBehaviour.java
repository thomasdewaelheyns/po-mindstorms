package penoplatinum.driver.behaviour;

public class RestoreDriverBehaviour {

  private final RobotAPI api;
  private Model model;
  private GapModelProcessor proc;
  private int state = -1;

  public RestoreDriverBehaviour(RobotAPI api, Model model) {
    super(model);
    this.api = api;
    this.model = model;

    proc = new GapModelProcessor(null);
    proc.setModel(model);

  }

  public int getNextAction() {
    if (model.getSensorPart().isTurning()) {
      return Navigator.NONE;
    }
    state++;
    return getStateStart();
  }

  private int getStateStart() {
    if (state == 0) {
      proc.performGapDetectionOnBuffer();
      if (model.getGapPart().isGapFound()) {
        int diff = (model.getGapPart().getGapStartAngle() + model.getGapPart().getGapEndAngle()) / 2;
        setAngle(diff);
        return Navigator.TURN;
      }
    }
    return Navigator.STOP;
  }

  public boolean isComplete() {
    return state > 0;
  }

  public String getKind() {
    return "Align to line";
  }

  public String getArgument() {
    return "";
  }
}


/**
NEEDS LOCAL IMPLEMENTATION TO DETECT GAP

  private static final int MIN_GAP_ANGLE        = 20;
  private static final int MAX_GAP_ANGLE        = 90;
  private static final int MIN_GAP_DISTANCE     = 40;
  private static final int MAX_GAP_INGORE_ANGLE =  1;

  public void performGapDetectionOnBuffer() {
    List<Integer> distances = model.getSonarPart(). getDistances();
    List<Integer> angles = model.getSonarPart().getAngles();

    this.startGapDetectionPass();
    for (int i = 0; i < distances.size(); i++) {
      this.record(distances.get(i), angles.get(i));
    }
    this.endGapDetectionPass();
  }

  private void startGapDetectionPass() {
    fail = false;
    uniqueGapFound = false;
    gapStartAngle = Integer.MAX_VALUE;
    gapEndAngle = Integer.MAX_VALUE;
    gapStartFound = false;
  }

  private void endGapDetectionPass() {
    this.direction = this.getDirection();

    // close a possible open gap
    this.closeGap(getAngle());

    // provide a copy of the info
    if( ! fail && uniqueGapFound ) {
      //model.markGap(gapStartAngle, gapEndAngle);
    }
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

  private int getDistance() {
    return model.getSensorPart().getSonarDistance();
  }

  private int getAngle() {
    return model.getSensorPart().getSonarAngle();
  }
}
