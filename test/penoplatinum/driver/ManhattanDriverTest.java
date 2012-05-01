package penoplatinum.driver;

/**
 * ManhattanDriverTest
 * 
 * Tests ManhattanDriver class.
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.robot.Robot;
import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;

import penoplatinum.driver.action.DriverAction;
import penoplatinum.driver.behaviour.DriverBehaviour;
import penoplatinum.robot.AdvancedRobot;


public class ManhattanDriverTest extends TestCase {

  private final static double SECTOR_SIZE = 0.04;

  private ManhattanDriver driver;
  private AdvancedRobot   mockedRobot;
  private RobotAPI        mockedRobotAPI;
  private Model           mockedModel;
  private SensorModelPart mockedSensorModelPart;
  private DriverBehaviour mockedBehaviour;
  private DriverAction    mockedAction;


  public ManhattanDriverTest(String name) { 
    super(name);
  }

  public void testCreation() {
    this.setup();
    verify(this.mockedRobot, times(3)).getModel();
    verifyNoMoreInteractions(this.mockedRobot);
    // note: times(2) is needed probably due to static invocation in real code
    verify(this.mockedModel, times(3)).getPart(ModelPartRegistry.SENSOR_MODEL_PART);
    verifyNoMoreInteractions(this.mockedModel);
  }

  public void testIdleStateAfterCreation() {
    this.setup();
    assertFalse(this.driver.isBusy());
  }
  
  // MOVE
  
  public void testMoveForwardInstruction() {
    this.setup();
    this.driver.move();
    assertTrue("driver isn't ready after zero steps.", this.driver.isBusy());
    // isMoving should not have been called now, because we aren't active yet
    verify(this.mockedSensorModelPart, never()).isMoving();
    assertFalse(this.driver.completedLastInstruction());
  }

  public void testIncompleteMove() { // we implement four steps before ready
    this.setup();
    when(this.mockedSensorModelPart.isMoving()).thenReturn(true,true,true);
    this.driver.move();
    this.driver.proceed(); // 1, init always busy
    verify(this.mockedRobotAPI).move(SECTOR_SIZE); // move succesfully initiated
    this.driver.proceed(); // 2, step 1
    this.driver.proceed(); // 3, step 2
    this.driver.proceed(); // 4, step 3
    verify(this.mockedSensorModelPart, times(3)).isMoving();
    verifyNoMoreInteractions(this.mockedRobotAPI);
    // the movement is still in progress
    assertTrue("driver isn't ready after 3 steps", this.driver.isBusy());
    verify(this.mockedSensorModelPart, times(4)).isMoving();
    assertFalse(this.driver.completedLastInstruction());
  }
  
  public void testCompleteMove() {
    this.setup();
    when(this.mockedSensorModelPart.isMoving()).thenReturn(true,true,true,false);
    this.driver.move();
    this.driver.proceed(); // 1, init not moving
    this.driver.proceed(); // 2
    this.driver.proceed(); // 3
    this.driver.proceed(); // 3
    // after four proceeds the action is complete and the motors have stoped
    // moving. this is here triggered by the fact that every time, the Driver
    // checks if the currentAction is ready
    this.driver.proceed(); // 4
    verify(this.mockedSensorModelPart, times(4)).isMoving();
    // the movement is finished
    assertFalse("driver should be ready after 4 steps", this.driver.isBusy());
    assertTrue(this.driver.completedLastInstruction());
  }

  // TURN [ LEFT | RIGHT ]

  public void testTurnLeftInstruction() {
    this.setup();
    this.driver.turnLeft();
    assertTrue("driver isn't ready after zero steps.", this.driver.isBusy());
    // isMoving should not have been called now, because we aren't active yet
    verify(this.mockedSensorModelPart, never()).isTurning();
    assertFalse(this.driver.completedLastInstruction());
  }
  
  public void testTurnRightInstruction() {
    this.setup();
    this.driver.turnRight();
    assertTrue("driver isn't ready after zero steps.", this.driver.isBusy());
    // isMoving should not have been called now, because we aren't active yet
    verify(this.mockedSensorModelPart, never()).isTurning();
    assertFalse(this.driver.completedLastInstruction());
  }
  
  public void testIncompleteTurnLeft() {
    this.setup();
    when(this.mockedSensorModelPart.isTurning()).thenReturn(true,true,true);
    this.driver.turnLeft();
    this.driver.proceed(); // 1
    verify(this.mockedRobotAPI).turn(90); // move succesfully initiated
    this.driver.proceed(); // 2
    this.driver.proceed(); // 3
    verify(this.mockedSensorModelPart, times(2)).isTurning();
    verifyNoMoreInteractions(this.mockedRobotAPI);
    // the movement is still in progress
    assertTrue("driver isn't ready after 3 steps", this.driver.isBusy());
    verify(this.mockedSensorModelPart, times(3)).isTurning();
    assertFalse(this.driver.completedLastInstruction());
  }

  public void testIncompleteTurnRight() {
    this.setup();
    when(this.mockedSensorModelPart.isTurning()).thenReturn(true,true,true);
    this.driver.turnRight();
    this.driver.proceed(); // 1
    verify(this.mockedRobotAPI).turn(-90); // move succesfully initiated
    this.driver.proceed(); // 2
    this.driver.proceed(); // 3
    verify(this.mockedSensorModelPart, times(2)).isTurning();
    verifyNoMoreInteractions(this.mockedRobotAPI);
    // the movement is still in progress
    assertTrue("driver isn't ready after 2 steps", this.driver.isBusy());
    verify(this.mockedSensorModelPart, times(3)).isTurning();
    assertFalse(this.driver.completedLastInstruction());
  }
  
  public void testCompleteTurnLeft() {
    this.setup();
    when(this.mockedSensorModelPart.isTurning()).thenReturn(true,true,true,false);
    this.driver.turnLeft();
    this.driver.proceed(); // 1, init apiCall, nothing else
    this.driver.proceed(); // 2
    this.driver.proceed(); // 3
    // after four proceeds the action is complete and the motors have stoped
    // moving. this is here triggered by the fact that every time, the Driver
    // checks if the currentAction is ready
    this.driver.proceed(); // 4
    this.driver.proceed(); // 5
    verify(this.mockedSensorModelPart, times(4)).isTurning();
    // the movement is finished
    assertFalse("driver should be ready after 5 steps", this.driver.isBusy());
    assertTrue(this.driver.completedLastInstruction());
  }

  public void testCompleteTurnRight() {
    this.setup();
    when(this.mockedSensorModelPart.isTurning()).thenReturn(true,true,false);
    this.driver.turnRight();
    this.driver.proceed(); // 1 //init, always busy
    this.driver.proceed(); // 2
    this.driver.proceed(); // 3
    // after four proceeds the action is complete and the motors have stoped
    // moving. this is here triggered by the fact that every time, the Driver
    // checks if the currentAction is ready
    this.driver.proceed(); // 4
    verify(this.mockedSensorModelPart, times(3)).isTurning();
    // the movement is finished
    assertFalse("driver should be ready after 3 steps", this.driver.isBusy());
    assertTrue(this.driver.completedLastInstruction());
  }
  
  // BEHAVIOUR
  
  public void testInterruptingBehaviour() {
    this.setup();
    when(this.mockedSensorModelPart.isMoving()).thenReturn(true);
    this.addBehaviour();
    when(this.mockedBehaviour.requiresAction(eq(this.mockedModel),
                                             (DriverAction)anyObject()))
      .thenReturn(false, true);
    when(this.mockedAction.interrupts()).thenReturn(true);
    when(this.mockedAction.isBusy()).thenReturn(true, true, true, false);

    this.driver.move();
    this.driver.proceed();
    verify(this.mockedBehaviour).requiresAction(eq(this.mockedModel),
                                                (DriverAction)anyObject());
    // the driver is busy driving, the 
    assertTrue(this.driver.isBusy());
    assertFalse(this.driver.completedLastInstruction());
    // the second proceed iteration, the behaviour kicks in
    this.driver.proceed();
    verify(this.mockedBehaviour, times(2)).requiresAction(eq(this.mockedModel),
                                                          (DriverAction)anyObject());
    verify(this.mockedAction).isBusy();
    assertTrue(this.driver.isBusy());
    verify(this.mockedAction, times(2)).isBusy();
    assertFalse(this.driver.completedLastInstruction());
    verify(this.mockedAction, times(3)).isBusy();
    // proceed one more time to finish interrupted action
    this.driver.proceed();
    assertFalse(this.driver.isBusy());
    verify(this.mockedAction, times(4)).isBusy();
    assertFalse(this.driver.completedLastInstruction());
    // verify(this.mockedAction, times(5)).isBusy(); -> currentAction is IDLE
  }

  public void testNonInterruptingBehaviour() {
    this.setup();
    when(this.mockedSensorModelPart.isMoving()).thenReturn(true);
    this.addBehaviour();
    when(this.mockedBehaviour.requiresAction(eq(this.mockedModel),
                                             (DriverAction)anyObject()))
      .thenReturn(false, true);
    when(this.mockedAction.interrupts()).thenReturn(false);
    when(this.mockedAction.isBusy()).thenReturn(true, true, true, false);

    this.driver.move();
    this.driver.proceed();
    verify(this.mockedBehaviour).requiresAction(eq(this.mockedModel),
                                                (DriverAction)anyObject());
    // the driver is busy driving, the 
    assertTrue(this.driver.isBusy());
    assertFalse(this.driver.completedLastInstruction());
    // the second proceed iteration, the behaviour kicks in
    this.driver.proceed();
    verify(this.mockedBehaviour, times(2)).requiresAction(eq(this.mockedModel),
                                                          (DriverAction)anyObject());
    verify(this.mockedAction).isBusy();
    assertTrue(this.driver.isBusy());
    verify(this.mockedAction, times(2)).isBusy();
    assertFalse(this.driver.completedLastInstruction());
    // proceed one more time to finish interrupted action
    this.driver.proceed();
    assertFalse(this.driver.isBusy());
    assertTrue(this.driver.completedLastInstruction());
  }

  // TODO: probably some more complex situations, but these will have to be 
  //       added as they emerge and need to be fixed.

  // private construction helpers
  
  private void setup() {
    this.createDriver();
    this.mockRobot();
    this.driver.drive(this.mockedRobot);
  }
  
  private void createDriver() {
    this.driver = new ManhattanDriver(SECTOR_SIZE);
  }
  
  private void addBehaviour() {
    this.mockedBehaviour = this.mockBehaviour();
    this.driver.addBehaviour(this.mockedBehaviour);
    this.mockedAction = this.mockAction();
    when(this.mockedBehaviour.getNextAction()).thenReturn(this.mockedAction);
  }

  private void mockRobot() {
    this.mockedRobot = mock(AdvancedRobot.class);

    this.mockedRobotAPI = mock(RobotAPI.class);
    when(this.mockedRobot.getRobotAPI()).thenReturn(this.mockedRobotAPI);
    
    this.mockedModel = mock(Model.class);
    when(this.mockedRobot.getModel()).thenReturn(this.mockedModel);

    this.mockedSensorModelPart = mock(SensorModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.SENSOR_MODEL_PART))
      .thenReturn(this.mockedSensorModelPart);
  }
  
  private DriverBehaviour mockBehaviour() {
    return mock(DriverBehaviour.class);
  }

  private DriverAction mockAction() {
    return mock(DriverAction.class);
  }
  
}
