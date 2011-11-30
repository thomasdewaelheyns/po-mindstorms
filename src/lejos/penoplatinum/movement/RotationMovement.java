package penoplatinum.movement;

import lejos.nxt.*;
import penoplatinum.Utils;

public class RotationMovement {

    public static double CCW_afwijking = 1.01;
    public int SPEEDFORWARD = 500;
    public int SPEEDTURN = 250;
    public final int WIELOMTREK = 175; //mm
    public final int WIELAFSTAND = 160;//112mm
    public Motor motorLeft = Motor.C;
    public Motor motorRight = Motor.B;
    private boolean movementDisabled;

    public void setMovementDisabled(boolean movementDisabled) {
        this.movementDisabled = movementDisabled;
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
