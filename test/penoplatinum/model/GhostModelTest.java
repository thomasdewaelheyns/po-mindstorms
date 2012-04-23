package penoplatinum.model;

/**
 * GhostModelTest
 * 
 * Tests GhostModel class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.model.processor.ModelProcessor;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.WallsModelPart;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.GridModelPart;

import penoplatinum.reporter.Reporter;


public class GhostModelTest extends TestCase {

  private GhostModel     model;
  private ModelProcessor mockedModelProcessor;
  private Reporter       mockedReporter;


  public GhostModelTest(String name) { 
    super(name);
  }

  public void testConstructor() {
    this.setup();
    assertTrue(this.model.getPart(ModelPartRegistry.SENSOR_MODEL_PART) 
               instanceof SensorModelPart);
    assertTrue(this.model.getPart(ModelPartRegistry.WALLS_MODEL_PART) 
               instanceof WallsModelPart);
    assertTrue(this.model.getPart(ModelPartRegistry.BARCODE_MODEL_PART) 
               instanceof BarcodeModelPart);
    assertTrue(this.model.getPart(ModelPartRegistry.LIGHT_MODEL_PART) 
               instanceof LightModelPart);
    assertTrue(this.model.getPart(ModelPartRegistry.GRID_MODEL_PART) 
               instanceof GridModelPart);
	}
	
  public void testSetProcessor() {
    this.setup();
    verify(this.mockedModelProcessor).setModel(this.model);
  }

  public void testGetSetReporter() {
    this.setup();
    assertSame(this.mockedReporter, this.model.getReporter());
  }

  public void testRefresh() {
    this.setup();
    this.model.refresh();
    verify(this.mockedModelProcessor).process();
    verify(this.mockedReporter).reportModelUpdate();
  }

  private void setup() {
    this.createGhostModel();
    this.mockModelProcessor();
    this.model.setProcessor(this.mockedModelProcessor);
    this.mockReporter();
    this.model.setReporter(this.mockedReporter);
  }
  
	private void createGhostModel() {
	  this.model = new GhostModel();
  }

  private void mockModelProcessor() {
    this.mockedModelProcessor = mock(ModelProcessor.class);
  }
  
  private void mockReporter() {
    this.mockedReporter = mock(Reporter.class);
  }
}
