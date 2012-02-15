package penoplatinum.simulator;

import java.awt.Point;
import penoplatinum.simulator.sensors.LightSensor;
import penoplatinum.simulator.sensors.MotorState;
import penoplatinum.simulator.sensors.Sonar;
import penoplatinum.simulator.sensors.TouchSensor;

public class SimulatedEntity {

  public final double LENGTH_ROBOT = 10.0;
  
  public static final int NUMBER_OF_SENSORS = 10;
  public static final double LIGHTSENSOR_DISTANCE = 10.0; // 10cm from center
  public static final double BUMPER_LENGTH_ROBOT = 11.0;
  public static final double WHEEL_SIZE = 17.5; // circumf. in cm
  public static final double WHEEL_BASE = 16.0; // wheeldist. in cm
  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction it's facing
  private double totalMovement = 0;
  private long lastStatisticsReport = 0;  // time of last stat report
  // the motorSpeeds and the sensorValues
  private int[] sensorValues = new int[NUMBER_OF_SENSORS];
  private Motor[] motors = new Motor[3];
  private Sensor[] sensors = new Sensor[NUMBER_OF_SENSORS];
  
  /* Moved to SensorValues
  private int prevLeft = 0;
  private int prevRight = 0;
  private int prevSonar = 0;/**/
  SimulationRobotAPI robotAPI;    // the API used to access hardware
  SimulationRobotAgent robotAgent;  // the communication layer
  private Robot robot;            // the actual robot
  
  private Simulator simulator;

  public SimulatedEntity(SimulationRobotAPI robotAPI, SimulationRobotAgent robotAgent, Robot robot) {
    this.setupMotors();
    this.setupSensors();
    this.robotAPI = robotAPI;
    this.robotAgent = robotAgent;
    this.robot = robot;
    robotAPI.setSimulatedEntity(this);
    robot.useRobotAPI(robotAPI);
  }
  
  public void useSimulator(Simulator simulator){
    this.simulator = simulator;
    for(Sensor s : this.sensors){
      s.useSimulator(simulator);
    }
  }

  public void setPostition(double positionX, double positionY, double direction) {
    this.positionX = positionX;
    this.positionY = positionY;
    this.direction = direction;
  }

  // this needs to be in sync with the "reality" ;-)
  // TODO: externalize the speed configuration of the different motors
  private void setupMotors() {
    setupMotor("L", Model.M1, Model.MS1);
    setupMotor("R", Model.M2, Model.MS2);
    setupMotor("S", Model.M3, Model.MS3);
  }
  
  private void setupMotor(String label, int tachoPort, int statePort){
    this.motors[tachoPort] = new Motor().setLabel(label);  // these two need to be running
    setSensor(tachoPort, this.motors[tachoPort]);
    setSensor(statePort, new MotorState(this.motors[tachoPort]));
  }
  
  private void setupSensors(){
    setSensor(Model.S1, new TouchSensor(45));
    setSensor(Model.S2, new TouchSensor(315));
    setSensor(Model.S3, new Sonar());
    setSensor(Model.S4, new LightSensor());
  }
  
  private void setSensor(int port, Sensor sensor){
    this.sensors[port] = sensor;
    sensor.useSimulatedEntity(this);
    sensor.useSimulator(simulator);
  }

  public SimulatedEntity setSpeed(int motor, int speed) {
    this.motors[motor].setSpeed(speed);
    return this;
  }

  /**
   * A robot is put on the map - as in the real world - on a certain place
   * and in a given direction.
   * The Simulator also instruments the robot with a RobotAPI and sets up
   * the RobotAgent to interact with the robot.
   */
  public SimulatedEntity putRobotAt(Robot robot, int x, int y, int direction) {
    this.robot = robot;
    this.positionX = x;
    this.positionY = y;
    this.direction = direction;

    // we provide the robot with our SimulationAPI
    this.getRobot().useRobotAPI(this.robotAPI);

    // we connect our SimulationRobotAgent to the robot
    this.robotAgent.setRobot(this.getRobot());

    return this;
  }

  // called by the implementation of the RobotAPI
  public SimulatedEntity moveRobot(double movement) {
    movement *= 100;
    // calculate the tacho count we need to do to reach this movement
    int tacho = (int) (movement / WHEEL_SIZE * 360);
    this.motors[Model.M1].rotateBy(tacho);
    this.motors[Model.M2].rotateBy(tacho);
    return this;
  }

