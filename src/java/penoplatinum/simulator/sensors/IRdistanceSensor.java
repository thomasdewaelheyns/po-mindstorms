package penoplatinum.simulator.sensors;

import penoplatinum.simulator.RobotEntity;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class IRdistanceSensor implements Sensor {

  private Simulator sim;
  private SimulatedEntity simEntity;

  @Override
  public int getValue() {
    // calculates the angles and distances needed
    RobotEntity pacman = sim.getPacMan();
    double dx = pacman.getPosX() - simEntity.getPosX();
    double dy = pacman.getPosY() - simEntity.getPosY();
    double angleToNorth = Math.atan(dx / dy) / Math.PI * 180;
    double actualAngle = (angleToNorth + simEntity.getDir() + 360) % 360;
    int distanceToWall = sim.getFreeDistance(simEntity.getCurrentTileCoordinates(), simEntity.getCurrentOnTileCoordinates(), (int) angleToNorth);
    int distanceToPacman = (int) Math.sqrt(dx * dx + dy * dy);

    if(distanceToPacman<40){
      return 170;
    } else {
      return 0;
    }
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
