/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator.entities;

import penoplatinum.simulator.sensors.Motor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.robot.Robot;
import penoplatinum.robot.SimulationRobotAPI;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.view.ViewRobot;
import penoplatinum.util.Point;
import static org.mockito.Mockito.*;

/**
 *
 * @author Florian
 */
public class SimulatedEntityTest {

  private SimulatedEntity simEntity;

  private Robot mockedRobot;

  private ViewRobot mockedViewRobot;

  private GatewayClient mockedGatewayClient;

  private Simulator mockedSimulator;

  private SimulationRobotAPI mockedApi;

  private Motor mockedMotorLeft;

  private Motor mockedMotorRight;

  private Motor mockedMotorSonar;

  public SimulatedEntityTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    this.mockedViewRobot = this.mockViewRobot();
    this.mockedRobot = this.mockRobot();
    this.mockedApi = this.mockRobotApi();
    this.simEntity = this.createSimulatedEntity();
    this.mockedSimulator = this.mockSimulator();
    simEntity.useViewRobot(mockedViewRobot);
    simEntity.setRobotApi(mockedApi);
    simEntity.useSimulator(mockedSimulator);
    mockedMotorLeft = this.mockMotor();
    mockedMotorRight = this.mockMotor();
    simEntity.setupMotor("L", SensorMapping.M1, SensorMapping.MS1, mockedMotorLeft);
    simEntity.setupMotor("R", SensorMapping.M2, SensorMapping.MS2, mockedMotorRight);
    
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of getRobotAPI method, of class SimulatedEntity.
   */
  @Test
  public void testGetRobotAPI() {
    assertEquals(mockedApi, simEntity.getRobotAPI());
  }

  /**
   * Test of setPostition method, of class SimulatedEntity.
   */
  @Test
  public void testSetPostition() {
    double positionX = 1;
    double positionY = 18;
    double direction = 33;
    simEntity.setPostition(positionX, positionY, direction);
    assertEquals(positionX, simEntity.getPosX(), 0.05);
    assertEquals(positionY, simEntity.getPosY(), 0.05);
    assertEquals((direction + 270) % 360, simEntity.getAngle(), 0.05);

  }

  /**
   * Test of putRobotAt method, of class SimulatedEntity.
   */
  @Test
  public void testPutRobotAt() {
    double positionX = 1;
    double positionY = 18;
    double direction = 33;
    assertEquals(simEntity, simEntity.putRobotAt(1, 18, 33));
    assertEquals(positionX, simEntity.getPosX(), 0.05);
    assertEquals(positionY, simEntity.getPosY(), 0.05);
    assertEquals((direction + 270) % 360, simEntity.getAngle(), 0.05);
  }

  /**
   * Test of moveRobot method, of class SimulatedEntity.
   */
  @Test
  public void testMoveRobot() {
    
    simEntity.moveRobot(0.360);
    verify(this.mockedMotorLeft).rotateBy(740);
    verify(this.mockedMotorRight).rotateBy(740);
    simEntity.moveRobot(-5);
    verify(this.mockedMotorLeft).rotateBy(-10285);
    verify(this.mockedMotorRight).rotateBy(-10285);
    simEntity.moveRobot(81);
    verify(this.mockedMotorLeft).rotateBy(166628);
    verify(this.mockedMotorRight).rotateBy(166628);
    simEntity.moveRobot(0.01);
    verify(this.mockedMotorLeft).rotateBy(20);
    verify(this.mockedMotorRight).rotateBy(20);
  }

  /**
   * Test of turnRobot method, of class SimulatedEntity.
   */
  @Test
  public void testTurnRobot() {
       simEntity.moveRobot(0.360);
    verify(this.mockedMotorLeft).rotateBy(740);
    verify(this.mockedMotorRight).rotateBy(740);
    simEntity.moveRobot(-5);
    verify(this.mockedMotorLeft).rotateBy(-10285);
    verify(this.mockedMotorRight).rotateBy(-10285);
    simEntity.moveRobot(81);
    verify(this.mockedMotorLeft).rotateBy(166628);
    verify(this.mockedMotorRight).rotateBy(166628);
    simEntity.moveRobot(0.01);
    verify(this.mockedMotorLeft).rotateBy(20);
    verify(this.mockedMotorRight).rotateBy(20);
  }

