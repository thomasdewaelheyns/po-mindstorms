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

  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction its facing
  
  private int[] sensorValues = new int[7]; // the current state of the sensors

  private int steps;              // the number of steps still to do
  private double dx, dy, dr;      // the difference for x, y and rotation
  
  private int currentMovement;    // movement that is being stepped
  private int lastChangeM1 = 0;    // the last change on Motor 1
  private int lastChangeM2 = 0;    // the last change on Motor 2
  private int lastChangeM3 = 0;    // the last change on Motor 3

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
  
  public Simulator moveRobot( double movement ) {
    this.currentMovement = Navigator.MOVE;
    
    // our direction 0 is pointing North, while it results in East, so add
    // 90 degrees
    double rads = Math.toRadians(this.direction + 90);

    // convert movement in meters to (pixel-based) units
    // 20cm = 120px / 1m = 600px / 1cm = 6px
    int distance = (int)(movement * 100.0 * 6.0);
    int direction = 1;
    if( distance < 0 ) {
      direction = -1;
      distance *= -1;
    }

    // move in steps of 1cm/2px
    // TODO: we now only deal with movements that are defined up to cm's.
    double step = 6.0;
    this.dx   = Math.cos(rads) * step * direction;
    this.dy   = Math.sin(rads) * step * direction;
    this.steps = (int)(distance / step);
    
    return this;
  }
  
  public Simulator turnRobot( double angle ) {
    this.currentMovement = Navigator.TURN;
    
    // turn in steps of 3 degrees
    this.dr   = 3.0;
    this.steps = (int)(angle / this.dr);

    return this;
  }
  
  public Simulator stopRobot() {
    this.currentMovement = Navigator.STOP;
    return this;
  }

  private void refreshView() {
    this.view.updateRobot( (int)this.positionX, (int)this.positionY,
                           (int)this.direction );
  }
  
  /**
   * Performs the next step in the movement currently executed by the robot
   */ 
  private void step() {
    this.lastChangeM1 = 0;
    this.lastChangeM2 = 0;
    // process the next step in the movement that is currently being performed
    switch( this.currentMovement ) {
      case Navigator.MOVE:
        if( this.steps-- > 0 ) {
          this.positionX += this.dx;
          this.positionY -= this.dy;
          // TODO: fix this to be correct towards actual change
          this.lastChangeM1 = 1;
          this.lastChangeM2 = 1;
        }
        break;
      case Navigator.TURN:
        if( this.steps-- > 0 ) {
          this.direction = ( this.direction + this.dr ) % 360;
          // TODO: fix this to be correct towards dimensions of robot
          this.lastChangeM1 = 1;
          this.lastChangeM2 = -1;
        }
        break;
      case Navigator.STOP:
        this.currentMovement = Navigator.NONE;
        break;
      case Navigator.NONE:
      default:
        // do nothing
    }
    
    // based on the new location, determine the value of the different sensors
    this.updateSensorValues();

    // always refresh our SimulationView
    this.refreshView();
  }
  
  private void updateSensorValues() {
    // TODO: improve this algorithm and make max configurable ;-)
    int dist = 20;
    int maxx = 320;
    int maxy = 300;
    
    if( this.positionX < dist || this.positionX > maxx - dist 
        ||
        this.positionY < dist || this.positionY > maxy - dist )
    {
      // the robot is "close" to a wall, push the front pushsensor
      // TODO: make this relative to the actual distance ;-)
      this.sensorValues[Model.S1] = 50;
    } else {
      this.sensorValues[Model.S1] = 0;      
    }
    
    this.sensorValues[Model.M1] = this.lastChangeM1;
    this.sensorValues[Model.M2] = this.lastChangeM2;
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
    while( ! this.robot.reachedGoal() ) {
      this.robot.step();
      this.step();
    }
    return this;
  }
  
  public int[] getSensorValues() {
    return this.sensorValues;
  }

}
