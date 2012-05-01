package penoplatinum.simulator.sensors;

import penoplatinum.map.MapUtil;
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
    int minimum = sim.getFreeDistance(simEntity.getPosX(), simEntity.getPosY(), (angle + 360) % 360);
    // TODO: reintroduce ? - removed to find Sonar detection bug (xtof)
    // this "abuses" our ability to make many sonar checks at once ?!
    // answer: This simulates the reality better. A sound wave gets sent out and bounces back.
    for (int i = -15; i < 16; i++) {
      int distance = sim.getFreeDistance(simEntity.getPosX(), simEntity.getPosY(), (angle + i + 360) % 360);
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
