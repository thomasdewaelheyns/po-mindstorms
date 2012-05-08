package penoplatinum.model.processor;

/**
 * LightModelProcessorTest
 * 
 * Tests LightModelProcessor class
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
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(0);
    this.processor.work();
    verifyZeroInteractions(this.mockedLightModelPart);
  }
  
  public void testDetectBlack() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(1, 2, 3, 4, 5);
    when(this.mockedSensorModelPart.getLightSensorValue()).thenReturn(15);
    this.processor.work();
    //verify(this.mockedLightModelPart).setAverageLightValue(505);
    verify(this.mockedLightModelPart).setCurrentLightColor(LightColor.BLACK);
  }

  public void testDetectWhite() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(1, 2, 3, 4, 5);
    when(this.mockedSensorModelPart.getLightSensorValue()).thenReturn(905);
    this.processor.work();

    //verify(this.mockedLightModelPart).setAverageLightValue(505);
    verify(this.mockedLightModelPart).setCurrentLightColor(LightColor.WHITE);
  }

  public void testDetectBrown() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(1, 2, 3, 4, 5);
    when(this.mockedSensorModelPart.getLightSensorValue()).thenReturn(501);
    this.processor.work();
    //verify(this.mockedLightModelPart).setAverageLightValue(504.996f);
    verify(this.mockedLightModelPart).setCurrentLightColor(LightColor.BROWN);
  }
  
  public void testAverageLightValueOverLongTime() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(1, 2, 3, 4, 5);
    // int he beginning, the average light value is 505
    // after 150 readings of 471 the average light value is 500.3204
    when(this.mockedSensorModelPart.getLightSensorValue())
      .thenReturn(469);

    // int the beginning ...
    this.processor.work();
    //verify(this.mockedLightModelPart).setAverageLightValue(504.966f);
    verify(this.mockedLightModelPart).setCurrentLightColor(LightColor.BLACK);
    when(this.mockedSensorModelPart.getLightSensorValue())
      .thenReturn(471);

    // 150 readings later ...
    this.processor.work();
    //verify(this.mockedLightModelPart).setAverageLightValue(479.99002f);
    verify(this.mockedLightModelPart).setCurrentLightColor(LightColor.BROWN);
  }
  
  // construction helpers
  
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
