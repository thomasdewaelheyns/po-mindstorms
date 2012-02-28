package penoplatinum.simulator;

/**
 * Model
 * 
 * Stores all information about the robot and its environment. Using 
 * ModelProcessors the incoming (sensor-)data is processed and turned in a 
 * map. The Navigator can interrogate this map to decide where to go next.
 * 
 * @author: Team Platinum
 */
import penoplatinum.modelprocessor.ModelProcessor;
import java.util.List;
import java.util.ArrayList;
import penoplatinum.modelprocessor.Buffer;
import penoplatinum.modelprocessor.ColorInterpreter;

public class Model {
  // shorthands mapping the sensors/numbers to their technical ports

  public static final int M1 = 0; // right motor
  public static final int M2 = 1; // left motor
  public static final int M3 = 2; // sonar motor
  public static final int S1 = 3; // touch right
  public static final int S2 = 4; // touch left
  public static final int S3 = 5; // sonarsensor
  public static final int S4 = 6; // lightsensor
  public static final int MS1 = 7; // Motor state 1
  public static final int MS2 = 8; // Motor state 1
  public static final int MS3 = 9; // Motor state 1
  public static final int SENSORVALUES_NUM = SimulatedEntity.NUMBER_OF_SENSORS; // number of sensorvalues 
  public static final int MOTORSTATE_FORWARD = 1;
  public static final int MOTORSTATE_BACKWARD = 2;
  public static final int MOTORSTATE_STOPPED = 3;
  
  private float positionX;
  private float positionY;
  private float direction;
  
  /**
   * the raw data of the sensors: three motors, sensors 1, 2, 3, 4 
   * and the states of the three motors defined by the MOTORSTATE enumeration
   */
  private int[] sensors = new int[SENSORVALUES_NUM];
  private int[] prevSensors = new int[SENSORVALUES_NUM];
  // processors are chained using a Decorator pattern
  private ModelProcessor processor;
  // flag indicating the robot is stuck, ModelProcessors determine this
  private Boolean stuckLeft = false;
  private Boolean stuckRight = false;
  // storage for Distance values/angles
  private List<Integer> distances = new ArrayList<Integer>();
  private List<Integer> angles = new ArrayList<Integer>();
  private int[] sweepValues = new int[4];
  private boolean sweepChanged = true;
  private int barcode = -1;
  private Line line = Line.NONE;
  private int bufferSize = 2000;
  private Buffer lightValueBuffer = new Buffer(bufferSize);
  private double barcodeAngle = 0;
  private boolean lightCorruption = false;
  private ColorInterpreter interpreter;
  
  /**
   * This value is true on the step that the sweep was completed
   */
  private Boolean sweepComplete;

  public Model() {
    interpreter = new ColorInterpreter();
    interpreter.setModel(this);
  }

  // sets the (top-level) processor
  public void setProcessor(ModelProcessor processor) {
    this.processor = processor;
    this.processor.setModel(this);
  }

  /**
   * method to update the current sensor-values. this also includes the
   * processing of the new data.
   */
  public void updateSensorValues(int[] values) {
    if (values.length != SENSORVALUES_NUM) {
      throw new RuntimeException("Invalid number of sensorvalues given!");
    }

    this.prevSensors = this.sensors.clone(); //TODO: WARNING GC
    this.sensors = values;
    this.process();
  }

  // method to update a set of distances and angles
  public void updateDistances(List<Integer> distances,
          List<Integer> angles) {
    this.distances = distances;
    this.angles = angles;
  }

  public List<Integer> getDistances() {
    return this.distances;
  }

  public List<Integer> getAngles() {
    return this.angles;
  }

  /**
   * triggers the processor(s) to start processing the sensordata and update
   * the map.
   */
  private void process() {
    setScanningLightData(false); // Resets this flag to false
    sweepComplete = false;
    if (this.processor != null) {
      this.processor.process();
    }
  }

  /**
   * accessors to give access to the sensor and map data. these are mainly
   * used by the ModelProcessors.
   */
  public int getSensorValue(int num) {
    return this.sensors[num];
  }

