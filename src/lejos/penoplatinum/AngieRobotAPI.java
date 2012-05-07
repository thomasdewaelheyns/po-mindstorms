package penoplatinum;

/**
 * Responsible for providing access to the hardware of the robot
 * TODO: WARNING, WHEN CHANGIN' PORTS THIS ENTIRE CLASS MUST BE CHECKED
 * @author: Team Platinum
 */
import java.util.ArrayList;
import java.util.List;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.reporter.DashboardReporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.bluetooth.RobotBluetoothGatewayClient;
import penoplatinum.driver.ManhattanDriver;
import penoplatinum.driver.behaviour.BarcodeDriverBehaviourLejos;
import penoplatinum.driver.behaviour.FrontProximityDriverBehaviour;
import penoplatinum.driver.behaviour.LineDriverBehaviour;
import penoplatinum.driver.behaviour.SideProximityDriverBehaviour;
import penoplatinum.navigator.GhostNavigator;
import penoplatinum.navigator.Navigator;
import penoplatinum.robot.GhostRobot;
import penoplatinum.robot.RobotAPI;
import penoplatinum.sensor.IRSeekerV2;
import penoplatinum.util.FPS;
import penoplatinum.util.Utils;

public class AngieRobotAPI implements RobotAPI {

  private final SensorPort LIGHT_SENSORPORT = SensorPort.S4;
  private final SensorPort SONAR_SENSORPORT = SensorPort.S3;
  private final SensorPort IR_SENSORPORT = SensorPort.S1;
  private final Motor motorLeft = Motor.B;
  private final Motor motorRight = Motor.C;
  private final Motor motorSonar = Motor.A;
  private LightSensor light;
  private IRSeekerV2 irSeeker;
  private int fps = 0;
  
  private UltrasonicSensor sonarSensor;
  private int forwardTacho;
  private int[] currentSweepAngles;
  private int currentSweepAngleIndex;
  private List<Integer> resultBuffer = new ArrayList<Integer>();
  private int sweepID = 0;
  
  
  public static double CCW_afwijking = 1.01;
  public int SPEEDFORWARD = 400; //400
  public int SPEEDTURN = 250;
  public final int WIELOMTREK = 175; //mm
  public final int WIELAFSTAND = 160;//112mm

  private int tachoLeftStart;
  private int tachoRightStart;
  private float internalOrientation;
  private float buffer;
  
  private static final int sensorNumberInfraRed = Config.S1;
  private static final int sensorNumberLight = Config.S4;
  private static final int sensorNumberSonar = Config.S3;
  private static final int sensorNumberMotorLeft = Config.M1;
  private static final int sensorNumberMotorRight = Config.M2;
  private static final int sensorNumberMotorSonar = Config.M3;
  int[] values = new int[Config.SENSORVALUES_NUM];

  public AngieRobotAPI() {
    //Reset tacho's
    motorSonar.resetTachoCount();
    motorLeft.resetTachoCount();
    motorRight.resetTachoCount();

    light = new LightSensor(LIGHT_SENSORPORT, true);
    sonarSensor = new UltrasonicSensor(SONAR_SENSORPORT);
    irSeeker = new IRSeekerV2(IR_SENSORPORT, IRSeekerV2.Mode.AC);

    motorSonar.smoothAcceleration(true);
    if (motorSonar.getTachoCount() != 0) {
      motorSonar.resetTachoCount();
    }
    forwardTacho = motorSonar.getTachoCount();
  }

  public void step() {
    updateSonarMovement();
  }
  
  public int[] getSensorValues() {
    values[sensorNumberMotorLeft] = motorLeft.getTachoCount();
    values[sensorNumberMotorRight] = motorRight.getTachoCount();
    values[sensorNumberMotorSonar] = motorSonar.getTachoCount();
    values[Config.MS1] = getMotorState(motorLeft);
    values[Config.MS2] = getMotorState(motorRight);
    values[Config.MS3] = getMotorState(motorSonar);

    values[sensorNumberLight] = light.getNormalizedLightValue();
    if (this.isSweeping()) {
      values[sensorNumberSonar] = (int) sonarSensor.getDistance();
      values[sensorNumberInfraRed] = irSeeker.getDirection();
      values[Config.IR0] = irSeeker.getSensorValue(1);
      values[Config.IR1] = irSeeker.getSensorValue(2);
      values[Config.IR2] = irSeeker.getSensorValue(3);
      values[Config.IR3] = irSeeker.getSensorValue(4);
      values[Config.IR4] = irSeeker.getSensorValue(5);
    }

    return values;
  }

  private int getMotorState(Motor m) {
    for (int i = 0; i < 3; i++) {
      if (!m.isMoving() || m.isStopped() || m.isFloating()) {
        return Config.MOTORSTATE_STOPPED;
      }
      if (m.isForward()) {
        return Config.MOTORSTATE_FORWARD;
      }
      if (m.isBackward()) {
        return Config.MOTORSTATE_BACKWARD;
      }
    }
    //Utils.Error("Syncronized??? " + (m.isMoving() ? 1 : 0) + "," + (m.isForward() ? 1 : 0) + "," + (m.isBackward() ? 1 : 0) + "," + (m.isStopped() ? 1 : 0) + "," + (m.isFloating() ? 1 : 0));
    //1, 0, 0, 1, 0
    //Utils.Error("I M P O S S I B L E !");
    return 0;
  }

