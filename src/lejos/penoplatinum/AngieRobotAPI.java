package penoplatinum;

import penoplatinum.sensor.IRSeekerV2;
import penoplatinum.util.Utils;
import java.util.List;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.model.Model;
import penoplatinum.movement.RotationMovement;
import penoplatinum.robot.RobotAPI;
import penoplatinum.sensor.RotatingSonarSensor;


/**
 * Responsible for providing access to the hardware of the robot
 * TODO: WARNING, WHEN CHANGIN' PORTS THIS ENTIRE CLASS MUST BE CHECKED
 * @author: Team Platinum
 */
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

  private static final int sensorNumberInfraRed = Model.S1;
  private static final int sensorNumberLight = Model.S4;
  private static final int sensorNumberSonar = Model.S3;
  private static final int sensorNumberMotorLeft = Model.M1;
  private static final int sensorNumberMotorRight = Model.M2;
  private static final int sensorNumberMotorSonar = Model.M3;
  int[] values = new int[Model.SENSORVALUES_NUM];

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

  public boolean move(double distance) {
    getMovement().driveDistance(distance);
    return true;
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
    values[Model.MS1] = getMotorState(motorLeft);
    values[Model.MS2] = getMotorState(motorRight);
    values[Model.MS3] = getMotorState(motorSonar);

    values[sensorNumberLight] = light.getNormalizedLightValue();
    if(this.isSweeping()){
      values[sensorNumberSonar] = (int) sonar.getDistance();
      values[sensorNumberInfraRed] = irSeeker.getDirection();
      values[Model.IR0] = irSeeker.getSensorValue(1);
      values[Model.IR1] = irSeeker.getSensorValue(2);
      values[Model.IR2] = irSeeker.getSensorValue(3);
      values[Model.IR3] = irSeeker.getSensorValue(4);
      values[Model.IR4] = irSeeker.getSensorValue(5);
    }

    return values;
  }

  private int getMotorState(Motor m) {
    for (int i = 0; i < 3; i++) {
      if (!m.isMoving() || m.isStopped() || m.isFloating()) {
        return Model.MOTORSTATE_STOPPED;
      }
      if (m.isForward()) {
        return Model.MOTORSTATE_FORWARD;
      }
      if (m.isBackward()) {
        return Model.MOTORSTATE_BACKWARD;
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
      case Model.M3:
        motorSonar.setSpeed(speed);
        break;
      case Model.M1:
        movement.SPEEDFORWARD = speed > 400 ? 400 : speed;
        break;
      case Model.M2:
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

  public float getRelativeAngle(float reference) {
    outAngle = -reference + movement.getInternalOrientation();
    return outAngle;
  }

  public boolean sweepInProgress() {
    return sonar.sweepInProgress();
  }

  public void sweep(int[] i) {
    sonar.sweep(i);
  }

  public List<Integer> getSweepResult() {
    return sonar.getSweepResult();
  }

  boolean isSweeping = false;
  public void setSweeping(boolean b){
    isSweeping = b;
  }
  public boolean isSweeping() {
    return isSweeping;
  }
}