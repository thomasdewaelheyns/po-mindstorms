package penoplatinum.simulator;

/**
 * SimulationRobotAPI
 * 
 * Implements the RobotAPI interface to track the robot's actions and return
 * this information to the Simulator.
 * 
 * Author: Team Platinum
 */

public class SimulationRobotAPI implements RobotAPI {

  private Simulator simulator;
  private boolean lastWasPositive = true;

  public void setSimulator( Simulator simulator ) {
    this.simulator = simulator;
    simulator.turnMotorToBlocking(135);
  }

  public void move( double distance ) {
    this.simulator.moveRobot( distance );
  }

  public void turn( double angle ) {
    this.simulator.turnRobot( angle );
  }

  public void stop() {
    this.simulator.stopRobot();
  }
  
  private void restartSonarMotor(){
    if(!simulator.sonarMotorIsMoving()){
      int angle = (lastWasPositive?-270:270);
      simulator.turnMotorTo(angle);
      lastWasPositive = !lastWasPositive;
    }
  }

  public int[] getSensorValues() {
    int[] out = new int[this.simulator.getSensorValues().length];
    restartSonarMotor();
    for(int i=0;i<out.length;i++){
        out[i] = (int) this.simulator.getSensorValues()[i];
    }
    return out;
  }

}
