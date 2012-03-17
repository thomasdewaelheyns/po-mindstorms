package penoplatinum.movement;

import lejos.nxt.*;
import penoplatinum.Utils;
import penoplatinum.util.ExtendedVector;

public class RotationMovement {

    public static double CCW_afwijking = 1.01;
    public int SPEEDFORWARD = 400; //400
    public int SPEEDTURN = 250;
    public final int WIELOMTREK = 175; //mm
    public final int WIELAFSTAND = 160;//112mm
    public Motor motorLeft = Motor.C;
    public Motor motorRight = Motor.B;
    private boolean movementDisabled;
    private int tachoLeftStart;
    private int tachoRightStart;
    private ExtendedVector internalOrientation = new ExtendedVector();
    private ExtendedVector buffer = new ExtendedVector();

    private void abortMovement() {
        internalOrientation.set(getInternalOrientation());
        startMovement();
    }

    public ExtendedVector getInternalOrientation() {
        int tachoLeftDiff = motorLeft.getTachoCount() - tachoLeftStart;
        int tachoRightDiff = motorRight.getTachoCount() - tachoRightStart;
        float averageForward = (tachoLeftDiff + tachoRightDiff) / 2.0f;

        float inverse = averageForward * WIELOMTREK / 360 / 1000;

        buffer.setX((float) (inverse * Math.sin(Math.toRadians(internalOrientation.getAngle()))));
        buffer.setY((float) (inverse * Math.cos(Math.toRadians(internalOrientation.getAngle()))));

        averageForward = (tachoRightDiff - tachoLeftDiff) / 2.0f;

        inverse = (float) (averageForward * WIELOMTREK / WIELAFSTAND / Math.PI);



        buffer.setAngle(-inverse);
        buffer.add(internalOrientation);
        return buffer;
    }

    private void startMovement() {
        tachoLeftStart = motorLeft.getTachoCount();
        tachoRightStart = motorRight.getTachoCount();
    }

    public void setMovementDisabled(boolean movementDisabled) {
        this.movementDisabled = movementDisabled;
//        motorLeft.smoothAcceleration(false);
//        motorLeft.regulateSpeed(false);
//
//        motorLeft.setPower(50);
//
//        motorRight.smoothAcceleration(false);
//        motorRight.regulateSpeed(false);
//
//        motorRight.setPower(50);

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
        if (movementDisabled) {
            return;
        }
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
        if (movementDisabled) {
            return;
        }
        abortMovement();
        setSpeed(SPEEDTURN);
        angleCCW *= CCW_afwijking;

        int h = (int) (angleCCW * Math.PI * WIELAFSTAND / WIELOMTREK);
        motorLeft.rotate(h, true);
        motorRight.rotate(-h, true);
    }

    public void turn(boolean ccw) {
        if (movementDisabled) {
            return;
        }
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
        if (Math.abs(motorLeft.getSpeed() - speed) > 150) {
            //TODO: implement better acceleration in this class
            //Utils.Log("Large speed difference!");
            // Accelerate from 0
            //TODO: this was usefull! stop();
            //Wait for regulator to regulate
            //Utils.Sleep(200); //TODO: WARNING: removed
        }

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