  public void setSpeed(int motor, int speed) {
    switch (motor) {
      case Config.M3:
        motorSonar.setSpeed(speed);
        break;
      case Config.M1:
        SPEEDFORWARD = speed > 400 ? 400 : speed;
        break;
      case Config.M2:
        SPEEDFORWARD = speed > 400 ? 400 : speed;
        break;
    }
  }

  public void beep() {
    lejos.nxt.Sound.beep();
  }
  private float outAngle;

  public void setReferenceAngle(float reference) {
    reference = getInternalOrientation();
  }

  public float getRelativeAngle(float reference) {
    outAngle = -reference + getInternalOrientation();
    return outAngle;
  }

  public void updateSonarMovement() {
    if (!isSweeping()) {
      return;
    }
    if (motorSonar.isMoving()) {
      return;
    }
    int currentTacho = motorSonar.getTachoCount() - forwardTacho;
    int currentAngle = currentSweepAngles[currentSweepAngleIndex] - forwardTacho;
    if (currentTacho != currentAngle) {
      motorSonar.rotateTo(currentAngle, true);
      return;
    }
    resultBuffer.add(sonarSensor.getDistance());
    currentSweepAngleIndex++;

    if (currentSweepAngleIndex >= currentSweepAngles.length) {
      motorSonar.rotateTo(currentSweepAngles[0] - forwardTacho, true);
      currentSweepAngles = null;
      sweepID++;
    }
  }

  public boolean isSweeping() {
    return currentSweepAngles != null;
  }

  public void sweep(int[] i) {
    currentSweepAngles = i;
    currentSweepAngleIndex = 0;
    //WARNING: the result list can be used after the next sweep is requested! resultBuffer.clear();
    resultBuffer = new ArrayList<Integer>();
    //resultBuffer.clear();
  }

  public List<Integer> getSweepResult() {
    return resultBuffer;
  }

  public int getSweepID() {
    return sweepID;
  }

  public long getFreeMemory() {
    return Runtime.getRuntime().freeMemory();
  }

  public long getTotalmemory() {
    return Runtime.getRuntime().totalMemory();
  }

  public void setFps(int fps) {
    this.fps = fps;
  }

  public int getFps() {
    return this.fps;
  }

  private void abortMovement() {
    internalOrientation = getInternalOrientation();
    tachoLeftStart = motorLeft.getTachoCount();
    tachoRightStart = motorRight.getTachoCount();
  }

  public float getInternalOrientation() {
    int tachoLeftDiff = motorLeft.getTachoCount() - tachoLeftStart;
    int tachoRightDiff = motorRight.getTachoCount() - tachoRightStart;
    float averageForward = (tachoRightDiff - tachoLeftDiff) / 2.0f;
    float inverse = (float) (averageForward * WIELOMTREK / WIELAFSTAND / Math.PI);
    buffer = inverse;
    buffer +=internalOrientation;
    return buffer;
  }

  public void move(double distance) {
    abortMovement();
    distance *= 1000;
    distance /= 0.99;
    int r = (int) (distance * 360 / WIELOMTREK);
    setSpeed(SPEEDFORWARD);
    motorLeft.rotate(r, true);
    motorRight.rotate(r, true);
  }

  public void turn(int angleCCW2) {
    abortMovement();
    setSpeed(SPEEDTURN);
    double angleCCW = angleCCW2 * CCW_afwijking;

    int h = (int) (angleCCW * Math.PI * WIELAFSTAND / WIELOMTREK);
    motorLeft.rotate(-h, true);
    motorRight.rotate(h, true);
  }

  public void setSpeed(int speed) {
    motorLeft.setSpeed(speed);
    motorRight.setSpeed(speed);
  }

  public void stop() {
    abortMovement();
    motorLeft.stop();
    motorRight.stop();
  }

  /**
   * Returns the average tacho count of the 2 motors
   */
  public float getAverageTacho() {
    return (motorLeft.getTachoCount() + motorRight.getTachoCount()) / 2f;
  }
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public static void main(String[] args) throws Exception {
    GhostRobot robot = new GhostRobot("Platinum");
    ManhattanDriver manhattan = new ManhattanDriver(0.4)
            .addBehaviour(new FrontProximityDriverBehaviour())
            .addBehaviour(new SideProximityDriverBehaviour())
            .addBehaviour(new BarcodeDriverBehaviourLejos())
            .addBehaviour(new LineDriverBehaviour());
    robot.useDriver(manhattan);

    Navigator navigator = new GhostNavigator();
    robot.useNavigator(navigator);

    RobotBluetoothGatewayClient gateway = new RobotBluetoothGatewayClient();
    robot.useGatewayClient(gateway);

    RobotBluetoothConnection conn = new RobotBluetoothConnection();
    conn.initializeConnection();
    Utils.EnableRemoteLogging(conn);
    
    gateway.useConnection(conn);
    gateway.run();
    
    robot.useReporter(new DashboardReporter());

    FPS fps = new FPS();
    AngieRobotAPI angie = new AngieRobotAPI();
    robot.useRobotAPI(angie);

    angie.setFps(fps.getFps());
    while (true) {
      angie.updateSonarMovement();
      fps.setCheckPoint();
      robot.step();
      System.gc();
      fps.endCheckPoint();
    }
  }

}