  /**
   * Test of stopRobot method, of class SimulatedEntity.
   */
  @Test
  public void testStopRobot() {
    System.out.println("stopRobot");
    SimulatedEntity instance = null;
    SimulatedEntity expResult = null;
    SimulatedEntity result = instance.stopRobot();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of rotateMotorTo method, of class SimulatedEntity.
   */
  @Test
  public void testRotateMotorTo() {
    System.out.println("rotateMotorTo");
    int motor = 0;
    int tacho = 0;
    SimulatedEntity instance = null;
    SimulatedEntity expResult = null;
    SimulatedEntity result = instance.rotateMotorTo(motor, tacho);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getPosX method, of class SimulatedEntity.
   */
  @Test
  public void testGetPosX() {
    System.out.println("getPosX");
    SimulatedEntity instance = null;
    double expResult = 0.0;
    double result = instance.getPosX();
    assertEquals(expResult, result, 0.0);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getPosY method, of class SimulatedEntity.
   */
  @Test
  public void testGetPosY() {
    System.out.println("getPosY");
    SimulatedEntity instance = null;
    double expResult = 0.0;
    double result = instance.getPosY();
    assertEquals(expResult, result, 0.0);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDir method, of class SimulatedEntity.
   */
  @Test
  public void testGetDir() {
    System.out.println("getDir");
    SimulatedEntity instance = null;
    double expResult = 0.0;
    double result = instance.getDir();
    assertEquals(expResult, result, 0.0);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getViewRobot method, of class SimulatedEntity.
   */
  @Test
  public void testGetViewRobot() {
    System.out.println("getViewRobot");
    SimulatedEntity instance = null;
    ViewRobot expResult = null;
    ViewRobot result = instance.getViewRobot();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getRobot method, of class SimulatedEntity.
   */
  @Test
  public void testGetRobot() {
    System.out.println("getRobot");
    SimulatedEntity instance = null;
    Robot expResult = null;
    Robot result = instance.getRobot();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getAngle method, of class SimulatedEntity.
   */
  @Test
  public void testGetAngle() {
    System.out.println("getAngle");
    SimulatedEntity instance = null;
    int expResult = 0;
    int result = instance.getAngle();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSensorValues method, of class SimulatedEntity.
   */
  @Test
  public void testGetSensorValues() {
    System.out.println("getSensorValues");
    SimulatedEntity instance = null;
    int[] expResult = null;
    int[] result = instance.getSensorValues();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of sonarMotorIsMoving method, of class SimulatedEntity.
   */
  @Test
  public void testSonarMotorIsMoving() {
    System.out.println("sonarMotorIsMoving");
    SimulatedEntity instance = null;
    boolean expResult = false;
    boolean result = instance.sonarMotorIsMoving();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of step method, of class SimulatedEntity.
   */
  @Test
  public void testStep() {
    System.out.println("step");
    SimulatedEntity instance = null;
    instance.step();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCurrentTileCoordinates method, of class SimulatedEntity.
   */
  @Test
  public void testGetCurrentTileCoordinates() {
    System.out.println("getCurrentTileCoordinates");
    SimulatedEntity instance = null;
    Point expResult = null;
    Point result = instance.getCurrentTileCoordinates();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getCurrentOnTileCoordinates method, of class SimulatedEntity.
   */
  @Test
  public void testGetCurrentOnTileCoordinates() {
    System.out.println("getCurrentOnTileCoordinates");
    SimulatedEntity instance = null;
    Point expResult = null;
    Point result = instance.getCurrentOnTileCoordinates();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDirection method, of class SimulatedEntity.
   */
  @Test
  public void testGetDirection() {
    System.out.println("getDirection");
    SimulatedEntity instance = null;
    double expResult = 0.0;
    double result = instance.getDirection();
    assertEquals(expResult, result, 0.0);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getInitialPosition method, of class SimulatedEntity.
   */
  @Test
  public void testGetInitialPosition() {
    System.out.println("getInitialPosition");
    SimulatedEntity instance = null;
    Point expResult = null;
    Point result = instance.getInitialPosition();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setInitialPosition method, of class SimulatedEntity.
   */
  @Test
  public void testSetInitialPosition() {
    System.out.println("setInitialPosition");
    Point initialPosition = null;
    SimulatedEntity instance = null;
    instance.setInitialPosition(initialPosition);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getInitialBearing method, of class SimulatedEntity.
   */
  @Test
  public void testGetInitialBearing() {
    System.out.println("getInitialBearing");
    SimulatedEntity instance = null;
    int expResult = 0;
    int result = instance.getInitialBearing();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setInitialBearing method, of class SimulatedEntity.
   */
  @Test
  public void testSetInitialBearing() {
    System.out.println("setInitialBearing");
    int initialBearing = 0;
    SimulatedEntity instance = null;
    instance.setInitialBearing(initialBearing);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  private Simulator mockSimulator() {
    return mock(Simulator.class);
  }

  private SimulatedEntity createSimulatedEntity() {
    return new SimulatedEntity(mockedRobot);
  }

  private Robot mockRobot() {
    return mock(Robot.class);
  }

  private ViewRobot mockViewRobot() {
    return mock(ViewRobot.class);
  }

  private SimulationRobotAPI mockRobotApi() {
    return mock(SimulationRobotAPI.class);
  }

  private Motor mockMotor() {
    Motor mockedMotor = mock(Motor.class);
    when(mockedMotor.setLabel("L")).thenReturn(mockedMotor);
    when(mockedMotor.setLabel("R")).thenReturn(mockedMotor);
    return mockedMotor;
  }
}
