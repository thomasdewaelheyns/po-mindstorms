package penoplatinum.simulator.sensors;

import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.Tickable;

/**
 * Motor
 * 
 * A software version of a Motor. It allows for changing its value through
 * a series of "ticks" and provides its current value.
 * 
 * @author: Team Platinum
 */
public class Motor implements Tickable, Sensor {

  public static final int FORWARD = 1;
  public static final int BACKWARD = -1;
  private String label = "unknown";
  private int speed = 250;
  private int tacho = 0;
  private Integer targetTacho = null;
  private int direction = Motor.FORWARD;

  public int getDirection() {
    return direction;
  }

  public boolean isMoving() {
    return targetTacho != null;
  }

  public Motor setLabel(String label) {
    this.label = label;
    return this;
  }

  public String getLabel() {
    return this.label;
  }

  public Motor setSpeed(int speed) {
    this.speed = speed;
    return this;
  }

  public Motor start() {
    this.rotateTo(Integer.MAX_VALUE * this.direction);
    return this;
  }

  public Motor stop() {
    this.targetTacho = null;
    return this;
  }

  public Motor rotateTo(int tacho) {
    this.direction = tacho >= this.tacho ? Motor.FORWARD : Motor.BACKWARD;
    this.targetTacho = tacho;
    return this;
  }

  public Motor rotateBy(int angle) {
    this.rotateTo(this.tacho + angle);
    return this;
  }

  public Motor goForward() {
    this.direction = Motor.FORWARD;
    return this;
  }

  public Motor goBackward() {
    this.direction = Motor.BACKWARD;
    return this;
  }

  public Motor toggleDirection() {
    this.direction *= -1;
    return this;
  }

  // Tickable
  public void tick(double elapsed) {
    if (this.targetTacho == null) {
      return;
    }

    // apply the currentSpeed
    double change = (this.speed * elapsed) * this.direction;

    this.tacho += change;

    // stop at targetTacho
    if ((this.direction == Motor.FORWARD && this.tacho >= this.targetTacho)
            || (this.direction == Motor.BACKWARD && this.tacho <= this.targetTacho)) {
      this.tacho = this.targetTacho;
      this.targetTacho = null;
    }
  }

  // Sensor
  @Override
  public int getValue() {
    return (int) this.tacho;
  }

  @Override
  public void useSimulator(Simulator sim) {
    // a motor does not need information from the world
  }

  @Override
  public void useSimulatedEntity(SimulatedEntity simEntity) {
    // a motor does not need information from the entity
  }
}
