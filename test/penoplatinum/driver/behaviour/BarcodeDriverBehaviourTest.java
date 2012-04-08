package penoplatinum.driver.behaviour;

/**
 * BarcodeDriverBehaviourTest
 * 
 * Tests BarcodeDriverBehaviour
 * 
 * @author: Team Platinum
 */

import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.robot.RobotAPI;

import penoplatinum.model.Model;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.BarcodeModelPart;

import penoplatinum.driver.action.DriverAction;
import penoplatinum.driver.action.CombinedDriverAction;
import penoplatinum.driver.action.MoveDriverAction;


public class BarcodeDriverBehaviourTest extends TestCase {

	private BarcodeDriverBehaviour behaviour;
	private Model mockedModel;
	private BarcodeModelPart mockedBarcodeModelPart;

  public BarcodeDriverBehaviourTest(String name) { 
    super(name);
  }
 
  // public interface
 
  public void testNoReadingBarcodeRequiresNoAction() {
		this.setup();
		when(this.mockedBarcodeModelPart.isReadingBarcode()).thenReturn(false);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertFalse(this.behaviour.requiresAction(this.mockedModel, moveAction));
	}

  public void testReadingBarcodeRequiresAction() {
		this.setup();
		when(this.mockedBarcodeModelPart.isReadingBarcode()).thenReturn(true);

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
	
	public void testGetNextActionWhenReadingBarcode() {
		this.setup();
		when(this.mockedBarcodeModelPart.isReadingBarcode()).thenReturn(true);

		MoveDriverAction moveAction = this.mockMoveDriverAction();
		assertTrue(this.behaviour.requiresAction(this.mockedModel, moveAction));
		
		DriverAction action = this.behaviour.getNextAction();
		assertTrue(action instanceof CombinedDriverAction);
		
		RobotAPI api = this.mockRobotAPI();
		action.work(api);
		verify(api).move(0.05);
	}

	// private construction helpers
	
	private void setup() {
		this.behaviour = this.createBehaviour();
		this.mockedModel = this.mockModel();
		this.mockedBarcodeModelPart = this.mockBarcodeModelPart();
    this.mockedBarcodeModelPart = mock(BarcodeModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.BARCODE_MODEL_PART))
      .thenReturn(this.mockedBarcodeModelPart);
	}
	
	private BarcodeDriverBehaviour createBehaviour() {
		return new BarcodeDriverBehaviour();
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

	private BarcodeModelPart mockBarcodeModelPart() {
		return mock(BarcodeModelPart.class);
	}
	
	private RobotAPI mockRobotAPI() {
		return mock(RobotAPI.class);
	}
}
