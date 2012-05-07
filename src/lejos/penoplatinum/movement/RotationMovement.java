package penoplatinum.movement;

import lejos.nxt.Motor;
import penoplatinum.util.Utils;

public class RotationMovement {

  public static double CCW_afwijking = 1.01;
  public int SPEEDFORWARD = 400; //400
  public int SPEEDTURN = 250;
  public final int WIELOMTREK = 175; //mm
  public final int WIELAFSTAND = 160;//112mm
  
  public Motor motorLeft;
  public Motor motorRight;

  private int tachoLeftStart;
  private int tachoRightStart;
  private float internalOrientation;
  private float buffer;

  public RotationMovement(Motor motorLeft, Motor motorRight) {
    this.motorLeft = motorLeft;
    this.motorRight = motorRight;
  }

  private void abortMovement() {
    internalOrientation = getInternalOrientation();
    tachoLeftStart = motorLeft.getTachoCount();
    tachoRightStart = motorRight.getTachoCount();
  }

  public float getInternalOrientation() {
    int tachoLeftDiff = motorLeft.getTachoCount() - tachoLeftStart;
    int tachoRightDiff = motorRight.getTachoCount() - tachoRightStart;
    float averageForward = (tachoRightDiff - tachoLeftDiff) / 2.0f;
    float inverse = (float) (averageForward * WIELOMTREK / WIELAFSTAND / Math.PI);
    buffer = inverse;
    buffer +=internalOrientation;
    return buffer;
  }

  public void driveForward() {
    motorLeft.forward();
    motorRight.forward();
  }

  public void driveBackward() {
    motorLeft.backward();
    motorRight.backward();
  }

  public void driveDistance(double distance) {
    abortMovement();
    distance *= 1000;
    distance /= 0.99;
    int r = (int) (distance * 360 / WIELOMTREK);
    setSpeed(SPEEDFORWARD);
    motorLeft.rotate(r, true);
    motorRight.rotate(r, true);
  }

  /**
   * This function blocks until movement is complete
   * TODO: WARNING: spins this thread! Preferable use this in a single thread, eg the main thread 
   */
  public void waitForMovementComplete() {
    while (motorLeft.isMoving()) {
      Utils.Sleep(20);
    }
  }

  public void turnAngle(double angleCCW) {
    abortMovement();
    setSpeed(SPEEDTURN);
    angleCCW *= CCW_afwijking;

    int h = (int) (angleCCW * Math.PI * WIELAFSTAND / WIELOMTREK);
    motorLeft.rotate(-h, true);
    motorRight.rotate(h, true);
  }

  public void turn(boolean ccw) {
    setSpeed(SPEEDTURN);
    if (ccw) {
      motorLeft.forward();
      motorRight.backward();
    } else {

      motorRight.forward();
      motorLeft.backward();
    }
  }

  public void setSpeed(int speed) {
    motorLeft.setSpeed(speed);
    motorRight.setSpeed(speed);
  }

  public void stop() {
    abortMovement();
    motorLeft.stop();
    motorRight.stop();
  }

  public boolean isStopped() {
    return (!motorLeft.isMoving() && !motorRight.isMoving());
  }

  /**
   * Returns the average tacho count of the 2 motors
   */
  public float getAverageTacho() {
    return (motorLeft.getTachoCount() + motorRight.getTachoCount()) / 2f;
  }
}
