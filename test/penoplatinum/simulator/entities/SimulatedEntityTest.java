package penoplatinum.simulator.entities;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import penoplatinum.Config;

import penoplatinum.map.Map;
import penoplatinum.simulator.sensors.Motor;
import penoplatinum.robot.Robot;
import penoplatinum.simulator.Simulator;
import penoplatinum.util.Point;


public class SimulatedEntityTest extends TestCase {

  private SimulatedEntity simEntity;
  private Robot mockedRobot;
  private Simulator mockedSimulator;
  private Motor mockedMotorLeft;
  private Motor mockedMotorRight;
  private Motor mockedMotorSonar;
  private Map mockedMap;

  @Before
  public void setUp() {
    this.mockedRobot = this.mockRobot();
    this.simEntity = this.createSimulatedEntity();
    this.mockedSimulator = this.mockSimulator();
    mockedMotorLeft = this.mockMotor();
    mockedMotorRight = this.mockMotor();
    mockedMotorSonar = this.mockMotor();
    simEntity.setupMotor(mockedMotorRight, Config.MOTOR_RIGHT);
    simEntity.setupMotor(mockedMotorLeft, Config.MOTOR_LEFT);
    simEntity.setupMotor(mockedMotorSonar, Config.MOTOR_SONAR);
    simEntity.useSimulator(mockedSimulator);
  }

  /**
   * Test of putRobotAt method, of class SimulatedEntity.
   */
  @Test
  public void testPutRobotAt() {
    assertEquals(simEntity, simEntity.putRobotAt(1, 18, 303));
    assertEquals(1, simEntity.getPosX(), 0.05);
    assertEquals(18, simEntity.getPosY(), 0.05);
    assertEquals(33, simEntity.getAngle(), 0.05);
  }

  /**
   * Test of getPosX method, of class SimulatedEntity.
   */
  @Test
  public void testGetters() {
    int x = 5;
    int y = 7;
    int direction = 300;
    simEntity.putRobotAt(x, y, direction);
    assertEquals(x, simEntity.getPosX(), 0.05);
    assertEquals(y, simEntity.getPosY(), 0.05);
    assertEquals(30, simEntity.getAngle());
    assertEquals(SimulatedViewRobot.class, simEntity.getViewRobot().getClass());
    assertEquals(direction, simEntity.getViewRobot().getDirection());
    assertEquals(this.mockedRobot, simEntity.getRobot());
  }

  /**
   * Test of step method, of class SimulatedEntity.
   */
  @Test
  public void testStep() {
    mockedSimulator.useMap(mockedMap);
    simEntity.putRobotAt(20, 20, 90);
    //Move and turn at the same time ;)
    //Make timeslice small enough.
    when(mockedMotorLeft.getFullAngleTurned()).thenReturn(34.0);
    when(mockedMotorRight.getFullAngleTurned()).thenReturn(6.0);
    simEntity.step();
    assertEquals(85.12587, simEntity.getDirection(), 0.001);
    verify(mockedMotorLeft).tick(Simulator.TIME_SLICE);
    verify(mockedMotorSonar).tick(Simulator.TIME_SLICE);
    verify(mockedMotorRight).tick(Simulator.TIME_SLICE);
    verify(mockedMotorLeft, times(2)).getFullAngleTurned();
    verify(mockedMotorRight, times(2)).getFullAngleTurned();
    verify(mockedSimulator, times(3)).getMap();
    verify(mockedRobot).step();
    assertEquals(19.027, simEntity.getPosX(), 0.001);
    assertEquals(20.0, simEntity.getPosY(), 0.001);
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
    return new SimulatedEntity(mockedRobot, 0);
  }

  private Robot mockRobot() {
    return mock(Robot.class);
  }

  private Motor mockMotor() {
    Motor mockedMotor = mock(Motor.class);
    when(mockedMotor.setLabel("L")).thenReturn(mockedMotor);
    when(mockedMotor.setLabel("R")).thenReturn(mockedMotor);
    when(mockedMotor.setLabel("S")).thenReturn(mockedMotor);
    return mockedMotor;
  }
}
