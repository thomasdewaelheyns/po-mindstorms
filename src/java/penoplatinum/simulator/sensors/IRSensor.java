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
    if(distanceToPacman > 5000) return 0;

    // Checks if there is a wall in the way that prevents vision of the pacman
    if(distanceToWall <= distanceToPacman) return 0;

    // Checks in which zone the detection occurs
    if((actualAngle <= 15) || (actualAngle> 345)) return 5;
    if((actualAngle <= 45) && (actualAngle> 15)) return 6;
    if((actualAngle <= 75) && (actualAngle> 45)) return 7;
    if((actualAngle <= 105) && (actualAngle> 55)) return 8;
    if((actualAngle <= 135) && (actualAngle> 105)) return 9;
    if((actualAngle <= 345) && (actualAngle> 315)) return 4;
    if((actualAngle <= 315) && (actualAngle> 285)) return 3;
    if((actualAngle <= 285) && (actualAngle> 255)) return 2;
    if((actualAngle <= 255) && (actualAngle> 225)) return 1;

    // if not returned till now, it means the detection is in the "blind spot" of the ir sensor
    return 0;
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
