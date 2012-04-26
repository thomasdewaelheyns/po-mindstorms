package penoplatinum.model.processor;

/**
 * LightModelProcessorTest
 * 
 * Tests LightModelProcessor interface is available
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


public class LightModelProcessorTest extends TestCase {

  private LightModelProcessor processor;
  private Model mockedModel;

  private SensorModelPart  mockedSensorModelPart;
  private LightModelPart   mockedLightModelPart;


  public LightModelProcessorTest(String name) { 
    super(name);
  }

  public void testNoNewSensorValuesMeansNoWork() {
    this.setup();
    when(this.mockedSensorModelPart.hasNewSensorValues()).thenReturn(false);
    this.processor.work();
    verifyZeroInteractions(this.mockedLightModelPart);
  }
  
  public void testDetectBlack() {
    this.setup();
    when(this.mockedSensorModelPart.hasNewSensorValues()).thenReturn(true);
    when(this.mockedLightModelPart.getCurrentLightValue()).thenReturn(15);
    this.processor.work();
    verify(this.mockedLightModelPart).setAverageLightValue(775);
    verify(this.mockedLightModelPart).setCurrentLightColor(LightColor.BLACK);
  }

  public void testDetectWhite() {
    this.setup();
    when(this.mockedSensorModelPart.hasNewSensorValues()).thenReturn(true);
    when(this.mockedLightModelPart.getCurrentLightValue()).thenReturn(905);
    this.processor.work();
    verify(this.mockedLightModelPart).setAverageLightValue(775);
    verify(this.mockedLightModelPart).setCurrentLightColor(LightColor.WHITE);
  }

  public void testDetectBrown() {
    this.setup();
    when(this.mockedSensorModelPart.hasNewSensorValues()).thenReturn(true);
    when(this.mockedLightModelPart.getCurrentLightValue()).thenReturn(501);
    this.processor.work();
    verify(this.mockedLightModelPart).setAverageLightValue(0.50100005f);
    verify(this.mockedLightModelPart).setCurrentLightColor(LightColor.BROWN);
  }
  
  // TODO: add test for evolution of avg value
  
  private void setup() {
    this.createProcessor();
    this.mockModel();
    this.processor.setModel(this.mockedModel);
  }

  private void createProcessor() {
    this.processor = new LightModelProcessor();
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
