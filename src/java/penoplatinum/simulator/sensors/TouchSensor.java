package penoplatinum.simulator.sensors;

import java.awt.Point;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class TouchSensor implements Sensor {
  private Simulator sim;
  private SimulatedEntity simEntity;
  private int angle;

  public TouchSensor(int angle) {
    this.angle = angle;
  }
  
  @Override
  public int getValue() {
    angle = (simEntity.getAngle() + angle) % 360;
    Point tile = simEntity.getCurrentTileCoordinates();
    Point pos = simEntity.getCurrentOnTileCoordinates();
    int distance = sim.getFreeDistance(tile, pos, angle);

    /*if (distance < SimulatedEntity.BUMPER_LENGTH_ROBOT) {
      this.sensorValues[sensorPort] = 50;
    } else {
      this.sensorValues[sensorPort] = 0;
    }/**/
    
    return (distance < SimulatedEntity.BUMPER_LENGTH_ROBOT? 50 : 0);
  }

  @Override
  public void useSimulator(Simulator sim) {
    this.sim=sim;
  }

  @Override
  public void useSimulatedEntity(SimulatedEntity simEntity) {
    this.simEntity = simEntity;
  }
}
