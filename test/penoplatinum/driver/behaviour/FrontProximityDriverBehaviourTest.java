package penoplatinum.driver.behaviour;

/**
 * FrontProximityDriverBehaviourTest
 * 
 * Tests FrontProximityDriverBehaviour
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


public class FrontProximityDriverBehaviourTest extends TestCase {

	private FrontProximityDriverBehaviour behaviour;
	private Model mockedModel;
	private WallsModelPart mockedWallsModelPart;

  public FrontProximityDriverBehaviourTest(String name) { 
    super(name);
  }
 
  // public interface
 
  public void testNoWallInFrontRequiresNoAction() {
		this.setup();
		when(this.mockedWallsModelPart.getWallFrontDistance()).thenReturn(1000);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertFalse(this.behaviour.requiresAction(this.mockedModel, moveAction));
	}

  public void testWallInFrontRequiresAction() {
		this.setup();
		when(this.mockedWallsModelPart.getWallFrontDistance()).thenReturn(1);

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
	
	public void testGetNextActionForWallInFront() {
		this.setup();
		when(this.mockedWallsModelPart.isWallFront()).thenReturn(false);

		when(this.mockedWallsModelPart.getWallFrontDistance()).thenReturn(1);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertTrue(this.behaviour.requiresAction(this.mockedModel, moveAction));
		
		DriverAction action = this.behaviour.getNextAction();
		assertTrue(action instanceof CombinedDriverAction);
		
		assertTrue(action.interrupts());
		
		RobotAPI api = this.mockRobotAPI();
		action.work(api);
		verify(api).move(-0.05);
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
	
	private FrontProximityDriverBehaviour createBehaviour() {
		return new FrontProximityDriverBehaviour();
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
