package penoplatinum.model.part;

/**
 *  SensorModelPart
 *
 *  Holds all the robot's raw sensor values and offers a functional interface
 *  to query all of them.
 * 
 *  @author Team Platinum
 */
import penoplatinum.model.Model;
import penoplatinum.simulator.entities.RobotConfig;
import penoplatinum.simulator.sensors.MotorState;

public class SensorModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model

  public static SensorModelPart from(Model model) {
    return (SensorModelPart) model.getPart(ModelPartRegistry.SENSOR_MODEL_PART);
  }
  private int[] sensors = new int[RobotConfig.SENSORVALUES_NUM];

  // public interface
  public boolean isMoving() {
    return this.leftMotorIsMoving() || this.rightMotorIsMoving();
  }

  public boolean isTurning() {
    return this.isMoving()
            && this.getLeftMotorState() != this.getRightMotorState();
  }

  // receive an update of the sensor values
  public void updateSensorValues(int[] values) {
    if (values.length != RobotConfig.SENSORVALUES_NUM) {
      throw new RuntimeException("Invalid number of sensorvalues given!");
    }

    for (int i = 0; i < RobotConfig.SENSORVALUES_NUM; i++) {
      this.sensors[i] = values[i];
    }
  }

  // private helper methods
  // TODO: if any of these need to become public, think twice ;-) then
  // move it up and add enough unit tests
  private boolean rightMotorIsMoving() {
    return this.getRightMotorState() != RobotConfig.MOTORSTATE_STOPPED;
  }

  private boolean leftMotorIsMoving() {
    return this.getLeftMotorState() != RobotConfig.MOTORSTATE_STOPPED;
  }

  private int getRightMotorState() {
    return this.getSensorValue(RobotConfig.MS1);
  }

  private int getLeftMotorState() {
    return this.getSensorValue(RobotConfig.MS2);
  }

  private int getSensorValue(int num) {
    return this.sensors[num];
  }
    private double totalTurnedAngle;

  public double getTotalTurnedAngle() {
    return totalTurnedAngle;
  }

  public void setTotalTurnedAngle(double totalTurnedAngle) {
    this.totalTurnedAngle = totalTurnedAngle;
  }

  private boolean hasNewSensorValues;

  public boolean hasNewSensorValues() {
    return hasNewSensorValues;
  }

  public int getLightSensorValue() { 
    return this.getSensorValue(RobotConfig.S4);
  }


  /*
  Below this point is older code. When clean up other code that requires it,
  move it up and add unit tests to consolidate it.

  public int getSonarDistance()    { return this.getSensorValue(S3);  }
  public int getSonarAngle()       { return this.getSensorValue(M3);  }
  
  
  public float getAverageTacho() {
  return (getSensorValue(Model.M1) + getSensorValue(Model.M2)) / 2f;
  }
  
  public double getTotalTurnedAngle() {
  return totalTurnedAngle;
  }
  
  public void setTotalTurnedAngle(double totalTurnedAngle) {
  this.totalTurnedAngle = totalTurnedAngle;
  }
  
  // receive an update of the sensor values
  public void updateSensorValues(int[] values) {
  
  if (values.length != Model.SENSORVALUES_NUM) {
  throw new RuntimeException("Invalid number of sensorvalues given!");
  }
  
  //this.prevSensors = this.sensors.clone(); //TODO: WARNING GC
  //this.sensors = values;
  for (int i = 0; i < Model.SENSORVALUES_NUM; i++) {
  this.sensors[i] = values[i];
  }
  hasNewSensorValues = true;
  
  //    this.process();
  
  
  
  }

  @Override
  public void clearDirty() {
  this.hasNewSensorValues = false;
  }
  
   */
}
