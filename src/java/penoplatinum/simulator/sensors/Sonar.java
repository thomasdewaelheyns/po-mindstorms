package penoplatinum.simulator.sensors;

import java.awt.Point;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class Sonar implements Sensor{
  private Simulator sim;
  private SimulatedEntity simEntity;

  public Sonar() {
  }

  @Override
  public int getValue() {
    int angle = (int) simEntity.getSensorValues()[Model.M3] + simEntity.getAngle();
    Point tile = simEntity.getCurrentTileCoordinates();
    Point pos = simEntity.getCurrentOnTileCoordinates();
    int minimum = sim.getFreeDistance(tile, pos, (angle + 360) % 360);
    // TODO: reintroduce ? - removed to find Sonar detection bug (xtof)
    // this "abuses" our ability to make many sonar checks at once ?!
     for (int i = -15; i < 16; i++) {
       int distance = sim.getFreeDistance(tile, pos, (angle+i+360)%360);
       minimum = Math.min(minimum, distance);
     }
    // this.sensorValues[Model.S3] = minimum > 90 ? 255 : minimum;
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
