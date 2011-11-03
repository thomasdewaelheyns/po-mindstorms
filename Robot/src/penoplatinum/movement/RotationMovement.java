package penoplatinum.movement;

import lejos.nxt.*;
import penoplatinum.Utils;

public class RotationMovement implements IMovement {

    public int SPEEDFORWARD = 125;
    public int SPEEDTURN = 250;
    public final int WIELOMTREK = 175; //mm
    public final int WIELAFSTANDMIDDEN = 56;//mm
    public final int WIELAFSTAND = 116;//112mm
    public Motor motorLeft = Motor.C;
    public Motor motorRight = Motor.B;

    public void MoveStraight(double distance) {
        MoveStraight(distance, true);
    }

    public void MoveStraight(double distance, boolean block) {
        distance *= 1000;
        distance /= 0.99;
        int r = (int) (distance * 360 / WIELOMTREK);
        motorLeft.setSpeed(SPEEDFORWARD);
        motorRight.setSpeed(SPEEDFORWARD);
        motorLeft.rotate(r, true);
        motorRight.rotate(r, !block);
    }

    public void TurnOnSpotCCW(double angle) {
        motorLeft.setSpeed(SPEEDTURN);
        motorRight.setSpeed(SPEEDTURN);
        angle /= 0.99;
        int h = (int) (angle * 2 * Math.PI * WIELAFSTANDMIDDEN / WIELOMTREK);
        motorLeft.rotate(h, true);
        motorRight.rotate(-h, false);
    }

    public void TurnAroundWheel(double angle, boolean aroundLeft) {
        int h = (int) (angle * 2 * Math.PI * WIELAFSTAND / WIELOMTREK);
        if (aroundLeft) {
            motorRight.rotate(h);
        } else {
            motorLeft.rotate(h);
        }
    }

    public void CalibrateWheelCircumference() {
        TouchSensor sensor = new TouchSensor(SensorPort.S4);


        System.out.println("Incoming");
        motorLeft.setSpeed(SPEEDFORWARD);
        motorRight.setSpeed(SPEEDFORWARD);
        motorLeft.forward();
        motorRight.forward();

        while (!sensor.isPressed()) {
        }

        System.out.println("Starting in 3 second");
        motorLeft.stop();
        motorRight.stop();
        Utils.Sleep(3000);

        System.out.println("Weeeeee");

        motorLeft.setSpeed(SPEEDFORWARD);
        motorRight.setSpeed(SPEEDFORWARD);
        motorLeft.forward();
        motorRight.forward();

        int tachoL = motorLeft.getTachoCount();
        int tachoR = motorRight.getTachoCount();
        while (!sensor.isPressed()) {
        }
        System.out.println("Auch!");

        motorLeft.stop();
        motorRight.stop();

        int diffL = motorLeft.getTachoCount() - tachoL;
        int diffR = motorRight.getTachoCount() - tachoR;

        System.out.println(diffL);
        System.out.println(diffR);

        System.out.println(1600 * 360 / diffL);
        System.out.println(1600 * 360 / diffR);
        Button.waitForPress();

    }

    public void CalibrateWheelDistance() {
    }

    public void Stop() {
        motorLeft.stop();
        motorRight.stop();
    }

    /**
     * Returns the average tacho count of the 2 motors
     */
    public float getAverageTacho() {
        return (motorLeft.getTachoCount() + motorRight.getTachoCount()) / 2f;
    }
}
