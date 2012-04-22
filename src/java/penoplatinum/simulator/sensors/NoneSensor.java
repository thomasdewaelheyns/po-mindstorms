package penoplatinum.simulator.sensors;

import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class NoneSensor implements Sensor {

  @Override
  public int getValue() {
    return 0;
  }

  @Override
  public void useSimulator(Simulator sim) {
  }

  @Override
  public void useSimulatedEntity(SimulatedEntity simEntity) {
  }
}