  /**
   * marks that the robot is currently stuck
   */
  public void markStuck() {
    this.stuckLeft = true;
    this.stuckRight = true;
  }

  public void markStuck(boolean left, boolean right) {
    this.stuckLeft = left;
    this.stuckRight = right;
  }

  // marks that the robot is (no longer) stuck
  public void markNotStuck() {
    this.stuckLeft = false;
    this.stuckRight = false;
  }

  public void markStuckLeft() {
    this.stuckLeft = true;
    this.stuckRight = false;
  }

  public void markStuckRight() {
    this.stuckLeft = false;
    this.stuckRight = true;
  }

  // indicates wheter the robot is currently stuck
  public Boolean isStuck() {
    return this.stuckLeft || this.stuckRight;
  }

  public Boolean isStuckLeft() {
    return this.stuckLeft;
  }

  public Boolean isStuckRight() {
    return this.stuckRight;
  }

  public Boolean isMoving() {
    return sensors[Model.MS1] != MOTORSTATE_STOPPED || sensors[Model.MS2] != MOTORSTATE_STOPPED;
//    return this.sensors[Model.M1] != this.prevSensors[Model.M1] &&
//           this.sensors[Model.M2] != this.prevSensors[Model.M2];
  }

  public Boolean isTurning() {
    return isMoving() && (sensors[Model.MS1] != sensors[Model.MS2]);
//    int changeLeft  = this.sensors[Model.M1] - this.prevSensors[Model.M1];
//    int changeRight = this.sensors[Model.M2] - this.prevSensors[Model.M2];
//    return changeLeft != 0 && changeLeft == changeRight * -1;
  }

  public void setNewSweep(int min, int minA, int max, int maxA) {
    this.sweepValues[0] = min;
    this.sweepValues[1] = minA;
    this.sweepValues[2] = max;
    this.sweepValues[3] = maxA;
    this.sweepChanged = true;
    this.sweepComplete = true;
  }

  // indicates whether the sweep-values have changed since the last time
  // they are consulted
  public boolean hasUpdatedSonarValues() {
    return this.sweepChanged;
  }

  // TODO: refactor this to more function name about extrema
  //       or separate methods to get extrema
  public int[] getSonarValues() {
    this.sweepChanged = false;
    return this.sweepValues.clone(); //TODO: WARNING GC
  }

  public int getBarcode() {
    return this.barcode;
  }
  private int lastBarcode;

  public void setBarcode(int barcode) {
    if (barcode != -1) {
      lastBarcode = barcode;
    }
    this.barcode = barcode;
  }

  public Buffer getLightValueBuffer() {
    return this.lightValueBuffer;
  }

  public Line getLine() {
    return line;
  }

  public void setLine(Line line) {
    this.line = line;
  }

  public void setBarcodeAngle(double angle) {
    this.barcodeAngle = angle;
  }

  public double getBarcodeAngle() {
    return this.barcodeAngle;
  }

  /**
   * Returns the average tacho count of the 2 motors
   */
  public float getAverageTacho() {
    return (getSensorValue(M1) + getSensorValue(M2)) / 2f;
  }

  public boolean isLightDataCorrupt() {
    return lightCorruption;
  }

  public void setLightCorruption(boolean lightCorruption) {
    this.lightCorruption = lightCorruption;
  }
  private StringBuilder builder = new StringBuilder();

  public String toString() {
    int lightValue = this.getSensorValue(S4);

    String interpretedColor = interpreter.getCurrentColor().toString();
    int sonarAngle = this.getSensorValue(M3) + 90;
    int sonarDistance = getSensorValue(S3);
    boolean pushLeft = this.getSensorValue(S1) == 255;
    boolean pushRight = this.getSensorValue(S2) == 255;

    builder.delete(0, builder.length());
    builder .append(lightValue).append(",\"")
            .append(interpretedColor.toLowerCase()).append("\",\"")
            .append(lastBarcode).append("\",")
            .append(sonarAngle).append(',')
            .append(sonarDistance).append(',')
            .append(pushLeft).append(',')
            .append(pushRight);
    return builder.toString();
  }
  private boolean leftObstacle;
  private boolean rightObstacle;

  public boolean isLeftObstacle() {
    return leftObstacle;
  }

