/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

import penoplatinum.simulator.Model;

/**
 * Holds all the robot's raw sensor values
 * Allows retrieval of the specific sensor values. ( = Mapping from sensor index
 * to the actual interpretation of the sensorvalue, eg getLightSensorValue)
 * 
 * @author MHGameWork
 */
public class SensorModelPart {

  private int[] sensors = new int[Model.SENSORVALUES_NUM];

  public int getLightSensorValue() {
    return this.sensors[Model.S4];
  }

  public int getSonarDistance() {
    return this.getSensorValue(Model.S3);
  }

  public int getSonarAngle() {
    return this.getSensorValue(Model.M3);// - middleTacho;
  }

  /**
   * accessors to give access to the sensor and map data. these are mainly
   * used by the ModelProcessors.
   * TODO: make this private and use individual getters for the sensors
   */
  public int getSensorValue(int num) {
    return this.sensors[num];
  }

  public Boolean isMoving() {
    return sensors[Model.MS1] != Model.MOTORSTATE_STOPPED || sensors[Model.MS2] != Model.MOTORSTATE_STOPPED;
//    return this.sensors[Model.M1] != this.prevSensors[Model.M1] &&
//           this.sensors[Model.M2] != this.prevSensors[Model.M2];
  }

  public Boolean isTurning() {
    return isMoving() && (sensors[Model.MS1] != sensors[Model.MS2]);
//    int changeLeft  = this.sensors[Model.M1] - this.prevSensors[Model.M1];
//    int changeRight = this.sensors[Model.M2] - this.prevSensors[Model.M2];
//    return changeLeft != 0 && changeLeft == changeRight * -1;
  }

  /**
   * Returns the average tacho count of the 2 motors
   */
  public float getAverageTacho() {
    return (getSensorValue(Model.M1) + getSensorValue(Model.M2)) / 2f;
  }
  private double totalTurnedAngle;

  /**
   * Todo: rename to getDirection
   * 
   * @return 
   */
  public double getTotalTurnedAngle() {
    return totalTurnedAngle;
  }

  /**
   * TODO: make this a sensorvalue!
   * @param totalTurnedAngle 
   */
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
  private boolean hasNewSensorValues;

  public boolean isHasNewSensorValues() {
    return hasNewSensorValues;
  }

  public void markSensorValuesProcessed() {
    this.hasNewSensorValues = false;
  }
}
