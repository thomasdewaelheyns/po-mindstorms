/**
 * Simulator
 * 
 * Accepts a robot, track and view and simulates the how the robot would run
 * this track (in a perfect world). Its main use is to test the theory behind
 * Navigator implementations, without the extra step onto the robot.
 * 
 * Future Improvements: Add support for multiple robots
 * 
 * Author: Team Platinum
 */
 
class Simulator {

  private SimulationView view;    // a view to display the simulation
  // private Track track;            // the track that the robot will run on
  private SimulationRobotAPI robotAPI;      // the API used to access hardware
  private SimulationRobotAgent robotAgent;  // the communication layer

  private Robot robot;            // the actual robot

  private int positionX;          // the position of the robot in the world
  private int positionY;          //   expressed in X,Y coordinates
  private int direction;          //   and a direction its facing

  // main constructor, no arguments, Simulator is selfcontained
  public Simulator() {
    this.setupSimulationEnvironment();
  }

  /**
   * The simulation environment provides an implementation of the RobotAPI
   * and RobotAgent which are wired to the Simulator. This allows any default
   * robot to be tested without modification.
   */
  private void setupSimulationEnvironment() {
    this.robotAPI = new SimulationRobotAPI();
    this.robotAPI.setSimulator(this);         // provide callback object);
    this.robotAgent = new SimulationRobotAgent();
    this.robotAgent.setSimulator(this);       // provide callback object);
  }

  /**
   * On a SimulationView, the Simulator can visually show what happens during
   * the simulation.
   */
  public Simulator displayOn(SimulationView view) {
    this.view = view;
    return this;
  }

  /**
   * A track can be provided. This will determine what information the 
   * Simulator will send to the robot's sensors, using the SimulationRobotAPI.
   */
  //public Simulator useTrack(Track track) {
  //  this.track = track;
  //  return this;
  //}
  
  /**
   * A robot is put on the track - as in the real world - on a certain place
   * and in a given direction.
   * The Simulator also instruments the robot with a RobotAPI and sets up
   * the RobotAgent to interact with the robot.
   */
  public Simulator putRobotAt(Robot robot, int x, int y, int direction) {
    this.robot = robot;
    this.positionX = x;
    this.positionY = y;
    this.direction = direction;
    
    // we provide the robot with our SimulationAPI
    this.robot.useRobotAPI( this.robotAPI );

    // we connect our SimulationRobotAgent to the robot
    this.robotAgent.setRobot( this.robot );

    return this;
  }
  
  public Simulator moveRobot( float movement ) {
    double rads = Math.toRadians(this.direction);

    // convert movement in meters to (pixel-based) units
    // 20cm = 40px / 1m = 200px / 1cm = 2px
    int distance = (int) ( movement * 100.0 * 2.0 );

    // move in steps of 1cm/2px
    // TODO: we now only deal with movements that are defined up to cm's.
    int step = 2;
    int dx   = (int)(Math.cos(rads) * step);
    int dy   = (int)(Math.sin(rads) * step);
    int steps = distance / step;
    for( ; steps>0; steps-- ) {
      this.positionX += dx;
      this.positionY += dy;
      this.refreshView();
      // TODO: add in-move collision checks
      // this.updateSensors();
      // this.updateStatus();
      // TODO: This might cause this loop to be interrupted
    }
    return this;
  }
  
  public Simulator turnRobot( int angle ) {
    this.direction = ( this.direction + angle ) % 360;
    this.refreshView();
    return this;
  }

  private void refreshView() {
    this.view.updateRobot( this.positionX, this.positionY, this.direction );
  }
  
  /**
   * Allows the end-user to send commands throught the communication layer
   * to the Robot. In the real world this is done through the RobotAgent,
   * which here is being provided and controled by the Simulator.
   */
  public Simulator send( String cmd ) {
    this.robotAgent.receive( cmd );
    return this;
  }

  /**
   * This processes status-feedback from the RobotAgent, extracted from the
   * Model and Navigator.
   */ 
  public Simulator receive( String status ) {
    // this is normally used by the PC client to implement a View of the 
    // Robot's mind.
    return this;
  }
  
  /**
   * This starts the actual simulation, which will start the robot and the 
   * agent.
   */
  public Simulator run() {
    this.robotAgent.run();
    this.robot.run();
    return this;
  }

}
