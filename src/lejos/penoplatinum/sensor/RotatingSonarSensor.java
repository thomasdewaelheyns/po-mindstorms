package penoplatinum.sensor;

import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.Utils;

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
        
        if (motor.getTachoCount() != 0) {
            Sound.playNote(Sound.PIANO, 220,1);
            Utils.Log("Initial tacho of the motor should've been 0!");
            motor.resetTachoCount();
        }
        
        startTacho = motor.getTachoCount() + 45;
        endTacho = motor.getTachoCount() - 45;
    }

    public float getDistance() {
        int dist = sensor.getDistance();

        return dist;

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
