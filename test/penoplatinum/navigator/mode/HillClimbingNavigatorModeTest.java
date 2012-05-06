package penoplatinum.navigator.mode;

/**
 * HillClimbingNavigatorModeTest
 * 
 * Tests HillClimbingNavigatorMode class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import java.util.List;

import penoplatinum.util.Bearing;

import penoplatinum.grid.Grid;
import penoplatinum.grid.agent.Agent;
import penoplatinum.grid.Sector;

import penoplatinum.model.Model;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.GridModelPart;

import penoplatinum.navigator.action.*;
import penoplatinum.util.Point;


public class HillClimbingNavigatorModeTest extends TestCase {

  private HillClimbingNavigatorMode mode;
  private Model                     mockedModel;
  private GridModelPart             mockedGridModelPart;
  private Grid                      mockedMyGrid;
  private Agent                     mockedMyAgent;
  private Sector                    mockedMySector;
  private Sector                    mockedNorthSector;
  private Sector                    mockedEastSector;
  private Sector                    mockedSouthSector;
  private Sector                    mockedWestSector;
  private Point mockPosition;


  public HillClimbingNavigatorModeTest(String name) { 
    super(name);
  }

  public void testNeverReachesGoal() {
    this.setup();
    assertFalse(this.mode.reachedGoal());
	}

  public void testNoAccessToNeighboursCreatesPlanWithLeftTurn() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(false);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(1, plan.size());
    assertTrue(plan.get(0) instanceof TurnLeftNavigatorAction);
  }

  public void testFacingNorthAccessToNorthNeighbourCreatesForwardPlan() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(true);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(false);

    when(this.mockedNorthSector.getValue()).thenReturn(1);

    when(this.mockedMyGrid.getBearingOf(this.mockedMyAgent)).
      thenReturn(Bearing.N);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.N);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(1, plan.size());
    assertTrue(plan.get(0) instanceof ForwardNavigatorAction);
  }

  public void testFacingEastAccessToEastNeighbourCreatesForwardPlan() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(true);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(false);

    when(this.mockedEastSector.getValue()).thenReturn(1);

    when(this.mockedMyGrid.getBearingOf(this.mockedMyAgent)).
      thenReturn(Bearing.E);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.E);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(1, plan.size());
    assertTrue(plan.get(0) instanceof ForwardNavigatorAction);
  }

  public void testFacingSouthAccessToSouthNeighbourCreatesForwardPlan() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(true);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(false);

    when(this.mockedSouthSector.getValue()).thenReturn(1);

    when(this.mockedMyGrid.getBearingOf(this.mockedMyAgent)).
      thenReturn(Bearing.S);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.S);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(1, plan.size());
    assertTrue(plan.get(0) instanceof ForwardNavigatorAction);
  }

  public void testFacingWestAccessToWestNeighbourCreatesForwardPlan() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(true);

    when(this.mockedWestSector.getValue()).thenReturn(1);

    when(this.mockedMyGrid.getBearingOf(this.mockedMyAgent)).
      thenReturn(Bearing.W);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.W);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(1, plan.size());
    assertTrue(plan.get(0) instanceof ForwardNavigatorAction);
  }
  
  public void testFacingNorthAccessToEastNeighbourCreatesTurnRightForwardPlan() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(true);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(false);

    when(this.mockedEastSector.getValue()).thenReturn(1);

    when(this.mockedMyGrid.getBearingOf(this.mockedMyAgent)).
      thenReturn(Bearing.N);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.N);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(2, plan.size());
    assertTrue(plan.get(0) instanceof TurnRightNavigatorAction);
    assertTrue(plan.get(1) instanceof ForwardNavigatorAction);
  }

  public void testFacingNorthAccessToSouthNeighbourCreates2TurnRightForwardPlan() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(true);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(false);

    when(this.mockedSouthSector.getValue()).thenReturn(1);

    when(this.mockedMyGrid.getBearingOf(this.mockedMyAgent)).
      thenReturn(Bearing.N);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.N);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(3, plan.size());
    assertTrue(plan.get(0) instanceof TurnRightNavigatorAction);
    assertTrue(plan.get(1) instanceof TurnRightNavigatorAction);
    assertTrue(plan.get(2) instanceof ForwardNavigatorAction);
  }

  public void testFacingNorthAccessToWestNeighbourCreatesTurnLeftForwardPlan() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(true);

    when(this.mockedWestSector.getValue()).thenReturn(1);

    when(this.mockedMyGrid.getBearingOf(this.mockedMyAgent)).
      thenReturn(Bearing.N);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.N);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(2, plan.size());
    assertTrue(plan.get(0) instanceof TurnLeftNavigatorAction);
    assertTrue(plan.get(1) instanceof ForwardNavigatorAction);
  }
  
  public void testFacingNorthAccessToEastWestWithWestHigestCreatesTurnRightPlan() {
    this.setup();

    when(this.mockedMySector.givesAccessTo(Bearing.N)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.E)).thenReturn(true);
    when(this.mockedMySector.givesAccessTo(Bearing.S)).thenReturn(false);
    when(this.mockedMySector.givesAccessTo(Bearing.W)).thenReturn(true);

    when(this.mockedEastSector.getValue()).thenReturn(1);
    when(this.mockedWestSector.getValue()).thenReturn(2);

    when(this.mockedMyGrid.getBearingOf(this.mockedMyAgent)).
      thenReturn(Bearing.N);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.N);

    List<NavigatorAction> plan = this.mode.createNewPlan();
    
    assertEquals(2, plan.size());
    assertTrue(plan.get(0) instanceof TurnLeftNavigatorAction);
    assertTrue(plan.get(1) instanceof ForwardNavigatorAction);
  }

  // TODO: add more exhaustive copy/paste/edit versions of all possible 
  //       combinations of the tests above.
  
	private void setup() {
	  this.mockedModel = mock(Model.class);

	  this.mockedGridModelPart = mock(GridModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.GRID_MODEL_PART))
      .thenReturn(this.mockedGridModelPart);

    this.mockedMyAgent = mock(Agent.class);
    when(this.mockedGridModelPart.getMyAgent())
      .thenReturn(this.mockedMyAgent);

    this.mockedMyGrid = mock(Grid.class);
    when(this.mockedGridModelPart.getMyGrid())
      .thenReturn(this.mockedMyGrid);

    this.mockedMySector = mock(Sector.class);   
    mockPosition = mock(Point.class);
    when(mockedMyGrid.getSectorAt(mockPosition)).thenReturn(mockedMySector);
    when(mockedMyGrid.getPositionOf(mockedMyAgent)).thenReturn(mockPosition);
    this.mockedMySector = mock(Sector.class);   
    when(this.mockedGridModelPart.getMySector())
      .thenReturn(this.mockedMySector);
    
    this.mockedNorthSector = mock(Sector.class);
    this.mockedEastSector  = mock(Sector.class);
    this.mockedSouthSector = mock(Sector.class);
    this.mockedWestSector  = mock(Sector.class);

    when(this.mockedMySector.getNeighbour(Bearing.N))
      .thenReturn(this.mockedNorthSector);
    when(this.mockedMySector.getNeighbour(Bearing.E))
      .thenReturn(this.mockedEastSector);
    when(this.mockedMySector.getNeighbour(Bearing.S))
      .thenReturn(this.mockedSouthSector);
    when(this.mockedMySector.getNeighbour(Bearing.W))
      .thenReturn(this.mockedWestSector);

	  this.mode = new HillClimbingNavigatorMode(this.mockedModel);
  }
}
