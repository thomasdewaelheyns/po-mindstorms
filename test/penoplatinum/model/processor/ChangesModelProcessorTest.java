package penoplatinum.model.processor;

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
import penoplatinum.grid.PacmanAgent;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.protocol.ProtocolHandler;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChangesModelProcessorTest extends TestCase {

  private Sector mockSector1 = mock(Sector.class);
  private Sector mockSector2 = mock(Sector.class);
  private SensorModelPart mockSensor;
  private GridModelPart mockGridPart;
  private Reporter mockReporter;
  private MessageModelPart mockMessagePart;
  private ProtocolHandler mockProtocol;
  private Model mockModel;

  @Before
  public void setUp() {
    mockSensor = mock(SensorModelPart.class);
    mockGridPart = mock(GridModelPart.class);
    mockModel = mock(Model.class);
    mockMessagePart = mock(MessageModelPart.class);
    mockProtocol = mock(ProtocolHandler.class);
    mockReporter = mock(Reporter.class);

    when(mockModel.getPart(ModelPartRegistry.SENSOR_MODEL_PART)).thenReturn(mockSensor);
    when(mockModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(mockGridPart);
    when(mockModel.getPart(ModelPartRegistry.MESSAGE_MODEL_PART)).thenReturn(mockMessagePart);
    when(mockModel.getReporter()).thenReturn(mockReporter);

    when(mockMessagePart.getProtocolHandler()).thenReturn(mockProtocol);
    when(mockGridPart.getChangedSectors()).thenReturn(Arrays.asList(mockSector1, mockSector2));
    when(mockSensor.isMoving()).thenReturn(Boolean.TRUE);
  }

  /**
   * Test of work method, of class ChangesModelProcessor.
   */
  @Test
  public void testWork() {
    ChangesModelProcessor instance = new ChangesModelProcessor();
    instance.setModel(mockModel);
    instance.work();
    verify(mockGridPart, times(0)).getChangedSectors();
    when(mockSensor.isMoving()).thenReturn(Boolean.FALSE);
    instance.work();
    verify(mockGridPart, times(1)).getChangedSectors();
    verify(mockProtocol, times(1)).handleFoundSector(mockSector1);
    verify(mockProtocol, times(1)).handleFoundSector(mockSector2);
    verify(mockReporter, times(1)).reportSectorUpdate(mockSector1);
    verify(mockReporter, times(1)).reportSectorUpdate(mockSector2);
    
    
    when(mockGridPart.getPacmanID()).thenReturn(1);
    when(mockGridPart.getPacmanAgent()).thenReturn(new PacmanAgent());
    instance.work();
    verify(mockProtocol).handleFoundAgent(any(PacmanAgent.class));
    instance.work();
    verify(mockProtocol).handleFoundAgent(any(PacmanAgent.class));
  }
}
