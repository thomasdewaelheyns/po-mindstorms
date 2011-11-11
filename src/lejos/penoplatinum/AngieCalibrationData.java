/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

/**
 *
 * @author MHGameWork
 */
public class AngieCalibrationData {
    /**
     * Average radius of both wheels
     */
    private int wheelRadius;
    /**
     * Distance along the wheel axis between the 2 wheels
     */
    private int wheelDistance;
    /**
     * The speed modifier applied to the wheels.
     * wheelSpeedModifier = (speedRight - speedLeft) / speedAverage
     * speedAverage = (speedRight + speedLeft) / 2
     */
    private int wheelSpeedModifier;
    
    
    /**
     * This array contains a mapping from sonar data -> real world distance
     * real world distance = sonarCalibrationData[sonarSample * sonarCalibrationResolution];
     */
    private float[] sonarCalibrationData;
    /**
     * Contains the sample resolution
     * real world distance = sonarCalibrationData[sonarSample * sonarCalibrationResolution];
     */
    private int sonarCalibrationResolution;

    public float[] getSonarCalibrationData() {
        return sonarCalibrationData;
    }

    public void setSonarCalibrationData(float[] sonarCalibrationData) {
        this.sonarCalibrationData = sonarCalibrationData;
    }

    public int getSonarCalibrationResolution() {
        return sonarCalibrationResolution;
    }

    public void setSonarCalibrationResolution(int sonarCalibrationResolution) {
        this.sonarCalibrationResolution = sonarCalibrationResolution;
    }

    public int getWheelDistance() {
        return wheelDistance;
    }

    public void setWheelDistance(int wheelDistance) {
        this.wheelDistance = wheelDistance;
    }

    public int getWheelRadius() {
        return wheelRadius;
    }

    public void setWheelRadius(int wheelRadius) {
        this.wheelRadius = wheelRadius;
    }

    public int getWheelSpeedModifier() {
        return wheelSpeedModifier;
    }

    public void setWheelSpeedModifier(int wheelSpeedModifier) {
        this.wheelSpeedModifier = wheelSpeedModifier;
    }
    
    
    
}
