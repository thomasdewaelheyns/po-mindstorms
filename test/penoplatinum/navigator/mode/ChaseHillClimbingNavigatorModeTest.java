package penoplatinum.navigator.mode;

/**
 * ChaseHillClimbingNavigatorModeTest
 * 
 * Tests ChaseHillClimbingNavigatorMode
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

import penoplatinum.util.Bearing;

import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Agent;

import penoplatinum.model.Model;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.GridModelPart;


public class ChaseHillClimbingNavigatorModeTest extends TestCase {

  private ChaseHillClimbingNavigatorMode mode;
  private Model              mockedModel;
  private GridModelPart      mockedGridModelPart;
  private Grid               mockedMyGrid;
  private Sector             mockedMySector;
  private Agent              mockedMyAgent;
  private Agent              mockedPacmanAgent;
  private Sector             mockedNorthSector;
  private Sector             mockedEastSector;
  private Sector             mockedSouthSector;
  private Sector             mockedWestSector;


  public ChaseHillClimbingNavigatorModeTest(String name) { 
    super(name);
  }

  public void testNotReachedGoal() {
    this.setup();

//    when(this.mockedMyGrid.getSectorOf(this.mockedPacmanAgent))
//      .thenReturn(this.mockUnknownSector());
    
    assertFalse(this.mode.reachedGoal());
	}

  public void testReachedGoalBecausePacmanIsAtNorth() {
    this.setup();

//    when(this.mockedMyGrid.getSectorOf(this.mockedPacmanAgent))
//      .thenReturn(this.mockedNorthSector);
    
    assertTrue(this.mode.reachedGoal());
	}

  public void testReachedGoalBecausePacmanIsAtEast() {
    this.setup();

//    when(this.mockedMyGrid.getSectorOf(this.mockedPacmanAgent))
//      .thenReturn(this.mockedEastSector);
    
    assertTrue(this.mode.reachedGoal());
	}

  public void testReachedGoalBecausePacmanIsAtSouth() {
    this.setup();

//    when(this.mockedMyGrid.getSectorOf(this.mockedPacmanAgent))
//      .thenReturn(this.mockedSouthSector);
    
    assertTrue(this.mode.reachedGoal());
	}

  public void testReachedGoalBecausePacmanIsAtWest() {
    this.setup();

//    when(this.mockedMyGrid.getSectorOf(this.mockedPacmanAgent))
//      .thenReturn(this.mockedWestSector);
    
    assertTrue(this.mode.reachedGoal());
	}
	
	private void setup() {
	  this.mockedModel = mock(Model.class);

	  this.mockedGridModelPart = mock(GridModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.GRID_MODEL_PART))
      .thenReturn(this.mockedGridModelPart);

    this.mockedMyGrid = mock(Grid.class);
    when(this.mockedGridModelPart.getMyGrid())
      .thenReturn(this.mockedMyGrid);

    this.mockedMyAgent = mock(Agent.class);
    when(this.mockedGridModelPart.getMyAgent())
      .thenReturn(this.mockedMyAgent);
      
    this.mockedPacmanAgent = mock(Agent.class);
    when(this.mockedMyGrid.getAgent("pacman"))
      .thenReturn(this.mockedPacmanAgent);

    this.mockedMySector = mock(Sector.class);
    when(this.mockedGridModelPart.getMySector())
      .thenReturn(this.mockedMySector);
    when(this.mockedMyGrid.getSectorOf(this.mockedMyAgent))
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

	  this.mode = new ChaseHillClimbingNavigatorMode(this.mockedModel);
  }
  
  private Sector mockUnknownSector() {
    return mock(Sector.class);
  }
}
