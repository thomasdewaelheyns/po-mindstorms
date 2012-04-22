package penoplatinum.simulator.sensors;

import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class MotorState implements Sensor {

  public static final int MOTORSTATE_FORWARD = 1;
  public static final int MOTORSTATE_BACKWARD = 2;
  public static final int MOTORSTATE_STOPPED = 3;
  private Motor m;

  public MotorState(Motor m) {
    this.m = m;
  }

  @Override
  public int getValue() {
    if (!m.isMoving()) {
      return MOTORSTATE_STOPPED;
    }
    if (m.getDirection() == Motor.FORWARD) {
      return MOTORSTATE_FORWARD;
    }
    if (m.getDirection() == Motor.BACKWARD) {
      return MOTORSTATE_BACKWARD;
    }
    throw new RuntimeException("I M P O S S I B L E !");
  }

  @Override
  public void useSimulator(Simulator sim) {
    // the MotorState sensor only needs information from the motor.
  }

  @Override
  public void useSimulatedEntity(SimulatedEntity simEntity) {
    // the MotorState Sensor only needs information from the motor.
  }
}
