package PenoPlatinum;

import lejos.nxt.*;

public class RubenMovement implements IMovement {

    private Object waitLock = new Object();
    public static final int SPEEDFORWARD = 400;
    public static final int SPEEDTURN = 400;
    public final int WIELOMTREK = 175; //mm
    public final int WIELAFSTAND = 56;//mm
    public static final int SPEED = 350;
    public Motor motorLeft = Motor.C;
    public Motor motorRight = Motor.B;

    public void MoveStraight(double distance) {
        distance *= 1000;
        int r = (int) (distance * 360 / WIELOMTREK);
        motorLeft.setSpeed(SPEEDFORWARD);
        motorRight.setSpeed(SPEEDFORWARD);
        motorLeft.rotate(r, true);
        motorRight.rotate(r, true);
        Utils.Sleep(r*1500/SPEEDFORWARD);
    }

    public void TurnOnSpotCCW(double angle) {
        int h = (int) (angle * 2 * Math.PI * WIELAFSTAND / WIELOMTREK);
        motorLeft.rotate(h, true);
        motorRight.rotate(-h, true);
        try {
            Thread.sleep(h * 1500 / SPEEDTURN);
        } catch (Exception e) {
        }
    }

    public void TurnAroundWheel(double angle, boolean isLeft) {
        throw new UnsupportedOperationException("Not supported yet.");
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
}
