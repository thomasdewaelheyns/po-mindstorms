/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import lejos.nxt.Motor;
import lejos.nxt.TouchSensor;
import penoplatinum.movement.AngieCalibrationData;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.RotatingSonarSensor;
import penoplatinum.sensor.WrappedLightSensor;

/**
 * Responsible for providing access to the hardware of the robot
 * @author MHGameWork
 */
public class Angie {

    private Motor motorLeft;
    private Motor motorRight;
    private TouchSensor touchLeft;
    private TouchSensor touchRight;
    private WrappedLightSensor light;
    private RotatingSonarSensor sonar;
    private AngieCalibrationData calibrationData;
    private RotationMovement movement;

    public RotationMovement getMovement() {
        return movement;
    }

    public AngieCalibrationData getCalibrationData() {
        return calibrationData;
    }

    public WrappedLightSensor getLight() {
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
        return touchLeft;
    }

    public TouchSensor getTouchRight() {
        return touchRight;
    }
}
