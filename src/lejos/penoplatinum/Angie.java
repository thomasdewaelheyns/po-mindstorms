/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.RotatingSonarSensor;
import penoplatinum.sensor.WrappedLightSensor;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.RobotAPI;

/**
 * Responsible for providing access to the hardware of the robot
 * @author MHGameWork
 */
public class Angie implements RobotAPI{

    private Motor motorLeft;
    private Motor motorRight;
    private TouchSensor touchLeft;
    private TouchSensor touchRight;
    private WrappedLightSensor light;
    private RotatingSonarSensor sonar;
    private AngieCalibrationData calibrationData;
    private RotationMovement movement;
    
    private static final int sensorNumberTouchLeft = Model.S2;
    private static final int sensorNumberTouchRight = Model.S1;
    private static final int sensorNumberLight = Model.S4;
    private static final int sensorNumberSonar = Model.S3;
    private static final int sensorNumberMotorLeft = Model.M2;
    private static final int sensorNumberMotorRight = Model.M3;
    private static final int sensorNumberMotorSonar = Model.M1;
    
    
    public Angie()
    {
        
        motorLeft = Motor.B;
        motorRight = Motor.C;
        
        touchLeft = new TouchSensor(SensorPort.S2);
        touchLeft = new TouchSensor(SensorPort.S1);
        light = new WrappedLightSensor(null, null);
        sonar = new RotatingSonarSensor(Motor.A, new UltrasonicSensor(SensorPort.S3));
        
    }

    
    public void step()
    {
        sonar.updateSonarMovement();
    }
    
    
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
        //TODO: GC
        int[] values = new int[4+3];
        values[sensorNumberMotorLeft] = motorLeft.getTachoCount();
        values[sensorNumberMotorRight] = motorRight.getTachoCount();
        values[sensorNumberMotorSonar] = sonar.getMotor().getTachoCount();
        
       
        values[sensorNumberTouchLeft] = touchLeft.isPressed() ? 255:0;
        values[sensorNumberTouchRight] = touchRight.isPressed() ? 255:0;
        values[sensorNumberLight] =  light.getLightValue();
        values[sensorNumberSonar] = (int)sonar.getDistance();
        
        return values;
    }
}
