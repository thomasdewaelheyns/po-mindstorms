package penoplatinum.simulator;

/**
 * SimulationRobotAgent
 * 
 * Implements the RobotAgent interface for use in the SimulationEnvironment.
 * It provides additional methods for the Simulator to set up links between
 * them.
 * 
 * Author: Team Platinum
 */

public class SimulationRobotAgent implements RobotAgent {

  private Robot robot;
  private Simulator simulator;

  public void setRobot( Robot robot ) {
    this.robot = robot;
  }
  
  public void run() {
    // in the Simulator, we don't use threading
  }

  public void receive( String cmd ) {
    this.robot.processCommand( cmd );
  }
  
  public void send( String status ) {
    this.simulator.receive( status );
  }

  // this method is not part of the RobotAgent interface, but is used by
  // the simulator to replace the outgoing bluetooth communication stack
  public void setSimulator( Simulator simulator ) {
    this.simulator = simulator;
    this.send( "SimulationAgent Ready" );
  }

}
