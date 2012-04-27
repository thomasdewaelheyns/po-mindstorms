package penoplatinum.model.part;

/**
 *  SensorModelPart
 *
 *  Holds all the robot's raw sensor values and offers a functional interface
 *  to query all of them.
 * 
 *  @author Team Platinum
 */

import penoplatinum.Config;

import penoplatinum.model.Model;


public class SensorModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static SensorModelPart from(Model model) {
    return (SensorModelPart) model.getPart(ModelPartRegistry.SENSOR_MODEL_PART);
  }

  private int[] sensors = new int[Config.SENSORVALUES_NUM];
  private boolean hasNewSensorValues;
  private double totalTurnedAngle;
  private int valuesId = 0;


  // receive an update of the sensor values
  public void updateSensorValues(int[] values) {
    if( values.length != Config.SENSORVALUES_NUM ) {
      throw new RuntimeException("Invalid number of sensorvalues given!");
    }

    for (int i = 0; i<Config.SENSORVALUES_NUM; i++) {
      this.sensors[i] = values[i];
    }
    
    this.valuesId++;
  }

  public boolean isMoving() {
    return this.leftMotorIsMoving() || this.rightMotorIsMoving();
  }

  public boolean isTurning() {
    return this.isMoving() && 
           (this.getLeftMotorState() != this.getRightMotorState());
  }
  
  public int getSonarDistance() {
    return this.getSensorValue(Config.SONAR_DISTANCE);
  }
  
  public int getSonarAngle() {
    return this.getSensorValue(Config.SONAR_ANGLE);
  }
  
  public int getIRDirection() {
    return this.getSensorValue(Config.IR_DIRECTION);
  }
  
  public int getIRValue(int direction) {
    return this.getSensorValue(Config.IR0 + direction);
  }

  public int getLightSensorValue() { 
    return this.getSensorValue(Config.LIGHT_SENSOR);
  }

  public void setTotalTurnedAngle(double totalTurnedAngle) {
    this.totalTurnedAngle = totalTurnedAngle;
  }

  public double getTotalTurnedAngle() {
    return this.totalTurnedAngle;
  }

  public int getValuesId() {
    return this.valuesId;
  }

  // private helper methods
  // TODO: if any of these need to become public, think twice ;-) then
  // move it up and add enough unit tests

  private boolean rightMotorIsMoving() {
    return this.getRightMotorState() != Config.MOTORSTATE_STOPPED;
  }

  private boolean leftMotorIsMoving() {
    return this.getLeftMotorState() != Config.MOTORSTATE_STOPPED;
  }

  private int getRightMotorState() {
    return this.getSensorValue(Config.MOTOR_STATE_RIGHT);
  }

  private int getLeftMotorState() {
    return this.getSensorValue(Config.MOTOR_STATE_LEFT);
  }

  // DO NOT MAKE THIS METHOD PUBLIC ... add additional accessors + tests !!!
  private int getSensorValue(int num) {
    return this.sensors[num];
  }
}
