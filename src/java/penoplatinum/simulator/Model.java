package penoplatinum.simulator;

/**
 * Model
 * 
 * Stores all information about the robot and its environment. Using 
 * ModelProcessors the incoming (sensor-)data is processed and turned in a 
 * map. The Navigator can interrogate this map to decide where to go next.
 * 
 * Author: Team Platinum
 */
import java.util.List;
import java.util.ArrayList;
import penoplatinum.navigator.Buffer;
import penoplatinum.Utils;

public class Model {
  // shorthands mapping the sensors/numbers to their technical ports

  public static final int M1 = 0; // right motor
  public static final int M2 = 1; // left motor
  public static final int M3 = 2; // sonar motor
  public static final int S1 = 3;
  public static final int S2 = 4;
  public static final int S3 = 5;
  public static final int S4 = 6;
  public static final int MS1 = 7; // Motor state 1
  public static final int MS2 = 8; // Motor state 1
  public static final int MS3 = 9; // Motor state 1
  public static final int SENSORVALUES_NUM = 10; // number of sensorvalues 
  public static final int MOTORSTATE_FORWARD = 1;
  public static final int MOTORSTATE_BACKWARD = 2;
  public static final int MOTORSTATE_STOPPED = 3;
  /**
   * the raw data of the sensors: three motors, sensors 1, 2, 3, 4 
   * and the states of the three motors defined by the MOTORSTATE enumeration
   */
  private int[] sensors = new int[SENSORVALUES_NUM];
  private int[] prevSensors = new int[SENSORVALUES_NUM];
  // processors are chained using a Decorator pattern
  private ModelProcessor processor;
  // current position
  private int x, y;
  // the map constructed based on the input
  //private Map map;
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

  //public Map getMap() {
  //  return this.map;
  //}
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
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

  public void setBarcode(int barcode) {
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
}
