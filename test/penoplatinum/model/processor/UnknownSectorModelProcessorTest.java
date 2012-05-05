package penoplatinum.model.processor;

import penoplatinum.util.Point;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Grid;
import org.junit.Test;
import junit.framework.TestCase;
import org.junit.Before;
import penoplatinum.grid.LinkedSector;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.util.Bearing;
import static org.mockito.Mockito.*;

public class UnknownSectorModelProcessorTest extends TestCase {
  
  private Sector mockCurrentSector;
  private Grid mockGrid;
  private GridModelPart mockGridPart;
  private Model mockModel;
  private Point mockPosition;
  
  @Before
  public void setUp() {
    mockCurrentSector = mock(Sector.class);
    mockGrid = mock(Grid.class);
    mockGridPart = mock(GridModelPart.class);
    mockModel = mock(Model.class);
    when(mockModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(mockGridPart);
    when(mockGridPart.getMyGrid()).thenReturn(mockGrid);
    when(mockGridPart.getMySector()).thenReturn(mockCurrentSector);
    mockPosition = mock(Point.class);
    when(mockGrid.getSectorAt(mockPosition)).thenReturn(mockCurrentSector);
    when(mockGrid.getPositionOf(any(Agent.class))).thenReturn(mockPosition);
  }

  // Test of work method, of class UnknownSectorModelProcessor.
  @Test
  public void testWork() {
    UnknownSectorModelProcessor instance = new UnknownSectorModelProcessor();
    instance.setModel(mockModel);
    when(mockGridPart.hasChangedSectors()).thenReturn(true);
    when(mockCurrentSector.givesAccessTo(Bearing.E)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.givesAccessTo(Bearing.S)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.givesAccessTo(Bearing.W)).thenReturn(Boolean.TRUE);
    when(mockCurrentSector.hasNeighbour(Bearing.S)).thenReturn(Boolean.TRUE);
    instance.work();
    verify(mockCurrentSector).addNeighbour(any(LinkedSector.class), eq(Bearing.E));
    verify(mockCurrentSector).addNeighbour(any(LinkedSector.class), eq(Bearing.W));
  }
}