  public void setLeftObstacle(boolean leftObstacle) {
    this.leftObstacle = leftObstacle;
  }

  public boolean isRightObstacle() {
    return rightObstacle;
  }

  public void setRightObstacle(boolean rightObstacle) {
    this.rightObstacle = rightObstacle;
  }
  private boolean gapFound;
  private int gapStartAngle;
  private int gapEndAngle;

  public int getGapEndAngle() {
    return gapEndAngle;
  }

  public void setGapEndAngle(int gapEndAngle) {
    this.gapEndAngle = gapEndAngle;
  }

  public boolean isGapFound() {
    return gapFound;
  }

  public void setGapFound(boolean gapFound) {
    this.gapFound = gapFound;
  }

  public int getGapStartAngle() {
    return gapStartAngle;
  }

  public void setGapStartAngle(int gapStartAngle) {
    this.gapStartAngle = gapStartAngle;
  }
  private boolean scanningLightData;

  public boolean isScanningLightData() {
    return scanningLightData;
  }

  /**
   * This hints other parts of the robot that something is reading light data
   * @param scanningLightData 
   */
  public void setScanningLightData(boolean scanningLightData) {
    this.scanningLightData = scanningLightData;
  }

  /**
   * @return the positionX
   */
  public float getPositionX() {
    return positionX;
  }

  /**
   * @param positionX the positionX to set
   */
  public void setPositionX(float positionX) {
    this.positionX = positionX;
  }

  /**
   * @return the positionY
   */
  public float getPositionY() {
    return positionY;
  }

  /**
   * @param positionY the positionY to set
   */
  public void setPositionY(float positionY) {
    this.positionY = positionY;
  }

  public float getDirection() {
    return direction;
  }

  public void setDirection(float direction) {
    this.direction = direction;
  }

  /**
   * Returns true when the sweep was completed this step
   * @return 
   */
  public Boolean isSweepComplete() {
    return sweepComplete;
  }
  
  
  
  
  
  private boolean wallLeft;
  private boolean wallFront;
  private boolean wallRight;
  private int wallLeftDistance;
  private int wallFrontDistance;
  private int wallRightDistance;

  
  private int wallLeftClosestAngle;
  private int wallRightClosestAngle;
  
  
  
  public boolean isWallFront() {
    return wallFront;
  }

  public void setWallFront(boolean wallFront) {
    this.wallFront = wallFront;
  }

  public boolean isWallLeft() {
    return wallLeft;
  }

  public void setWallLeft(boolean wallLeft) {
    this.wallLeft = wallLeft;
  }

  public boolean isWallRight() {
    return wallRight;
  }

  public void setWallRight(boolean wallRight) {
    this.wallRight = wallRight;
  }

  public int getWallFrontDistance() {
    return wallFrontDistance;
  }

  public void setWallFrontDistance(int wallFrontDistance) {
    this.wallFrontDistance = wallFrontDistance;
  }

  public int getWallLeftDistance() {
    return wallLeftDistance;
  }

  public void setWallLeftDistance(int wallLeftDistance) {
    this.wallLeftDistance = wallLeftDistance;
  }

  public int getWallRightDistance() {
    return wallRightDistance;
  }

  public void setWallRightDistance(int wallRightDistance) {
    this.wallRightDistance = wallRightDistance;
  }

  public int getWallLeftClosestAngle() {
    return wallLeftClosestAngle;
  }

  public void setWallLeftClosestAngle(int wallLeftClosestAngle) {
    this.wallLeftClosestAngle = wallLeftClosestAngle;
  }

  public int getWallRightClosestAngle() {
    return wallRightClosestAngle;
  }

  public void setWallRightClosestAngle(int wallRightClosestAngle) {
    this.wallRightClosestAngle = wallRightClosestAngle;
  }
  
  
  
  
  private double totalTurnedAngle;

  public double getTotalTurnedAngle() {
    return totalTurnedAngle;
  }

  public void setTotalTurnedAngle(double totalTurnedAngle) {
    this.totalTurnedAngle = totalTurnedAngle;
  }
  
  
  
  
  
  
  
  
  
  
  
}
