package penoplatinum.model.processor;

import penoplatinum.util.Point;
import penoplatinum.grid.Grid;
import penoplatinum.grid.agent.Agent;
import penoplatinum.grid.Sector;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.model.part.WallsModelPart;
import penoplatinum.util.Bearing;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ImportWallsModelProcessorTest extends TestCase {

  private Sector mockGridCurrent;
  private Sector mockWallCurrent;
  private Grid mockGrid;
  private WallsModelPart mockWalls;
  private GridModelPart mockGridPart;
  private Model mockModel;
  private Point mockPosition;
  
  @Before
  public void setUp() {
    mockGridCurrent = mock(Sector.class);
    mockWallCurrent = mock(Sector.class);
    mockGrid = mock(Grid.class);
    mockWalls = mock(WallsModelPart.class);
    mockGridPart = mock(GridModelPart.class);
    mockModel = mock(Model.class);
    when(mockModel.getPart(ModelPartRegistry.WALLS_MODEL_PART)).thenReturn(mockWalls);
    when(mockModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(mockGridPart);
  }

  /**
   * Test of work method, of class ImportWallsModelProcessor.
   */
  @Test
  public void testWork() {
    ImportWallsModelProcessor instance = new ImportWallsModelProcessor();
    instance.setModel(mockModel);
    
    when(mockGridPart.getMyGrid()).thenReturn(mockGrid);
    when(mockWalls.getCurrentSector()).thenReturn(mockWallCurrent);
    when(mockWalls.getSectorId()).thenReturn(1);
    mockPosition = mock(Point.class);
    when(mockGrid.getSectorAt(mockPosition)).thenReturn(mockGridCurrent);
    when(mockGrid.getPositionOf(any(Agent.class))).thenReturn(mockPosition);
//    when(mockGrid.getSectorOf(any(Agent.class))).thenReturn(mockGridCurrent);
    
    when(mockWallCurrent.knowsWall(Bearing.N)).thenReturn(Boolean.TRUE);
    when(mockWallCurrent.hasWall(Bearing.N))  .thenReturn(Boolean.TRUE);
    when(mockWallCurrent.knowsWall(Bearing.E)).thenReturn(Boolean.TRUE);
    when(mockWallCurrent.hasNoWall(Bearing.E)).thenReturn(Boolean.TRUE);
    
    instance.work();
    
    verify(mockGridCurrent).setWall(Bearing.N);
    verify(mockGridCurrent).setNoWall(Bearing.E);
    verify(mockGridCurrent).clearWall(Bearing.S);
    verify(mockGridCurrent).clearWall(Bearing.W);
    
    verify(mockGridPart).markSectorChanged(mockGridCurrent);
  }
}
