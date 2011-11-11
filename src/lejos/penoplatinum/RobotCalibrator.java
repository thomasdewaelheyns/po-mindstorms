/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.util.Stopwatch;
import penoplatinum.Angie;
import penoplatinum.Utils;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.WrappedLightSensor;

/**
 * This class is responsible for determining the robot's calibration data.
 * @author MHGameWork
 */
public class RobotCalibrator {

    private final Angie angie;

    public RobotCalibrator(Angie angie) {
        this.angie = angie;

    }

    /**
     * Returns the time difference in nanoseconds between when the left touch sensor hits the wall and when the right sensor hits the wall.
     * The robot first places itself perpendicular to a wall, then drives backward and forward again.
     * @return left-right
     */
    public int measureStraightDriveError() {

        Motor motorLeft = angie.getMotorLeft();
        Motor motorRight = angie.getMotorRight();
        final RotationMovement mov = angie.getMovement();

        mov.setSpeed(200);



        TouchSensor sensor = new TouchSensor(SensorPort.S4);


        searchWall();
        alignToWall(mov);

        Utils.Log("Start Wheel Speed Modifier calibration");
        Sound.playNote(Sound.PIANO, 392, 250);
        Sound.playNote(Sound.PIANO, 392, 250);

        mov.driveDistance(-200);



        mov.driveForward();

        long leftTime = -1;
        long rightTime = -1;

        while (leftTime < 0 || rightTime < 0) {
            if (angie.getTouchLeft().isPressed() && leftTime < 0) {
                leftTime = System.nanoTime();
            }
            if (angie.getTouchRight().isPressed() && rightTime < 0) {
                rightTime = System.nanoTime();
            }
        }


        return (int) (leftTime - rightTime);
    }

    private void alignToWall(final RotationMovement mov) {
        // keep orienting to wall until the robot is correctly aligned
        boolean noCorrection = false;
        while (!noCorrection) {

            mov.driveDistance(-10);
            noCorrection = orientToWall();
        }
    }

    private void searchWall() {
        angie.getMovement().driveForward();
        while (!angie.getTouchLeft().isPressed() && !angie.getTouchRight().isPressed()) {
            Utils.Sleep(10);
        }
    }

    /**
     * Returns true when  no correction to the robots orientation was necessary
     * @return 
     */
    private boolean orientToWall() {
        angie.getMovement().driveForward();
        boolean noCorrection = true;
        while (!angie.getTouchLeft().isPressed() && !angie.getTouchRight().isPressed()) {
            int i = 0;
            if (angie.getTouchLeft().isPressed()) {
                angie.getMotorLeft().stop();
                i++;
            }
            if (angie.getTouchRight().isPressed()) {
                angie.getMotorRight().stop();
                i++;
            }
            if (i == 1)
            {
                //Only one motor was stopped, correction occured
                noCorrection = false;
            }
        }
        
        return noCorrection;
    }

    public static void calibrateCCW(RotationMovement mov, WrappedLightSensor light) {
        mov.CCW_afwijking = 1;

        Button.waitForPress();
        double angle = 90;
        int turnCount = 0;
        int executeCount = 0;
        boolean blackNext = true;
        while (true) {
            if (mov.isStopped()) {
                Sound.beep();
                if (executeCount != turnCount) {
                    break;
                }
                executeCount += 2;
                mov.turnCCW(360.0);
            }
            if (blackNext && !light.isColor(WrappedLightSensor.Color.Brown)) {
                turnCount++;
                blackNext = false;
            }
            if (!blackNext && light.isColor(WrappedLightSensor.Color.Brown)) {
                blackNext = true;
            }
        }
        Utils.Log(turnCount + " " + executeCount);
        double afwijkingTeller = executeCount * 180;
        double afwijkingNoemer = executeCount * 180 + (turnCount - executeCount) * angle;
        double afwijking = afwijkingTeller / afwijkingNoemer;
        Utils.Log("" + afwijking);
        mov.CCW_afwijking *= afwijking;
        Utils.Log("mov: " + mov.CCW_afwijking);
    }
}
