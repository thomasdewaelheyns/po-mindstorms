package penoplatinum.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
  private int sonarAngle = 120;   // the sonar moves from - to + this angle
  private int prevSonarTacho = 0;
  private Random random = new Random(987453231);
  private boolean continuousSweep = false;

  public SimulationRobotAPI setSimulatedEntity(SimulatedEntity simulator) {
    this.simulatedEntity = simulator;
    return this;
  }

  public boolean move(double distance) {
    double error = 0.2;//0.2;
    double afwijking = 0;// 0.1;
    distance *= 1 + (random.nextDouble() - 0.5 + afwijking) * error;
    this.simulatedEntity.moveRobot(distance);
    return true;
  }

  public void turn(int angle) {
    double error = 0.2; //0.05;
    double afwijking = 0.3;

    angle = (int) (angle * (1 + (random.nextDouble() - 0.5 + afwijking) * error));
    this.simulatedEntity.turnRobot(angle);
  }

  public void stop() {
    this.simulatedEntity.stopRobot();
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
    this.simulatedEntity.setSpeed(motor, speed);
  }
  private int motorDelay = 10;

  private void restartSonarMotor() {
    if (continuousSweep) {
      restartSonarMotorContinuous();
    } else {
      restartSonarMotorOnDemand();
    }
  }

  private void restartSonarMotorContinuous() {
    int currentTacho = (int) this.simulatedEntity.getSensorValues()[Model.M3];
    // if the motor has finished its previous movement, sweep back ...
    if (currentTacho == this.prevSonarTacho && motorDelay == 0) {
      motorDelay = 20;

    }
    if (motorDelay > 0) {
      motorDelay--;
      if (motorDelay == 1) { // Warning: this 1 here is deliberate
        this.sonarAngle *= -1;
        simulatedEntity.rotateMotorTo(Model.M3, this.sonarAngle);
      }
    }

    this.prevSonarTacho = currentTacho;
  }

  @Override
  public void beep() {
    System.out.println("BEEP");
  }
  private ExtendedVector currentPosition = new ExtendedVector();
  private ExtendedVector outVector = new ExtendedVector();

  @Override
  public void setReferencePoint(ReferencePosition reference) {
    updateCurrentPosition();
    reference.internalValue.set(currentPosition);
  }

  @Override
  public ExtendedVector getRelativePosition(ReferencePosition reference) {
    if (reference.internalValue == null) {
      throw new IllegalArgumentException("This reference has not yet been set using setReferencePoint.");
    }
    updateCurrentPosition();
    outVector.set(reference.internalValue);
    outVector.negate();
    outVector.add(currentPosition);
    return outVector;
  }

  private void updateCurrentPosition() {
    currentPosition.setX((float) simulatedEntity.getPosX());
    currentPosition.setY((float) simulatedEntity.getPosY());
    currentPosition.setAngle((float)simulatedEntity.getDirection() + 90);
  }
  int[] currentSweepAngles;
  int currentSweepAngleIndex;
  List<Integer> resultBuffer = new ArrayList<Integer>();

  @Override
  public boolean sweepInProgress() {
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

  private void restartSonarMotorOnDemand() {
    if (!sweepInProgress()) {
      return;
    }

    int currentTacho = (int) this.simulatedEntity.getSensorValues()[Model.M3];

    int currentAngle = currentSweepAngles[currentSweepAngleIndex];

    if (currentTacho != currentAngle) {
      simulatedEntity.rotateMotorTo(Model.M3, currentAngle);
      return;
    }
    resultBuffer.add((int) this.simulatedEntity.getSensorValues()[Model.S3]);
    currentSweepAngleIndex++;

    if (currentSweepAngleIndex >= currentSweepAngles.length) {
      currentSweepAngles = null;
    }


  }
}