  // called by the implementation of the RobotAPI
  public SimulatedEntity turnRobot(int angle) {
    // calculate anmount of tacho needed to perform a turn by angle
    double dist = Math.PI * WHEEL_BASE / 360 * angle;
    int tacho = (int) (dist / WHEEL_SIZE * 360);

    // let both motor's rotate the same tacho but in opposite direction
    this.motors[Model.M1].rotateBy(tacho);
    this.motors[Model.M2].rotateBy(tacho * -1);
    return this;
  }

  // called by the implementation of the RobotAPI
  public SimulatedEntity stopRobot() {
    this.motors[Model.M1].stop();
    this.motors[Model.M2].stop();
    return this;
  }

  // low-level access method to a motor
  public SimulatedEntity rotateMotorTo(int motor, int tacho) {
    this.motors[motor].rotateTo(tacho);
    return this;
  }

  public double getPosX() {
    return positionX;
  }
  
  public double getPosY() {
    return positionY;
  }
  
  public double getDir(){
    return direction;
  }
  
  ViewRobot getRobotView(){
    return new ViewRobot(this);
  }
  
  public Robot getRobot() {
    return robot;
  }

  /**
   * Our internal representation of the baring uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  public int getAngle() {
    return (int) ((this.direction + 90) % 360);
  }

  public int[] getSensorValues() {
    return this.sensorValues;
  }
  
  public boolean sonarMotorIsMoving() {
    return this.motors[Model.M3].getValue() != this.sensorValues[Model.M3];
  }


  private void trackMovementStatistics(double movement) {
    this.totalMovement += movement;
    // report the statistics every 2 seconds
    long current = System.currentTimeMillis();
    if (current - this.lastStatisticsReport > 2000) {
      this.lastStatisticsReport = current;
      //this.reportMovementStatistics();
    }
  }

  // performs the next step in the movement currently executed by the robot
  void step() {
    // let all motors know that another timeslice has passed

    this.motors[Model.M1].tick(simulator.TIME_SLICE);
    this.motors[Model.M2].tick(simulator.TIME_SLICE);
    this.motors[Model.M3].tick(simulator.TIME_SLICE);

    // based on the motor's (new) angle's determine the displacement
    int changeLeft = this.motors[Model.M1].getValue() - sensorValues[Model.M1];
    int changeRight = this.motors[Model.M2].getValue() - sensorValues[Model.M2];

    if (changeLeft == changeRight) {
      // we're moving in one direction 
      double d = WHEEL_SIZE / 360 * changeRight;
      double dx = Math.cos(Math.toRadians(this.getAngle())) * d;
      double dy = Math.sin(Math.toRadians(this.getAngle())) * d;
      if (simulator.hasTile(this.positionX + dx, this.positionY + dy)) {
        if (!simulator.goesThroughWallX(this, dx)) {
          this.positionX += dx;
        }
        if (!simulator.goesThroughWallY(this, dy)) {
          this.positionY -= dy;
        }
      }
      this.trackMovementStatistics(d);
    } else if (changeLeft == changeRight * -1) {
      // we're turning
      double d = WHEEL_SIZE / 360 * changeLeft;
      double dr = (d / (Math.PI * WHEEL_BASE)) * 360;
      this.direction += dr;
    } else {
      // hell froze over
      System.err.println("ERROR: inconsistent motor behaviour.");
      System.err.println(changeLeft + ", " + changeRight);
    }

    // based on the new location, determine the value of the different sensors
    this.updateSensorValues();

    // always refresh our SimulationView
    //simulator.refreshView();
    
    getRobot().step();
  }

  private void reportMovementStatistics() {
    simulator.view.log("");
    simulator.view.log("Total Distance = " + this.totalMovement + "cm");
    //simulator.view.log("Visited Tiles  = " + this.visitedTiles.size());
    //this.view.log("Fitness        = " + this.getFitness());
  }/*/

  /**
   * based on the robot's position, determine the values for the different
   * sensors.
   * TODO: extract the robot's physical configuration into separate object
   *       this is shared with the Model in a way (for now)
   */
  private void updateSensorValues() {
    for(int i = 0; i<NUMBER_OF_SENSORS; i++){
      sensorValues[i] = sensors[i].getValue();
    }
  }
  public Point getCurrentTileCoordinates() {
    // determine tile coordinates we're on
    int left = (int) (this.positionX / Tile.SIZE)+ 1;
    int top = (int) (this.positionY / Tile.SIZE) + 1;
    return new Point(left, top);
  }

  public Point getCurrentOnTileCoordinates() {
    // determine tile coordinates on the tile we're on
    int left = (int) (this.positionX % Tile.SIZE);
    int top = (int) (this.positionY % Tile.SIZE);
    return new Point(left, top);
  }
}
