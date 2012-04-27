package penoplatinum.simulator.sensors;

import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.util.Point;

public class Sonar implements Sensor {

  private Simulator sim;
  private SimulatedEntity simEntity;
  private Motor motor;

  public Sonar(Motor motor) {
    this.motor = motor;
  }

  @Override
  public int getValue() {
    int angle = (int) motor.getValue() + simEntity.getAngle();
    Point tile = simEntity.getCurrentTileCoordinates();
    Point pos = simEntity.getCurrentOnTileCoordinates();
    int minimum = sim.getFreeDistance(tile, pos, (angle + 360) % 360);
    // TODO: reintroduce ? - removed to find Sonar detection bug (xtof)
    // this "abuses" our ability to make many sonar checks at once ?!
    // answer: This simulates the reality better. A sound wave gets sent out and bounces back.
    for (int i = -15; i < 16; i++) {
      int distance = sim.getFreeDistance(tile, pos, (angle + i + 360) % 360);
      minimum = Math.min(minimum, distance);
      // System.out.println("minimum: "+minimum+" "+i);
    }
    return minimum > 90 ? 255 : minimum;
  }

  @Override
  public void useSimulator(Simulator sim) {
    this.sim = sim;
  }

  @Override
  public void useSimulatedEntity(SimulatedEntity simEntity) {
    this.simEntity = simEntity;
  }
}
