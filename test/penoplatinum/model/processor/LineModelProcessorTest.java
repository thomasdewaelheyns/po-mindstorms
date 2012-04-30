package penoplatinum.model.processor;

/**
 * LineModelProcessorTest
 * 
 * Tests LineModelProcessor class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.model.Model;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.LightModelPart;

import penoplatinum.util.LightColor;
import penoplatinum.util.Line;


public class LineModelProcessorTest extends TestCase {

  private LineModelProcessor processor;
  private Model mockedModel;

  private SensorModelPart mockedSensorModelPart;
  private LightModelPart  mockedLightModelPart;


  public LineModelProcessorTest(String name) { 
    super(name);
  }

  public void testNoNewSensorValuesMeansNoWork() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesId()).thenReturn(0);
    this.processor.work();
    verifyZeroInteractions(this.mockedLightModelPart);
  }

  // construction helpers
  
  private void setup() {
    this.createProcessor();
    this.mockModel();
    this.processor.setModel(this.mockedModel);
  }

  private void createProcessor() {
    this.processor = new LineModelProcessor();
  }
  
  private void mockModel() {
    this.mockedModel = mock(Model.class);
    // sensor
    this.mockedSensorModelPart = mock(SensorModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.SENSOR_MODEL_PART))
      .thenReturn(this.mockedSensorModelPart);
    // light
    this.mockedLightModelPart = mock(LightModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.LIGHT_MODEL_PART))
      .thenReturn(this.mockedLightModelPart);
  }
}