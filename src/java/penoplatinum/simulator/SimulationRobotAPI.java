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
  private int sonarAngle = 270;

  public void setSimulator( Simulator simulator ) {
    this.simulator = simulator;
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
  
  public int[] getSensorValues() {
    // we inject the sweeping behaviour of the sonar at this point, because
    // it allows us to simulate an autonous thread without requiring one.
    this.restartSonarMotor();

    // put all sensorvalues in an array and return them
    int[] out = new int[this.simulator.getSensorValues().length];
    for(int i=0;i<out.length;i++){
        out[i] = (int) this.simulator.getSensorValues()[i];
    }
    return out;
  }

  private void restartSonarMotor(){
    if( ! simulator.sonarMotorIsMoving() ) {
      this.sonarAngle *= -1;
      simulator.turnMotorTo(this.sonarAngle);
    }
  }

}
