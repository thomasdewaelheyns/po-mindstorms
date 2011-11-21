package penoplatinum.sensor;

import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;

/**
 * Abstraction for a new type of sensor: a sonar mounted on top of a motor.
 * @author MHGameWork
 */
public class RotatingSonarSensor {

    private Motor motor;
    private final UltrasonicSensor sensor;
    private int startTacho;
    private int endTacho;
    private boolean first;

    public RotatingSonarSensor(Motor motor, UltrasonicSensor sensor) {
        this.motor = motor;
        this.sensor = sensor;
    }

    public float getDistance() {
        return sensor.getDistance();
    }

    public void updateSonarMovement() {
        if (motor.isMoving()) {
            return;
        }

        first = !first;
        if (first) {
            motor.rotateTo(endTacho, true);
        } else {
            motor.rotateTo(startTacho, true);

        }
    }

    public Motor getMotor() {
        return motor;
    }

    public UltrasonicSensor getSensor() {
        return sensor;
    }
}
