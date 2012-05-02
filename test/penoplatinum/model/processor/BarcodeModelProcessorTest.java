package penoplatinum.model.processor;

/**
 * BarcodeModelProcessorTest
 * 
 * Tests BarcodeModelProcessor interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.model.Model;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.LightModelPart;

import penoplatinum.util.LightColor;


public class BarcodeModelProcessorTest extends TestCase {

  private BarcodeModelProcessor processor;
  private Model mockedModel;

  private BarcodeModelPart mockedBarcodeModelPart;
  private SensorModelPart  mockedSensorModelPart;
  private LightModelPart   mockedLightModelPart;


  public BarcodeModelProcessorTest(String name) { 
    super(name);
  }

  public void testSetModel() {
    this.setup();
    verify(this.mockedModel).getPart(ModelPartRegistry.BARCODE_MODEL_PART);
    verify(this.mockedModel).getPart(ModelPartRegistry.SENSOR_MODEL_PART);
    verify(this.mockedModel).getPart(ModelPartRegistry.LIGHT_MODEL_PART);
    verifyNoMoreInteractions(this.mockedModel);
  }

  public void testNoWorkWithoutNewSensorValues() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(0);
    this.processor.work();
    verify(this.mockedSensorModelPart).getValuesID();
    verifyNoMoreInteractions(this.mockedSensorModelPart);
    verifyZeroInteractions(this.mockedBarcodeModelPart,
                           this.mockedLightModelPart);
  }                         

  public void testWorkAndWait() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(1);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn(LightColor.BROWN);
    this.processor.work();
    verify(this.mockedSensorModelPart).getValuesID();
    verify(this.mockedLightModelPart).getCurrentLightColor();
    this.processor.work();
    verify(this.mockedSensorModelPart, times(2)).getValuesID();
    verifyNoMoreInteractions(this.mockedLightModelPart);
  }
  
  public void testWorkAndStart() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(1, 2, 3, 4, 5);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn(LightColor.BROWN, LightColor.BLACK, LightColor.BLACK);
    this.processor.work();
    this.processor.work();
    verify(this.mockedBarcodeModelPart).startNewReading();
    verify(this.mockedBarcodeModelPart).addReading(LightColor.BLACK);
  }

  public void testWorkAndTurn() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID()).thenReturn(1, 2, 3, 4, 5);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn(LightColor.BROWN, LightColor.BLACK, LightColor.BLACK);
    when(this.mockedSensorModelPart.isTurning()).thenReturn(true);
    this.processor.work(); // waiting
    this.processor.work(); // starting
    this.processor.work(); // reading
    verify(this.mockedBarcodeModelPart).discardReading();
  }

  public void testWorkWithInterference() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID())
      .thenReturn(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn(LightColor.BROWN, LightColor.BLACK, LightColor.BLACK,
                  LightColor.WHITE, LightColor.BROWN, LightColor.BROWN,
                  LightColor.BROWN, LightColor.BROWN, LightColor.BROWN,
                  LightColor.BROWN, LightColor.BROWN);
    this.processor.work(); // waiting
    this.processor.work(); // starting, reading BLACK
    this.processor.work(); // reading WHITE
    this.processor.work(); // reading BROWN
    this.processor.work(); // reading BROWN
    this.processor.work(); // reading BROWN
    this.processor.work(); // reading BROWN
    this.processor.work(); // reading BROWN
    this.processor.work(); // reading BROWN
    verify(this.mockedBarcodeModelPart, never()).discardReading();
    this.processor.work(); // reading -----
    verify(this.mockedBarcodeModelPart).discardReading();
  }

  public void testWorkAndStop() {
    this.setup();
    when(this.mockedSensorModelPart.getValuesID())
      .thenReturn(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                  16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
    when(this.mockedLightModelPart.getCurrentLightColor())
      .thenReturn(LightColor.BROWN, LightColor.BLACK, LightColor.BLACK,
                  LightColor.WHITE, LightColor.BLACK, LightColor.WHITE,
                  LightColor.BLACK, LightColor.WHITE, LightColor.BLACK,
                  LightColor.WHITE, LightColor.BROWN, LightColor.BROWN,
                  LightColor.BROWN, LightColor.BROWN, LightColor.BROWN,
                  LightColor.BROWN, LightColor.BROWN, LightColor.BROWN,
                  LightColor.BROWN, LightColor.BROWN  );

    for(int i=0; i<20; i++) { // 20 = 1 brown, 8 barcode, 10+1 brown
      this.processor.work();
    }
    verify(this.mockedBarcodeModelPart, never()).discardReading();
    verify(this.mockedBarcodeModelPart, never()).finishReading();

    this.processor.work();

    verify(this.mockedBarcodeModelPart, never()).discardReading();
    verify(this.mockedBarcodeModelPart).finishReading();
  }
  
  private void setup() {
    this.createProcessor();
    this.mockModel();
    this.processor.setModel(this.mockedModel);
  }

  private void createProcessor() {
    this.processor = new BarcodeModelProcessor();
  }
  
  private void mockModel() {
    this.mockedModel = mock(Model.class);
    // barcode
    this.mockedBarcodeModelPart = mock(BarcodeModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.BARCODE_MODEL_PART))
      .thenReturn(this.mockedBarcodeModelPart);
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
