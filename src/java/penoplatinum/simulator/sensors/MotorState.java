package penoplatinum.simulator.sensors;

import penoplatinum.Config;

import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class MotorState implements Sensor {

  private Motor m;

  public MotorState(Motor m) {
    this.m = m;
  }

  @Override
  public int getValue() {
    if (!m.isMoving()) {
      return Config.MOTORSTATE_STOPPED;
    } else if (m.getDirection() == Motor.FORWARD) {
      return Config.MOTORSTATE_FORWARD;
    } else if (m.getDirection() == Motor.BACKWARD) {
      return Config.MOTORSTATE_BACKWARD;
    } else {
      throw new RuntimeException("I M P O S S I B L E !");
    }
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
