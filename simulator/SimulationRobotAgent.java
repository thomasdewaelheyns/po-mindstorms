/**
 * SimulationRobotAgent
 * 
 * Implements the RobotAgent interface for use in the SimulationEnvironment.
 * It provides additional methods for the Simulator to set up links between
 * them.
 * 
 * Author: Team Platinum
 */

class SimulationRobotAgent implements RobotAgent {

  private Robot robot;
  private Simulator simulator;

  public void setRobot( Robot robot ) {
    this.robot = robot;
  }
  
  public void run() {
    // no threading yet
  }

  public void receive( String cmd ) {
    this.robot.processCommand( cmd );
  }
  
  public void send( String status ) {
    this.simulator.receive( status );
  }
  
  public void setSimulator( Simulator simulator ) {
    this.simulator = simulator;
    this.simulator.receive( "SimulationAgent Ready" );
  }

}
