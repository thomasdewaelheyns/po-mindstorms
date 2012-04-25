package penoplatinum.robot;

import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.util.ExtendedVector;
import java.util.ArrayList;
import java.util.List;
import penoplatinum.simulator.entities.RobotConfig;
import penoplatinum.util.ReferencePosition;

/**
 * SimulationRobotAPI
 * 
 * Implements the RobotAPI interface to track the robot's actions and return
 * this information to the Simulator.
 * 
 * @author: Team Platinum
 */
public class SimulationRobotAPI implements RobotAPI {

  private SimulatedEntity simulatedEntity;

  public SimulationRobotAPI setSimulatedEntity(SimulatedEntity simulator) {
    this.simulatedEntity = simulator;
    return this;
  }

  public void move(double distance) {
    distance *= 100;
    // calculate the tacho count we need to do to reach this movement
    int tacho = (int) (distance / RobotConfig.WHEEL_SIZE * 360);
    this.simulatedEntity.getMotor(RobotConfig.M1).rotateBy(tacho);
    this.simulatedEntity.getMotor(RobotConfig.M2).rotateBy(tacho);
  }

  public void turn(int angle) {
    // calculate anmount of tacho needed to perform a turn by angle
    double dist = Math.PI * RobotConfig.WHEEL_BASE / 360 * angle;
    int tacho = (int) (dist / RobotConfig.WHEEL_SIZE * 360);
    this.simulatedEntity.getMotor(RobotConfig.M1).rotateBy(tacho);
    this.simulatedEntity.getMotor(RobotConfig.M2).rotateBy(-tacho);
  }

  public void stop() {
    this.simulatedEntity.getMotor(RobotConfig.M1).stop();
    this.simulatedEntity.getMotor(RobotConfig.M2).stop();
  }

  public int[] getSensorValues() {
    // we inject the sweeping behaviour of the sonar at this point, because
    // it allows us to simulate an autonous thread without requiring one.
    this.restartSonarMotor();
    // put all sensorvalues in an array and return them
    int[] out = new int[this.simulatedEntity.getSensorValues().length];
    for (int i = 0; i < out.length; i++) {
      out[i] = (int) this.simulatedEntity.getSensorValues()[i];
    }
    return out;
  }

  public void setSpeed(int motor, int speed) {
    this.simulatedEntity.getMotor(motor).setSpeed(speed);
  }

  private void restartSonarMotor() {
    if (!isSweeping()) {
      return;
    }

    int currentTacho = (int) this.simulatedEntity.getSensorValues()[RobotConfig.M3];
    int currentAngle = currentSweepAngles[currentSweepAngleIndex];
    if (currentTacho != currentAngle) {
      simulatedEntity.getMotor(RobotConfig.M3).rotateTo(currentAngle);
      return;
    }
    resultBuffer.add((int) this.simulatedEntity.getSensorValues()[RobotConfig.S3]);
    currentSweepAngleIndex++;

    if (currentSweepAngleIndex >= currentSweepAngles.length) {
      currentSweepAngles = null;
    }
  }

  @Override
  public void beep() {
    System.out.println("BEEP");
  }
  private float currentAngle ;
  private float outAngle;

  @Override
  public void setReferenceAngle(float reference) {
    updateCurrentAngle();
    reference = currentAngle;
  }

  @Override
  public float getRelativeAngle(float reference) {
    updateCurrentAngle();
    outAngle = -reference + currentAngle;
    return outAngle;
  }

  private void updateCurrentPosition() {
    currentPosition.setX((float) simulatedEntity.getPosX());
    currentPosition.setY((float) simulatedEntity.getPosY());
    currentPosition.setAngle((float) simulatedEntity.getDirection() + 90);
  }
  private void updateCurrentAngle() {
    currentAngle = ((float)simulatedEntity.getDirection() + 90);
  }
  int[] currentSweepAngles;
  int currentSweepAngleIndex;
  List<Integer> resultBuffer = new ArrayList<Integer>();

  @Override
  public boolean isSweeping() {
    return currentSweepAngles != null;
  }

  @Override
  public void sweep(int[] i) {
    currentSweepAngles = i;
    currentSweepAngleIndex = 0;
    //WARNING: this is not possible, the result-array of getSweepResult can live longer than 1 sweep
    resultBuffer = new ArrayList<Integer>();
  }

  @Override
  public List<Integer> getSweepResult() {
    return resultBuffer;
  }
}