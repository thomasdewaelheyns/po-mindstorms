package penoplatinum.simulator.sensors;

import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class IRSensor implements Sensor {
  Simulator sim;
  SimulatedEntity simEntity;

  @Override
  public int getValue() {
    
    // calculates the angles and distances needed
    SimulatedEntity pacman = sim.getPacMan();
    double dx = pacman.getPosX() - simEntity.getPosX();
    double dy = pacman.getPosY() - simEntity.getPosY();
    double angleToNorth = Math.atan(dx/dy)/Math.PI*180;
    double actualAngle = (angleToNorth+simEntity.getDir()+360) % 360;
    int distanceToWall = sim.getFreeDistance(simEntity.getCurrentTileCoordinates(), simEntity.getCurrentOnTileCoordinates(), (int) angleToNorth);
    int distanceToPacman = (int)Math.sqrt(dx*dx + dy*dy);

    // checks if the distance to the pacman is higher than 5 meter
    if(distanceToPacman > 500) return 0;

    // Checks if there is a wall in the way that prevents vision of the pacman
    if(distanceToWall <= distanceToPacman) return 0;

    int angle = (int) ((actualAngle+165)%360/30);
    if(angle>9){
      return 0;
    } else {
      return angle;
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
