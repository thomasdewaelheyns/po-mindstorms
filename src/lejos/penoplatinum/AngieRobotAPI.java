package penoplatinum;

/**
 * Responsible for providing access to the hardware of the robot
 * TODO: WARNING, WHEN CHANGIN' PORTS THIS ENTIRE CLASS MUST BE CHECKED
 * @author: Team Platinum
 */

import java.util.List;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

import penoplatinum.Config;

import penoplatinum.model.Model;
import penoplatinum.movement.RotationMovement;
import penoplatinum.robot.RobotAPI;
import penoplatinum.sensor.RotatingSonarSensor;
import penoplatinum.util.ExtendedVector;
import penoplatinum.util.ReferencePosition;

import penoplatinum.sensor.IRSeekerV2;
import penoplatinum.util.Utils;


public class AngieRobotAPI implements RobotAPI {

  private final SensorPort LIGHT_SENSORPORT = SensorPort.S4;
  private final SensorPort SONAR_SENSORPORT = SensorPort.S3;
  private final SensorPort IR_SENSORPORT = SensorPort.S1;
  private final Motor motorLeft = Motor.B;
  private final Motor motorRight = Motor.C;
  private final Motor motorSonar = Motor.A;
  private LightSensor light;
  private RotatingSonarSensor sonar;
  private IRSeekerV2 irSeeker;
  private RotationMovement movement;
  
  private static final int sensorNumberInfraRed = Config.S1;
  private static final int sensorNumberLight = Config.S4;
  private static final int sensorNumberSonar = Config.S3;
  private static final int sensorNumberMotorLeft = Config.M1;
  private static final int sensorNumberMotorRight = Config.M2;
  private static final int sensorNumberMotorSonar = Config.M3;
  
  int[] values = new int[Config.SENSORVALUES_NUM];

  public AngieRobotAPI() {
    //Reset tacho's
    motorSonar.resetTachoCount();
    motorLeft.resetTachoCount();
    motorRight.resetTachoCount();
    movement = new RotationMovement(motorLeft, motorRight);

    light = new LightSensor(LIGHT_SENSORPORT, true);
    sonar = new RotatingSonarSensor(motorSonar, new UltrasonicSensor(SONAR_SENSORPORT));
    irSeeker = new IRSeekerV2(IR_SENSORPORT, IRSeekerV2.Mode.AC);
  }

  public void step() {
    sonar.updateSonarMovement();
  }

  public RotationMovement getMovement() {
    return movement;
  }

  public LightSensor getLight() {
    return light;
  }

  public Motor getMotorLeft() {
    return motorLeft;
  }

  public Motor getMotorRight() {
    return motorRight;
  }

  public RotatingSonarSensor getSonar() {
    return sonar;
  }

  public TouchSensor getTouchLeft() {
    return null;
    //return touchLeft;
  }

  public TouchSensor getTouchRight() {
    return null;
    //return touchRight;
  }

  public IRSeekerV2 getIrSeeker() {
    return irSeeker;
  }

  public void move(double distance) {
    getMovement().driveDistance(distance);
  }

  public void turn(double angle) {
    getMovement().turnAngle(angle);
  }

  public void stop() {
    getMovement().stop();
  }

  public int[] getSensorValues() {
    values[sensorNumberMotorLeft] = motorLeft.getTachoCount();
    values[sensorNumberMotorRight] = motorRight.getTachoCount();
    values[sensorNumberMotorSonar] = sonar.getMotor().getTachoCount();
    values[Config.MS1] = getMotorState(motorLeft);
    values[Config.MS2] = getMotorState(motorRight);
    values[Config.MS3] = getMotorState(motorSonar);

    values[sensorNumberLight] = light.getNormalizedLightValue();
    if (this.isSweeping()) {
      values[sensorNumberSonar] = (int) sonar.getDistance();
      values[sensorNumberInfraRed] = irSeeker.getDirection();
      values[Config.IR0] = irSeeker.getSensorValue(1);
      values[Config.IR1] = irSeeker.getSensorValue(2);
      values[Config.IR2] = irSeeker.getSensorValue(3);
      values[Config.IR3] = irSeeker.getSensorValue(4);
      values[Config.IR4] = irSeeker.getSensorValue(5);
    }

    return values;
  }

  private int getMotorState(Motor m) {
    for (int i = 0; i < 3; i++) {
      if (!m.isMoving() || m.isStopped() || m.isFloating()) {
        return Config.MOTORSTATE_STOPPED;
      }
      if (m.isForward()) {
        return Config.MOTORSTATE_FORWARD;
      }
      if (m.isBackward()) {
        return Config.MOTORSTATE_BACKWARD;
      }
    }
    Utils.Error("Syncronized??? " + (m.isMoving() ? 1 : 0) + "," + (m.isForward() ? 1 : 0) + "," + (m.isBackward() ? 1 : 0) + "," + (m.isStopped() ? 1 : 0) + "," + (m.isFloating() ? 1 : 0));
    //1, 0, 0, 1, 0
    Utils.Error("I M P O S S I B L E !");
    return 0;
  }

  public void turn(int angle) {
    getMovement().turnAngle(angle);
  }

  public void setSpeed(int motor, int speed) {
    switch (motor) {
      case Config.M3:
        motorSonar.setSpeed(speed);
        break;
      case Config.M1:
        movement.SPEEDFORWARD = speed > 400 ? 400 : speed;
        break;
      case Config.M2:
        movement.SPEEDFORWARD = speed > 400 ? 400 : speed;
        break;
    }
  }

  public void beep() {
    lejos.nxt.Sound.beep();
  }
  private float outAngle;

  public void setReferenceAngle(float reference) {
    reference = movement.getInternalOrientation();
  }

  public ExtendedVector getRelativePosition(ReferencePosition reference) {
    if (reference.internalValue == null) {
      throw new IllegalArgumentException("This reference has not yet been set using setReferencePoint.");
    }
    outVector.set(reference.internalValue);
    outVector.negate();
    outVector.add(movement.getInternalOrientation());

    return outVector;
  }
  public float getRelativeAngle(float reference) {
    outAngle = -reference + movement.getInternalOrientation();
    return outAngle;
  }

  public boolean isSweeping() {
    return sonar.sweepInProgress();
  }

  public void sweep(int[] i) {
    sonar.sweep(i);
  }

  public List<Integer> getSweepResult() {
    return sonar.getSweepResult();
  }
}