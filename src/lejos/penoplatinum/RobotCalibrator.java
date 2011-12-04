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
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.WrappedLightSensor;

/**
 * This class is responsible for determining the robot's calibration data.
 * @author: Team Platinum
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
            if (i == 1) {
                //Only one motor was stopped, correction occured
                noCorrection = false;
            }
        }

        return noCorrection;
    }

    /**
     * Returns the number of tacho's required for the motor's to turn, to achieve a given precision for the robot's rotation
     * @param requiredAccuracy Require this method to calibrate to given maximum degrees error
     */
    public void calculateTurnTachos(float requiredAccuracy) {


        // This is the error in degrees when turning 360 degrees using this calibration method
        int angular360error = 10;

        int required360s = (int) (angular360error / requiredAccuracy);

        while (required360s > 0) {
            if (angie.getMovement().isStopped()) {
                angie.getMovement().turnAngle(10000);
            }
        }

        int totalTachos = 0; //TODO: result of calibration

        float tachosPerDegree = totalTachos / 360 / required360s;



        RotationMovement mov = angie.getMovement();
        WrappedLightSensor light = angie.getLight();



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
                mov.turnAngle(360.0);
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

    /**
     * 
     * @return 
     */
    public float measureTachosPerMeter(int numberTimes) {
        final int panelsize = 80;
        
        angie.getMovement().driveForward();
        waitUntilBarcodeEnd();
        float startTacho = angie.getMovement().getAverageTacho();
        waitUntilBarcodeEnd();
        float endTacho = angie.getMovement().getAverageTacho();
        
        angie.getMovement().driveBackward();

        return (endTacho - startTacho) / panelsize;
    }

    private void waitUntilBarcodeEnd() {
        
        
    }
}
