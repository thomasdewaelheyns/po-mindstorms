package penoplatinum.model.processor;

/**
 * BarcodeWallsModelProcessorTest
 * 
 * Tests BarcodeWallsModelProcessor interface is available
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;
import org.mockito.Matchers;
import org.mockito.ArgumentMatcher;

import penoplatinum.grid.LinkedSector;

import penoplatinum.model.Model;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.WallsModelPart;

import penoplatinum.util.Bearing;


public class BarcodeWallsModelProcessorTest extends TestCase {

  private BarcodeWallsModelProcessor processor;
  private Model mockedModel;

  private BarcodeModelPart mockedBarcodeModelPart;
  private GridModelPart    mockedGridModelPart;
  private WallsModelPart   mockedWallsModelPart;


  public BarcodeWallsModelProcessorTest(String name) { 
    super(name);
  }

  public void testSetModel() {
    this.setup();
    verify(this.mockedModel).getPart(ModelPartRegistry.BARCODE_MODEL_PART);
    verify(this.mockedModel).getPart(ModelPartRegistry.GRID_MODEL_PART);
    verify(this.mockedModel).getPart(ModelPartRegistry.WALLS_MODEL_PART);
    verifyNoMoreInteractions(this.mockedModel);
  }

  public void testNoWorkWhenNotReadingBarcode() {
    this.setup();
    when(this.mockedBarcodeModelPart.isReadingBarcode()).thenReturn(false);
    this.processor.work();
    verifyZeroInteractions(this.mockedGridModelPart);
  }
  
  public void testWork() {
    this.setup();
    when(this.mockedBarcodeModelPart.isReadingBarcode()).thenReturn(true);
    when(this.mockedGridModelPart.getCurrentBearing()).thenReturn(Bearing.N);
    LinkedSector expected = new LinkedSector();
    expected.setNoWall(Bearing.N)
            .setNoWall(Bearing.S)
            .setWall(Bearing.E)
            .setWall(Bearing.W);
    this.processor.work();
    verify(this.mockedWallsModelPart).updateSector(argThat(new hasOnlyWallsEW()));
  }
  
  private class hasOnlyWallsEW extends ArgumentMatcher<LinkedSector> {
    public boolean matches(Object obj) {
      LinkedSector sector = (LinkedSector) obj;
      return sector.hasNoWall(Bearing.N) &&
             sector.hasNoWall(Bearing.S) &&
             sector.hasWall(Bearing.E) &&
             sector.hasWall(Bearing.W);
    }
  }
  
  public void testWorkOnlyOncePerBarcode() {
    this.setup();
    when(this.mockedBarcodeModelPart.isReadingBarcode()).thenReturn(true);
    when(this.mockedGridModelPart.getCurrentBearing()).thenReturn(Bearing.N);
    this.processor.work();
    verify(this.mockedGridModelPart).getCurrentBearing(); // once
    this.processor.work();    
    verifyNoMoreInteractions(this.mockedGridModelPart);   // not twice
  }

  private void setup() {
    this.createProcessor();
    this.mockModel();
    this.processor.setModel(this.mockedModel);
  }

  private void createProcessor() {
    this.processor = new BarcodeWallsModelProcessor();
  }
  
  private void mockModel() {
    this.mockedModel = mock(Model.class);
    // barcode
    this.mockedBarcodeModelPart = mock(BarcodeModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.BARCODE_MODEL_PART))
      .thenReturn(this.mockedBarcodeModelPart);
    // grid
    this.mockedGridModelPart = mock(GridModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.GRID_MODEL_PART))
      .thenReturn(this.mockedGridModelPart);
    // walls
    this.mockedWallsModelPart = mock(WallsModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.WALLS_MODEL_PART))
      .thenReturn(this.mockedWallsModelPart);
  }
}
