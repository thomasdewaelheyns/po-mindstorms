package penoplatinum.simulator.entities;

/**
 * 
 * 
 * @authot Team Platinum
 */
import penoplatinum.simulator.sensors.IRSensor;
import penoplatinum.simulator.sensors.IRdistanceSensor;
import penoplatinum.simulator.sensors.LightSensor;
import penoplatinum.simulator.sensors.MotorState;
import penoplatinum.simulator.sensors.NoneSensor;
import penoplatinum.simulator.sensors.Sonar;

import penoplatinum.simulator.view.ViewRobot;

import penoplatinum.gateway.GatewayClient;

import penoplatinum.map.MapUtil;
import penoplatinum.robot.Robot;
import penoplatinum.robot.RobotAPI;
import penoplatinum.simulator.sensors.Motor;
import penoplatinum.simulator.RobotEntity;
import penoplatinum.simulator.Sensor;
import penoplatinum.robot.SimulationRobotAPI;
import penoplatinum.simulator.Simulator;
import penoplatinum.util.Point;

public class SimulatedEntity implements RobotEntity {

  public final double LENGTH_ROBOT = 10.0;

  public static final double LIGHTSENSOR_DISTANCE = 5.0; // 10cm from center

  public static final double BUMPER_LENGTH_ROBOT = 11.0;

  public static final double WHEEL_SIZE = 17.5; // circumf. in cm

  public static final double WHEEL_BASE = 16.0; // wheeldist. in cm

  private double positionX;       // the position of the robot in the world

  private double positionY;       //   expressed in X,Y coordinates

  private double direction;       //   and a direction it's facing

  private double totalMovement = 0;

  private long lastStatisticsReport = 0;  // time of last stat report

  private Point initialPosition;

  private int initialBearing;
  // the motorSpeeds and the sensorValues

  private int[] sensorValues = new int[SensorMapping.SENSORVALUES_NUM];

  private Motor[] motors = new Motor[3];

  private Sensor[] sensors = new Sensor[SensorMapping.SENSORVALUES_NUM];

  private SimulationRobotAPI robotAPI;    // the API used to access hardware

  private Robot robot;       // the actual robot

  private ViewRobot viewRobot;   // 

  private Simulator simulator;

  private final GatewayClient robotAgent;

  public SimulatedEntity(Robot robot) {
    this.setupSensors();
    this.robot = robot;
    setRobotApi(new SimulationRobotAPI());
    this.robotAgent = this.robot.getGatewayClient();
    useViewRobot(new SimulatedViewRobot(this));
  }

  public void useViewRobot(ViewRobot viewRobot) {
    this.viewRobot = viewRobot;
  }

  public void setRobotApi(SimulationRobotAPI simApi) {
    this.robotAPI = simApi;
    this.robotAPI.setSimulatedEntity(this);
    this.robot.useRobotAPI(this.robotAPI);
  }

  @Override
  public RobotAPI getRobotAPI() {
    return this.robotAPI;
  }

  public void useSimulator(Simulator simulator) {
    this.simulator = simulator;
    for (Sensor s : this.sensors) {
      s.useSimulator(simulator);
    }
  }

  public void setPostition(double positionX, double positionY, double direction) {
    this.positionX = positionX;
    this.positionY = positionY;
    this.direction = direction;
  }

  private Motor setupMotor(String label, int tachoPort, int statePort) {
    Motor motor = new Motor().setLabel(label);  // these two need to be running
    return setupMotor(label, tachoPort, statePort, motor);
  }

  public Motor setupMotor(String label, int tachoPort, int statePort, Motor motor) {
    this.motors[tachoPort] = motor.setLabel(label);  // these two need to be running
    setSensor(tachoPort, this.motors[tachoPort]);
    setSensor(statePort, new MotorState(this.motors[tachoPort]));
    return this.motors[tachoPort];
  }

  // TODO: externalize the speed configuration of the different motors
  private void setupSensors() {
    setupMotor("L", SensorMapping.M1, SensorMapping.MS1);
    setupMotor("R", SensorMapping.M2, SensorMapping.MS2);
    setupMotor("S", SensorMapping.M3, SensorMapping.MS3);
    //setSensor(SensorMapping.S1, new TouchSensor(45));
    //setSensor(SensorMapping.S2, new TouchSensor(315));
    setSensor(SensorMapping.S1, new IRSensor());
    setSensor(SensorMapping.S2, new NoneSensor());
    setSensor(SensorMapping.S3, new Sonar(this.motors[SensorMapping.M3]));
    setSensor(SensorMapping.S4, new LightSensor());
    setSensor(SensorMapping.IR0, new IRdistanceSensor(120));
    setSensor(SensorMapping.IR1, new IRdistanceSensor(60));
    setSensor(SensorMapping.IR2, new IRdistanceSensor(0));
    setSensor(SensorMapping.IR3, new IRdistanceSensor(-60));
    setSensor(SensorMapping.IR4, new IRdistanceSensor(-120));

  }

