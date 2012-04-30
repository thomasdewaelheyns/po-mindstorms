package penoplatinum.model.processor;

import junit.framework.TestCase;
import penoplatinum.util.Point;
import penoplatinum.model.Model;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.util.Bearing;
import static org.mockito.Mockito.*;

public class IRModelProcessorTest extends TestCase {

  private SensorModelPart mockSensor;
  private GridModelPart mockGrid;
  private Model mockModel;

  @Before
  @Override
  public void setUp() {
    mockSensor = mock(SensorModelPart.class);
    mockGrid = mock(GridModelPart.class);
    mockModel = mock(Model.class);
    when(mockModel.getPart(ModelPartRegistry.SENSOR_MODEL_PART)).thenReturn(mockSensor);
    when(mockModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(mockGrid);
  }

  /**
   * Test of work method, of class IRModelProcessor.
   */
  @Test
  public void testWork() {
    IRModelProcessor instance = new IRModelProcessor();
    instance.setModel(mockModel);
    setIRValue(5, 0, 0, 250, 0, 0);
    when(mockGrid.getMyPosition()).thenReturn(new Point(4, 8));
    when(mockGrid.getCurrentBearing()).thenReturn(Bearing.N);
    instance.work();
    verify(mockGrid, times(1)).setPacMan(4, 7);

    setIRValue(2, 200, 250, 0, 0, 0);
    instance.work();
    verify(mockGrid, times(1)).setPacMan(3, 8);

    setIRValue(8, 0, 0, 0, 250, 200);
    instance.work();
    verify(mockGrid, times(1)).setPacMan(5, 8);

  }

  @Test
  public void testOutOfRange() {
    IRModelProcessor instance = new IRModelProcessor();
    instance.setModel(mockModel);
    setIRValue(5, 0, 0, 149, 0, 0);
    when(mockGrid.getMyPosition()).thenReturn(new Point(4, 10));
    instance.work();
    verify(mockGrid, times(0)).setPacMan(4, 7);
  }

  @Test
  public void testMyBearing() {
    IRModelProcessor instance = new IRModelProcessor();
    instance.setModel(mockModel);
    
    when(mockGrid.getMyPosition()).thenReturn(new Point(4, 10));
    setIRValue(8, 0, 0, 0, 250, 200);
    when(mockGrid.getCurrentBearing()).thenReturn(Bearing.N);
    instance.work();
    verify(mockGrid, times(1)).setPacMan(5, 10);

    when(mockGrid.getCurrentBearing()).thenReturn(Bearing.E);
    instance.work();
    verify(mockGrid, times(1)).setPacMan(4, 11);

    when(mockGrid.getCurrentBearing()).thenReturn(Bearing.S);
    instance.work();
    verify(mockGrid, times(1)).setPacMan(3, 10);

    when(mockGrid.getCurrentBearing()).thenReturn(Bearing.W);
    instance.work();
    verify(mockGrid, times(1)).setPacMan(4, 9);
  }

  private void setIRValue(int dir, int i0, int i1, int i2, int i3, int i4) {
    when(mockSensor.getIRDirection()).thenReturn(dir);
    when(mockSensor.getIRValue(0)).thenReturn(i0);
    when(mockSensor.getIRValue(1)).thenReturn(i1);
    when(mockSensor.getIRValue(2)).thenReturn(i2);
    when(mockSensor.getIRValue(3)).thenReturn(i3);
    when(mockSensor.getIRValue(4)).thenReturn(i4);
  }
}
