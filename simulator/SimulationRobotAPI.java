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
    return this.simulator.getSensorValues();
  }

}
