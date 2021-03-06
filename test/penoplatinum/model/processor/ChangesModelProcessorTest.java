package penoplatinum.model.processor;

import penoplatinum.util.Point;
import penoplatinum.grid.Grid;
import java.util.Arrays;
import penoplatinum.grid.Sector;
import penoplatinum.reporter.Reporter;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.grid.agent.PacmanAgent;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.protocol.ProtocolHandler;
import penoplatinum.util.Bearing;
import static org.mockito.Mockito.*;

public class ChangesModelProcessorTest extends TestCase {

  private Sector mockSector1 = mock(Sector.class);
  private Sector mockSector2 = mock(Sector.class);
  private SensorModelPart mockSensor;
  private GridModelPart mockGridPart;
  private Reporter mockReporter;
  private MessageModelPart mockMessagePart;
  private ProtocolHandler mockProtocol;
  private BarcodeModelPart mockBarcodePart;
  private Model mockModel;
  private Grid mockGrid;

  @Before
  public void setUp() {
    mockSensor = mock(SensorModelPart.class);
    mockGridPart = mock(GridModelPart.class);
    mockModel = mock(Model.class);
    mockMessagePart = mock(MessageModelPart.class);
    mockProtocol = mock(ProtocolHandler.class);
    mockReporter = mock(Reporter.class);
    mockBarcodePart = mock(BarcodeModelPart.class);
    mockGrid = mock(Grid.class);

    when(mockModel.getPart(ModelPartRegistry.SENSOR_MODEL_PART)).thenReturn(mockSensor);
    when(mockModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(mockGridPart);
    when(mockModel.getPart(ModelPartRegistry.MESSAGE_MODEL_PART)).thenReturn(mockMessagePart);
    when(mockModel.getPart(ModelPartRegistry.BARCODE_MODEL_PART)).thenReturn(mockBarcodePart);
    when(mockModel.getReporter()).thenReturn(mockReporter);

    when(mockMessagePart.getProtocolHandler()).thenReturn(mockProtocol);
    when(mockGridPart.getChangedSectors()).thenReturn(Arrays.asList(mockSector1, mockSector2));
    when(mockSensor.isMoving()).thenReturn(Boolean.TRUE);
    
    when(mockGridPart.getMyGrid()).thenReturn(mockGrid);
  }

  /**
   * Test of work method, of class ChangesModelProcessor.
   */
  @Test
  public void testWork() {
    ChangesModelProcessor instance = new ChangesModelProcessor();
    instance.setModel(mockModel);
    when(mockGridPart.getMyPosition()).thenReturn(new Point(1, 1));
    when(mockGridPart.getMyBearing()).thenReturn(Bearing.S);
    Grid mockedMyGrid = mock(Grid.class);
    when(mockGridPart.getMyGrid()).thenReturn(mockedMyGrid);
    Sector mockedSector = mock(Sector.class);
    when(mockedMyGrid.getSectors()).thenReturn(Arrays.asList(mockedSector));
    instance.work();
    verify(mockGridPart, times(1)).getChangedSectors(); // work once, DON'T do it again.
    instance.work();
    verify(mockGridPart, times(1)).getChangedSectors();
    when(mockSensor.isMoving()).thenReturn(Boolean.FALSE);
    instance.work();
    verify(mockGridPart, times(1)).getChangedSectors();
    verify(mockProtocol, times(1)).handleFoundSector(mockSector1);
    verify(mockProtocol, times(1)).handleFoundSector(mockSector2);
    verify(mockReporter, times(1)).reportSectorUpdate(mockSector1, "myGrid");
    verify(mockReporter, times(1)).reportSectorUpdate(mockSector2, "myGrid");
    
    when(mockGridPart.getMyPosition()).thenReturn(new Point(1, 0));
    when(mockGridPart.getPacmanID()).thenReturn(1);
    when(mockGridPart.getPacmanAgent()).thenReturn(new PacmanAgent());
    instance.work();
    verify(mockProtocol).handleFoundAgent(any(Grid.class), any(PacmanAgent.class));
    instance.work();
    verify(mockProtocol).handleFoundAgent(any(Grid.class), any(PacmanAgent.class));
  }
}
