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
  
  public void testDetectNoLineOnBrown() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesId())
      .thenReturn(1,2,3,4,5);
    when(this.mockedSensorModelPart.isTurning())
      .thenReturn(false);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn(LightColor.BROWN);
    this.processor.work();
    this.processor.work();
    this.processor.work();
    this.processor.work();
    this.processor.work();
    verify(this.mockedLightModelPart, times(5)).getCurrentLightColor();
  }

  public void testDetectWhiteLine() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesId())
      .thenReturn(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
    when(this.mockedSensorModelPart.isTurning())
      .thenReturn(false);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn( LightColor.BROWN,
                   LightColor.WHITE, LightColor.WHITE, LightColor.WHITE,
                   LightColor.WHITE, LightColor.WHITE,
                   LightColor.BROWN, LightColor.BROWN, LightColor.BROWN,
                   LightColor.BROWN, LightColor.BROWN );
    for(int i=0;i<12;i++) { // 11 readings + 1 : we detect in the next frame
      this.processor.work();
    }
    verify(this.mockedLightModelPart, times(12)).setLine(Line.NONE);
    verify(this.mockedLightModelPart, times(12)).getCurrentLightColor();
    verify(this.mockedLightModelPart).setLine(Line.WHITE);
    verifyNoMoreInteractions(this.mockedLightModelPart);
  }

  public void testBlackWhenReadingWhiteEndsDetection() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesId())
      .thenReturn(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
    when(this.mockedSensorModelPart.isTurning())
      .thenReturn(false);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn( LightColor.BROWN,
                   LightColor.WHITE, LightColor.WHITE, LightColor.WHITE,
                   LightColor.WHITE, LightColor.WHITE,
                   LightColor.BLACK, LightColor.BROWN, LightColor.BROWN,
                   LightColor.BROWN, LightColor.BROWN );
    for(int i=0;i<12;i++) { // 11 readings + 1 : we detect in the next frame
      this.processor.work();
    }
    verify(this.mockedLightModelPart, times(12)).setLine(Line.NONE);
    verify(this.mockedLightModelPart, times(12)).getCurrentLightColor();
    verifyNoMoreInteractions(this.mockedLightModelPart);
  }

  public void testTurningCausesAbortedDetection() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesId())
      .thenReturn(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
    when(this.mockedSensorModelPart.isTurning())
      .thenReturn(false, false, false, true);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn( LightColor.BROWN,
                   LightColor.WHITE, LightColor.WHITE, LightColor.WHITE,
                   LightColor.WHITE, LightColor.WHITE,
                   LightColor.BLACK, LightColor.BROWN, LightColor.BROWN,
                   LightColor.BROWN, LightColor.BROWN );
    for(int i=0;i<12;i++) { // 11 readings + 1 : we detect in the next frame
      this.processor.work();
    }
    verify(this.mockedLightModelPart, times(12)).setLine(Line.NONE);
    verify(this.mockedLightModelPart, times(3)).getCurrentLightColor();
    verifyNoMoreInteractions(this.mockedLightModelPart);
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