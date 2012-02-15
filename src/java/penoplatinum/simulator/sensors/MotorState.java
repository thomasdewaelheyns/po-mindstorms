package penoplatinum.simulator.sensors;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Motor;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class MotorState implements Sensor{
  private Motor m;

  public MotorState(Motor m) {
    this.m = m;
  }

  @Override
  public int getValue() {
    if (!m.isMoving()) {
      return Model.MOTORSTATE_STOPPED;
    }
    if (m.getDirection() == Motor.FORWARD) {
      return Model.MOTORSTATE_FORWARD;
    }
    if (m.getDirection() == Motor.BACKWARD) {
      return Model.MOTORSTATE_BACKWARD;
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
