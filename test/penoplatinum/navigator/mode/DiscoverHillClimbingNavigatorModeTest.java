package penoplatinum.navigator.mode;

/**
 * DiscoverHillClimbingNavigatorModeTest
 * 
 * Tests DiscoverHillClimbingNavigatorMode
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

import penoplatinum.grid.Grid;
import penoplatinum.grid.GridUtils;
import penoplatinum.grid.Sector;

import penoplatinum.model.Model;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;


public class DiscoverHillClimbingNavigatorModeTest extends TestCase {

  private DiscoverHillClimbingNavigatorMode mode;
  private Model                 mockedModel;
  private GridModelPart         mockedGridModelPart;
  private Grid                  mockedMyGrid;
  private List<Sector>          mockedSectors;

  public DiscoverHillClimbingNavigatorModeTest(String name) { 
    super(name);
  }

  public void testNotReachedGoal() {
    this.setup();
    makeFullyKnown(this.mockedSectors.get(0));
    makeFullyKnown(this.mockedSectors.get(1));
    makeFullyKnown(this.mockedSectors.get(2));
    
    assertFalse(this.mode.reachedGoal());
  }
  
  private void makeFullyKnown(Sector s){
    for(Bearing b : Bearing.NESW){
      when(s.knowsWall(b)).thenReturn(true);
    }
  }

  public void testReachedGoal() {
    this.setup();
    makeFullyKnown(this.mockedSectors.get(0));
    makeFullyKnown(this.mockedSectors.get(1));
    makeFullyKnown(this.mockedSectors.get(2));
    makeFullyKnown(this.mockedSectors.get(3));
    
    
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

    this.mockedSectors = new ArrayList<Sector>();
    this.mockedSectors.add(mock(Sector.class));
    this.mockedSectors.add(mock(Sector.class));
    this.mockedSectors.add(mock(Sector.class));
    this.mockedSectors.add(mock(Sector.class));
    
    when(this.mockedMyGrid.getSectors()).thenReturn(this.mockedSectors);
    this.mode = new DiscoverHillClimbingNavigatorMode(this.mockedModel);
  }
}
