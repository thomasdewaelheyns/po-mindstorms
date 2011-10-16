/**
 * SimulationRobotAPI
 * 
 * Implements the RobotAPI interface to track the robot's actions and return
 * this information to the Simulator.
 * 
 * Author: Team Platinum
 */

class SimulationRobotAPI implements RobotAPI {

  private Simulator simulator;

  public void setSimulator( Simulator simulator ) {
    this.simulator = simulator;
  }

  public void move( float distance ) {
    this.simulator.moveRobot( distance );
  }

  public void turn( int angle ) {
    this.simulator.turnRobot( angle );
  }

}
