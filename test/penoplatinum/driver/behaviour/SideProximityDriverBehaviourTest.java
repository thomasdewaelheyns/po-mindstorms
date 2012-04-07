package penoplatinum.driver.behaviour;

/**
 * SideProximityDriverBehaviourTest
 * 
 * Tests SideProximityDriverBehaviour
 * 
 * @author: Team Platinum
 */

import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.WallsModelPart;

import penoplatinum.driver.action.DriverAction;
import penoplatinum.driver.action.CombinedDriverAction;
import penoplatinum.driver.action.MoveDriverAction;


public class SideProximityDriverBehaviourTest extends TestCase {

	private SideProximityDriverBehaviour behaviour;
	private Model mockedModel;
	private WallsModelPart mockedWallsModelPart;

  public SideProximityDriverBehaviourTest(String name) { 
    super(name);
  }
 
  // public interface
 
  public void testOtherThanMoveDriverActionRequiresNoAction() {
		this.setup();
		DriverAction someAction = this.mockDriverAction();
		assertFalse(this.behaviour.requiresAction(this.mockedModel, someAction));
	}

  public void testWallInFrontRequiresNoAction() {
		this.setup();
		when(this.mockedWallsModelPart.isWallFront()).thenReturn(true);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertFalse(this.behaviour.requiresAction(this.mockedModel, moveAction));
	}

  public void testNoWallLeftOrRightRequiresNoAction() {
		this.setup();
		when(this.mockedWallsModelPart.isWallFront()).thenReturn(false);

		when(this.mockedWallsModelPart.getWallLeftDistance()).thenReturn(1000);
		when(this.mockedWallsModelPart.getWallRightDistance()).thenReturn(1000);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertFalse(this.behaviour.requiresAction(this.mockedModel, moveAction));
	}

  public void testWallLeftRequiresAction() {
		this.setup();
		when(this.mockedWallsModelPart.isWallFront()).thenReturn(false);

		when(this.mockedWallsModelPart.getWallLeftDistance()).thenReturn(1);
		when(this.mockedWallsModelPart.getWallRightDistance()).thenReturn(1000);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertTrue(this.behaviour.requiresAction(this.mockedModel, moveAction));
	}

  public void testWallRightRequiresAction() {
		this.setup();
		when(this.mockedWallsModelPart.isWallFront()).thenReturn(false);

		when(this.mockedWallsModelPart.getWallLeftDistance()).thenReturn(1000);
		when(this.mockedWallsModelPart.getWallRightDistance()).thenReturn(1);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertTrue(this.behaviour.requiresAction(this.mockedModel, moveAction));
	}
	
  public void testGetNextActionThrowsExceptionIfNoCorrection() {
		this.setup();
		try {
			this.behaviour.getNextAction();
			fail( "Behaviour should throw Exception if no action is required.");
		} catch(Exception e) {}
	}
	
	public void testGetNextActionForLeftWall() {
		this.setup();
		when(this.mockedWallsModelPart.isWallFront()).thenReturn(false);

		when(this.mockedWallsModelPart.getWallLeftDistance()).thenReturn(1);
		when(this.mockedWallsModelPart.getWallRightDistance()).thenReturn(1000);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertTrue(this.behaviour.requiresAction(this.mockedModel, moveAction));
		
		DriverAction action = this.behaviour.getNextAction();
		assertTrue(action instanceof CombinedDriverAction);
		
		RobotAPI api = this.mockRobotAPI();
		action.work(api);
		verify(api).turn(-15);
	}

	public void testGetNextActionForRightWall() {
		this.setup();
		when(this.mockedWallsModelPart.isWallFront()).thenReturn(false);

		when(this.mockedWallsModelPart.getWallLeftDistance()).thenReturn(1000);
		when(this.mockedWallsModelPart.getWallRightDistance()).thenReturn(1);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertTrue(this.behaviour.requiresAction(this.mockedModel, moveAction));
		
		DriverAction action = this.behaviour.getNextAction();
		assertTrue(action instanceof CombinedDriverAction);
		
		RobotAPI api = this.mockRobotAPI();
		action.work(api);
		verify(api).turn(15);
	}

	// private construction helpers
	
	private void setup() {
		this.behaviour = this.createBehaviour();
		this.mockedModel = this.mockModel();
		this.mockedWallsModelPart = this.mockWallsModelPart();
    this.mockedWallsModelPart = mock(WallsModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.WALLS_MODEL_PART))
      .thenReturn(this.mockedWallsModelPart);
	}
	
	private SideProximityDriverBehaviour createBehaviour() {
		return new SideProximityDriverBehaviour();
	}
	
	private Model mockModel() {
		return mock(Model.class);
	}
	
	private DriverAction mockDriverAction() {
		return mock(DriverAction.class);
	}

	private MoveDriverAction mockMoveDriverAction() {
		return mock(MoveDriverAction.class);
	}

	private WallsModelPart mockWallsModelPart() {
		return mock(WallsModelPart.class);
	}
	
	private RobotAPI mockRobotAPI() {
		return mock(RobotAPI.class);
	}
}