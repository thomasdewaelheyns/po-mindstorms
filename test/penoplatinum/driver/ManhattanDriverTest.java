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


public class ManhattanDriverTest extends TestCase {

  private ManhattanDriver driver;
  private Robot           mockedRobot;
  private RobotAPI        mockedRobotAPI;
  private Model           mockedModel;
  private SensorModelPart mockedSensorModelPart;


  public ManhattanDriverTest(String name) { 
    super(name);
  }

  public void testCreation() {
    this.setup();
    verify(this.mockedRobot, times(2)).getModel();
    verifyNoMoreInteractions(this.mockedRobot);
    // note: times(2) is needed probably due to static invocation in real code
    verify(this.mockedModel, times(2)).getPart(ModelPartRegistry.SENSOR_MODEL_PART);
    verifyNoMoreInteractions(this.mockedModel);
  }

  public void testIdleStateAfterCreation() {
    this.setup();
    assertFalse(this.driver.isBusy());
  }
  
  public void testMoveForwardInstruction() {
    this.setup();
    this.driver.move(1);
    assertTrue("driver isn't ready after zero steps.", this.driver.isBusy());
    // isMoving should not have been called now, because we aren't active yet
    verify(this.mockedSensorModelPart, never()).isMoving();
    assertFalse(this.driver.completedLastInstruction());
  }

  public void testMoveWithOneProceedStep() {
    this.setup();
    when(this.mockedSensorModelPart.isMoving()).thenReturn(true);
    this.driver.move(1);
    this.driver.proceed();
    verify(this.mockedSensorModelPart).isMoving(); // proceed checks isMoving
    verify(this.mockedRobotAPI).move(1); // move succesfully initiated
    // but the move isn't finished on one step
    assertTrue("driver isn't finished after one step.", this.driver.isBusy());
    verify(this.mockedSensorModelPart, times(2)).isMoving();
    assertFalse(this.driver.completedLastInstruction());
  }

  public void testIncompleteMove() { // we implement four steps before ready
    this.setup();
    when(this.mockedSensorModelPart.isMoving()).thenReturn(true,true,true);
    this.driver.move(1);
    this.driver.proceed(); // 1
    verify(this.mockedRobotAPI).move(1); // move succesfully initiated
    this.driver.proceed(); // 2
    this.driver.proceed(); // 3
    verify(this.mockedSensorModelPart, times(3)).isMoving();
    verifyNoMoreInteractions(this.mockedRobotAPI);
    // the movement is still in progress
    assertTrue("driver isn't ready after 3 steps", this.driver.isBusy());
    verify(this.mockedSensorModelPart, times(4)).isMoving();
    // assertFalse(this.driver.completedLastInstruction());
  }
  
  public void testCompleteMoveForward() {
    this.setup();
    when(this.mockedSensorModelPart.isMoving()).thenReturn(true,true,true,false);
    this.driver.move(1);
    this.driver.proceed(); // 1
    this.driver.proceed(); // 2
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

  // private construction helpers
  
  private void setup() {
    this.createDriver();
    this.mockRobot();
    this.driver.drive(this.mockedRobot);
  }
  
  private void createDriver() {
    this.driver = new ManhattanDriver();
  }

  private void mockRobot() {
    this.mockedRobot = mock(Robot.class);

    this.mockedRobotAPI = mock(RobotAPI.class);
    when(this.mockedRobot.getRobotAPI()).thenReturn(this.mockedRobotAPI);
    
    this.mockedModel = mock(Model.class);
    when(this.mockedRobot.getModel()).thenReturn(this.mockedModel);

    this.mockedSensorModelPart = mock(SensorModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.SENSOR_MODEL_PART))
      .thenReturn(this.mockedSensorModelPart);
  }

}
