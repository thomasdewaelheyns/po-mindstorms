package penoplatinum.simulator.sensors;

import penoplatinum.util.Utils;
import penoplatinum.simulator.RobotEntity;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.Simulator;

public class IRdistanceSensor implements Sensor {

  private Simulator sim;
  private SimulatedEntity simEntity;
  private int centerAngle;
  private final static int VIEW_ANGLE = 90;

  public IRdistanceSensor(int centerAngle) {
    this.centerAngle = centerAngle;
  }

  @Override
  public int getValue() {
    // calculates the angles and distances needed
    RobotEntity pacman = sim.getPacMan();
    if (pacman == null) {
      return 0;
    }
    double dx = pacman.getPosX() - simEntity.getPosX();
    double dy = pacman.getPosY() - simEntity.getPosY();
    double angleToNorth = 0;
    if (dy != 0) {
      angleToNorth = Math.atan(dx / dy) / Math.PI * 180;
      if (dy > 0) {
        angleToNorth += 180;
      }
    } else if (dx > 0) {
      angleToNorth = 270;
    } else {
      angleToNorth = 90;
    }
    angleToNorth += 360;
    angleToNorth %= 360;
    
    double actualAngle = (angleToNorth - simEntity.getDir() + 720) % 360;
    //System.out.println(dx+", "+dy+", "+angleToNorth+", "+simEntity.getDir()+", "+actualAngle);
    final int changedOriginVector = (int) (angleToNorth+90)%360;
    int distanceToWall = sim.getFreeDistance(simEntity.getCurrentTileCoordinates(), simEntity.getCurrentOnTileCoordinates(), changedOriginVector);
    int distanceToPacman = (int) Math.sqrt(dx * dx + dy * dy);
    
    if(distanceToWall<distanceToPacman){
      return 0;
    }

    actualAngle = Utils.ClampLooped(actualAngle, -180, 180);
    int minAngle = Utils.ClampLooped(centerAngle-VIEW_ANGLE/2, -180, 180);
    int maxAngle = Utils.ClampLooped(centerAngle+VIEW_ANGLE/2, -180, 180);
    if(actualAngle<minAngle){
      return 0;
    }
    if(actualAngle>maxAngle){
      return 0;
    }
    if(distanceToPacman>=60){
      return 0;
    }
//    System.out.println(Utils.ClampLooped(centerAngle, -180, 180));
    return 170;
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