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
import java.util.List;
import java.util.ArrayList;

import penoplatinum.Utils;
import penoplatinum.modelprocessor.Buffer;
import penoplatinum.modelprocessor.LightColor;
import penoplatinum.modelprocessor.ModelProcessor;

import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Grid;
import penoplatinum.grid.GridView;

public class OriginalModel implements Model {
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
  /**
   * This value is true on the step that the sweep was completed
   */
  private Boolean sweepComplete;

  public OriginalModel() {
  }

  // sets the (top-level) processor
  public Model setProcessor(ModelProcessor processor) {
    this.processor = processor;
    this.processor.setModel(this);
    return this;
  }

  /**
   * method to update the current sensor-values. this also includes the
   * processing of the new data.
   */
  public Model updateSensorValues(int[] values) {
    if (values.length != SENSORVALUES_NUM) {
      Utils.Sleep(5000);
      throw new RuntimeException("Invalid number of sensorvalues given!");
    }
    if (this.sensors == null) {
      Utils.Log("HELOOOOOO");
      Utils.Sleep(5000);
    }

    this.prevSensors = this.sensors.clone(); //TODO: WARNING GC
    this.sensors = values;
    this.process();
    
    return this;
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
    isSweepDataChanged = false;

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
  private boolean isSweepDataChanged;

  /**
   * WARNING: THIS IS NOT SUPPOSED TO BE CALLED BY A MODELPROCESSOR
   * @param distances
   * @param angles 
   */
  public void updateSonarValues(List<Integer> distances, List<Integer> angles) {
    this.distances = distances;
    this.angles = angles;
    isSweepDataChanged = true;
  }

  public boolean isSweepDataChanged() {
    return isSweepDataChanged;
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

    String interpretedColor = getCurrentLightColor().toString();
    int sonarAngle = this.getSensorValue(M3) + 90;
    int sonarDistance = getSensorValue(S3);
    boolean pushLeft = this.getSensorValue(S1) == 255;
    boolean pushRight = this.getSensorValue(S2) == 255;

    builder.delete(0, builder.length());
    builder.append(lightValue).append(",\"").append(interpretedColor.toLowerCase()).append("\",\"").append(lastBarcode).append("\",").append(sonarAngle).append(',').append(sonarDistance).append(',').append(pushLeft).append(',').append(pushRight);
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

  // TODO: remove this
  public String explain() { return ""; }
  public void updateSonarValues(List<Integer> distances) {}
  
  public Model displayGridOn(GridView view) { return null; }
  public Grid getGrid() { return null; }
  public void addIncomingMessage(String msg) {}
  public List<String> receiveIncomingMessages() { return null; }
  public List<String> getOutgoingMessages() { return null; }
  public void clearInbox() {}
  public void clearOutbox() {}
  public Agent getAgent() { return null; }
  public boolean hasNewSonarValues() { return false; }
  public void markSonarValuesProcessed() {}
  public void updateSector(Sector newSector) {}
  public Sector getDetectedSector() { return null; }
  public Sector getCurrentSector() { return null; }
  public int getLeftFreeDistance() { return 0; }
  public int getFrontFreeDistance() { return 0; }
  public int getRightFreeDistance() { return 0; }
  public boolean sectorHasChanged() { return false; }
  public void moveForward() {}
  public void turnLeft() {}
  public void turnRight() {}
  public void clearLastMovement() {}
  public int getLastMovement() { return 0; }

  @Override
  public LightColor getCurrentLightColor() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void receiveIncomingMessages(List<String> buffer) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setReadingBarcode(boolean b) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isReadingBarcode() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