  private void setSensor(int port, Sensor sensor) {
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
   * the GatewayClient to interact with the robot.
   */
  public SimulatedEntity putRobotAt(int x, int y, int direction) {
    this.positionX = x;
    this.positionY = y;
    this.direction = direction;
    return this;
  }

  // called by the implementation of the RobotAPI
  public SimulatedEntity moveRobot(double movement) {
    movement *= 100;
    // calculate the tacho count we need to do to reach this movement
    int tacho = (int) (movement / WHEEL_SIZE * 360);
    this.motors[SensorMapping.M1].rotateBy(tacho);
    this.motors[SensorMapping.M2].rotateBy(tacho);
    return this;
  }

  // called by the implementation of the RobotAPI
  public SimulatedEntity turnRobot(int angle) {
    // calculate anmount of tacho needed to perform a turn by angle
    double dist = Math.PI * WHEEL_BASE / 360 * angle;
    int tacho = (int) (dist / WHEEL_SIZE * 360);

    // let both motor's rotate the same tacho but in opposite direction
    this.motors[SensorMapping.M1].rotateBy(tacho);
    this.motors[SensorMapping.M2].rotateBy(tacho * -1);
    return this;
  }

  // called by the implementation of the RobotAPI
  public SimulatedEntity stopRobot() {
    this.motors[SensorMapping.M1].stop();
    this.motors[SensorMapping.M2].stop();
    return this;
  }

  // low-level access method to a motor(CHANGED SO THAT ONLY SONAR MOTOR IS ACCESSIBLE)
  public SimulatedEntity rotateSonarTo(int tacho) {
    this.motors[SensorMapping.M3].rotateTo(tacho);
    return this;
  }

  @Override
  public double getPosX() {
    return positionX;
  }

  @Override
  public double getPosY() {
    return positionY;
  }

  /**
   * The direction of the current robot.
   * 0° is north. Counterclockwise
   * @return the direction
   */
  @Override
  public double getDir() {
    return direction;
  }

  @Override
  public ViewRobot getViewRobot() {
    return this.viewRobot;
  }

  public Robot getRobot() {
    return this.robot;
  }

  /**
   * Our internal representation of the bearing uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  public int getAngle() {
    return (int) ((this.direction + 270) % 360);
  }

  public int[] getSensorValues() {
    return this.sensorValues;
  }

  public boolean sonarMotorIsMoving() {
    return this.motors[SensorMapping.M3].getValue() != this.sensorValues[SensorMapping.M3];
  }

  // performs the next step in the movement currently executed by the robot
  public void step() {
    // let all motors know that another timeslice has passed

    this.motors[SensorMapping.M1].tick(simulator.TIME_SLICE);
    this.motors[SensorMapping.M2].tick(simulator.TIME_SLICE);
    this.motors[SensorMapping.M3].tick(simulator.TIME_SLICE);

    // based on the motor's (new) angle's determine the displacement
    int changeLeft = this.motors[SensorMapping.M1].getValue() - sensorValues[SensorMapping.M1];
    int changeRight = this.motors[SensorMapping.M2].getValue() - sensorValues[SensorMapping.M2];

    if (changeLeft == changeRight) {
      // we're moving in one direction 
      double d = WHEEL_SIZE / 360 * changeRight;
      double dx = Math.cos(Math.toRadians(this.getAngle())) * d;
      double dy = Math.sin(Math.toRadians(this.getAngle())) * d;
      if (MapUtil.hasTile(simulator.getMap(), this.positionX + dx, this.positionY + dy)) {
        if (!MapUtil.goesThroughWallX(simulator.getMap(), this, dx)) {
          this.positionX += dx;
        }
        if (!MapUtil.goesThroughWallY(simulator.getMap(), this, dy)) {
          this.positionY -= dy;
        }
      }
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

    this.getRobot().step();
  }

  /**
   * based on the robot's position, determine the values for the different
   * sensors.
   * TODO: extract the robot's physical configuration into separate object
   *       this is shared with the SensorMapping in a way (for now)
   */
  private void updateSensorValues() {
    for (int i = 0; i < SensorMapping.SENSORVALUES_NUM; i++) {
      sensorValues[i] = sensors[i].getValue();
    }
  }

  public Point getCurrentTileCoordinates() {
    // determine tile coordinates we're on
    int left = (int) (this.positionX / simulator.getTileSize()) + 1;
    int top = (int) (this.positionY / simulator.getTileSize()) + 1;
    return new Point(left, top);
  }

  public Point getCurrentOnTileCoordinates() {
    // determine tile coordinates on the tile we're on
    int left = (int) (this.positionX % simulator.getTileSize());
    int top = (int) (this.positionY % simulator.getTileSize());
    return new Point(left, top);
  }

  public double getDirection() {
    return this.direction;
  }

  public Point getInitialPosition() {
    return initialPosition;
  }

  public void setInitialPosition(Point initialPosition) {
    this.initialPosition = initialPosition;
  }

  public int getInitialBearing() {
    return initialBearing;
  }

  public void setInitialBearing(int initialBearing) {
    this.initialBearing = initialBearing;
  }
}
