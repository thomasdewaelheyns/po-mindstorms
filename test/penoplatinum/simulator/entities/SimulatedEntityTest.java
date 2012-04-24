/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator.entities;

import penoplatinum.map.Map;
import penoplatinum.simulator.sensors.Motor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.grid.Sector;
import penoplatinum.map.MapUtil;
import penoplatinum.robot.Robot;
import penoplatinum.robot.SimulationRobotAPI;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.entities.SensorMapping;
import penoplatinum.simulator.entities.SimulatedEntity;
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

  private Map mockedMap;

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
    mockedMotorSonar = this.mockMotor();
    simEntity.setupMotor("L", SensorMapping.M1, SensorMapping.MS1, mockedMotorLeft);
    simEntity.setupMotor("R", SensorMapping.M2, SensorMapping.MS2, mockedMotorRight);
    simEntity.setupMotor("S", SensorMapping.M3, SensorMapping.MS3, mockedMotorSonar);
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
    simEntity.turnRobot(360);
    verify(this.mockedMotorLeft).rotateBy(1034);
    verify(this.mockedMotorRight).rotateBy(-1034);
    simEntity.turnRobot(-5);
    verify(this.mockedMotorLeft).rotateBy(-14);
    verify(this.mockedMotorRight).rotateBy(14);
    simEntity.turnRobot(81);
    verify(this.mockedMotorLeft).rotateBy(232);
    verify(this.mockedMotorRight).rotateBy(-232);
    simEntity.turnRobot(-90);
    verify(this.mockedMotorLeft).rotateBy(-258);
    verify(this.mockedMotorRight).rotateBy(258);
  }

  /**
   * Test of stopRobot method, of class SimulatedEntity.
   */
  @Test
  public void testStopRobot() {

    simEntity.moveRobot(0.360);
    verify(this.mockedMotorLeft).rotateBy(740);
    verify(this.mockedMotorRight).rotateBy(740);
    simEntity.stopRobot();
    verify(this.mockedMotorLeft).stop();
    verify(this.mockedMotorRight).stop();

  }

  /**
   * Test of rotateMotorTo method, of class SimulatedEntity.
   */
  @Test
  public void testRotateSensorTo() {
    simEntity.rotateSonarTo(500);
    verify(this.mockedMotorSonar).rotateTo(500);
    simEntity.rotateSonarTo(-50);
    verify(this.mockedMotorSonar).rotateTo(-50);
    simEntity.rotateSonarTo(1);
    verify(this.mockedMotorSonar).rotateTo(1);
    simEntity.rotateSonarTo(-1);
    verify(this.mockedMotorSonar).rotateTo(-1);
  }

  /**
   * Test of getPosX method, of class SimulatedEntity.
   */
  @Test
  public void testGetters() {
    int x = 5;
    int y = 7;
    int direction = 30;
    simEntity.putRobotAt(x, y, direction);
    assertEquals(simEntity.getPosX(), x, 0.05);
    assertEquals(simEntity.getPosY(), y, 0.05);
    assertEquals(simEntity.getAngle(), direction + 270);
    assertEquals(simEntity.getViewRobot(), this.mockedViewRobot);
    assertEquals(simEntity.getRobot(), this.mockedRobot);
  }

  /**
   * Test of sonarMotorIsMoving method, of class SimulatedEntity.
   */
  @Test
  public void testSonarMotorIsMoving() {
    simEntity.getSensorValues()[SensorMapping.M3] = 50;
    assertTrue(simEntity.sonarMotorIsMoving());

  }

  /**
   * Test of step method, of class SimulatedEntity.
   */
  @Test
  public void testStep() {
    mockedSimulator.useMap(mockedMap);
    //first test move
    simEntity.moveRobot(0.01);
    double positionX1 = simEntity.getPosX();
    double positionY1 = simEntity.getPosY();
    when(mockedMotorLeft.getValue()).thenReturn(20);
    when(mockedMotorRight.getValue()).thenReturn(20);
    simEntity.step();
    verify(mockedMotorLeft).tick(Simulator.TIME_SLICE);
    verify(mockedMotorSonar).tick(Simulator.TIME_SLICE);
    verify(mockedMotorRight).tick(Simulator.TIME_SLICE);
    verify(mockedMotorLeft, times(2)).getValue();
    verify(mockedMotorRight, times(2)).getValue();
    verify(mockedSimulator, times(4)).getMap();
    verify(mockedRobot).step();
    double positionX2 = simEntity.getPosX();
    double positionY2 = simEntity.getPosY();
    assertFalse(positionX1==positionX2);
    assertFalse(positionY1==positionY2);
    //test turn
    simEntity.turnRobot(-5);
    double direction1 = simEntity.getDirection();
    when(mockedMotorLeft.getValue()).thenReturn(20-14);
    when(mockedMotorRight.getValue()).thenReturn(20+14);
    simEntity.step();
    double direction2 = simEntity.getDirection();
    assertFalse(direction1 == direction2);


  }

  /**
   * Test of getCurrentTileCoordinates method, of class SimulatedEntity.
   */
  @Test
  public void testGetCurrentTileCoordinates() {
    Point point11 = new Point(1, 1);
    Point point22 = new Point(2, 2);
    Point point = simEntity.getCurrentTileCoordinates();
    assertEquals(point11, point);
    simEntity.putRobotAt(39, 39, 30);
    Point point2 = simEntity.getCurrentTileCoordinates();
    assertEquals(point11, point2);
    simEntity.putRobotAt(40, 40, 0);
    Point point3 = simEntity.getCurrentTileCoordinates();
    assertEquals(point22, point3);
    simEntity.putRobotAt(41, 41, 130);
    Point point4 = simEntity.getCurrentTileCoordinates();
    assertEquals(point22, point4);
    simEntity.putRobotAt(50, 50, 130);
    Point point5 = simEntity.getCurrentTileCoordinates();
    assertEquals(point22, point5);
  }

  /**
   * Test of getCurrentOnTileCoordinates method, of class SimulatedEntity.
   */
  @Test
  public void testGetCurrentOnTileCoordinates() {
    Point point11 = new Point(39, 39);
    Point point22 = new Point(0, 0);
    Point point33 = new Point(1, 1);
    Point point44 = new Point(10, 10);
    Point point = simEntity.getCurrentOnTileCoordinates();
    assertEquals(point22, point);
    simEntity.putRobotAt(39, 39, 30);
    Point point2 = simEntity.getCurrentOnTileCoordinates();
    assertEquals(point11, point2);
    simEntity.putRobotAt(40, 40, 0);
    Point point3 = simEntity.getCurrentOnTileCoordinates();
    assertEquals(point22, point3);
    simEntity.putRobotAt(41, 41, 130);
    Point point4 = simEntity.getCurrentOnTileCoordinates();
    assertEquals(point33, point4);
    simEntity.putRobotAt(50, 50, 130);
    Point point5 = simEntity.getCurrentOnTileCoordinates();
    assertEquals(point44, point5);
  }

  /**
   * Test of getInitialPosition method, of class SimulatedEntity.
   */
  @Test
  public void testIntials() {
    assertEquals(simEntity.getInitialBearing(), 0);
    assertEquals(simEntity.getInitialPosition(), null);
    simEntity.setInitialBearing(30);
    Point point = new Point(50, 50);
    simEntity.setInitialPosition(point);
    assertEquals(simEntity.getInitialPosition(), point);
    assertEquals(simEntity.getInitialBearing(), 30);
  }

  private Simulator mockSimulator() {
    Simulator mockedSimulator = mock(Simulator.class);
    mockedMap = penoplatinum.map.MapTestUtil.getMap();
    when(mockedSimulator.getTileSize()).thenReturn(40);
    when(mockedSimulator.getMap()).thenReturn(mockedMap);
    return mockedSimulator;
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
    when(mockedMotor.setLabel("S")).thenReturn(mockedMotor);
    
    return mockedMotor;
  }
}
