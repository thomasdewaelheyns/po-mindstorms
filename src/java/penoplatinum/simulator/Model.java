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

public class Model {
  // shorthands mapping the sensors/numbers to their technical ports
  public static final int M1 = 0; // right motor
  public static final int M2 = 1; // left motor
  public static final int M3 = 2; // sonar motor
  public static final int S1 = 3;
  public static final int S2 = 4;
  public static final int S3 = 5;
  public static final int S4 = 6;

  // the raw data of the 7 sensor: three motors and sensors 1, 2, 3, 4
  private int[] sensors = new int[7];
  private int[] prevSensors = new int[7];

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
  private List<Integer> angles    = new ArrayList<Integer>();
    
  private int[] sweepValues = new int[4];
  private boolean sweepChanged = true;

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
    this.prevSensors = this.sensors.clone();
    this.sensors = values;
    this.process();
  }

  // method to update a set of distances and angles
  public void updateDistances( List<Integer> distances, 
                               List<Integer> angles )
  {
    this.distances = distances;
    this.angles    = angles;
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
    this.stuckLeft  = left;
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
    return this.sensors[Model.M1] != this.prevSensors[Model.M1] &&
           this.sensors[Model.M2] != this.prevSensors[Model.M2];
  }

  public Boolean isTurning() {
    int changeLeft  = this.sensors[Model.M1] - this.prevSensors[Model.M1];
    int changeRight = this.sensors[Model.M2] - this.prevSensors[Model.M2];
    return changeLeft != 0 && changeLeft == changeRight * -1;
  }

  public void setNewSweep(int min, int minA, int max, int maxA){
    this.sweepValues[0] = min;
    this.sweepValues[1] = minA;
    this.sweepValues[2] = max;
    this.sweepValues[3] = maxA;
    this.sweepChanged = true;
  }

  // indicates whether the sweep-values have changed since the last time
  // they are consulted
  public boolean hasUpdatedSonarValues(){
    return this.sweepChanged;
  }
    
  // TODO: refactor this to more function name about extrema
  //       or separate methods to get extrema
  public int[] getSonarValues(){
    this.sweepChanged = false;
    return this.sweepValues.clone();
  }
}
