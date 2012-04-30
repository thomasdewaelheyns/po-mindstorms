package penoplatinum.model.processor;

/**
 * WallDetectionModelProcessorTest
 * 
 * Tests WallDetectionModelProcessor class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;
import org.mockito.Matchers;
import org.mockito.ArgumentMatcher;

import penoplatinum.grid.Sector;
import penoplatinum.grid.LinkedSector;

import penoplatinum.model.Model;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.part.WallsModelPart;
import penoplatinum.model.part.GridModelPart;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;


public class WallDetectionModelProcessorTest extends TestCase {

  private WallDetectionModelProcessor processor;
  private Model mockedModel;

  private SonarModelPart mockedSonarModelPart;
  private WallsModelPart mockedWallsModelPart;
  private GridModelPart  mockedGridModelPart;


  public WallDetectionModelProcessorTest(String name) { 
    super(name);
  }

  public void testNoNewSonarValuesMeansNoWork() {
    this.setup();
    when(this.mockedSonarModelPart.getCurrentSweepId()).thenReturn(0);
    this.processor.work();
    verifyZeroInteractions(this.mockedWallsModelPart);
  }
  
  public void testDetectFirstSector() {
    this.setup();
    when(this.mockedSonarModelPart.getCurrentSweepId()).thenReturn(1);
    when(this.mockedGridModelPart.getMyBearing()).thenReturn(Bearing.N);
    //   +-------+
    //   |
    //   |   ^
    //   +
    when(this.mockedWallsModelPart.isWallFront()).thenReturn(true);
    when(this.mockedWallsModelPart.isWallLeft()).thenReturn(true);
    when(this.mockedWallsModelPart.isWallRight()).thenReturn(false);
    when(this.mockedWallsModelPart.getCurrentSector()).thenReturn(null);
    when(this.mockedGridModelPart.getCurrentPosition()).thenReturn(new Point(0,0));
    
    this.processor.work();
    
    Sector expected = new LinkedSector().setWall(Bearing.N)
                                        .setWall(Bearing.W)
                                        .setNoWall(Bearing.E);
    
    verify(this.mockedWallsModelPart)
      .updateSector(argThat(new hasSameWalls(expected)));
  }

  public void testTurnRightOnFirstSector() {
    this.setup();
    //   +-------+
    //   |
    //   |   ^
    //   +
    Sector prevSector = new LinkedSector().setWall(Bearing.N)
                                          .setWall(Bearing.W)
                                          .setNoWall(Bearing.E);
    when(this.mockedSonarModelPart.getCurrentSweepId())
      .thenReturn(1, 2);
    when(this.mockedGridModelPart.getMyBearing())
      .thenReturn(Bearing.N, Bearing.E);
    when(this.mockedWallsModelPart.isWallFront())
      .thenReturn(true, false);
    when(this.mockedWallsModelPart.isWallLeft())
      .thenReturn(true, true);
    when(this.mockedWallsModelPart.isWallRight())
      .thenReturn(false, false);
    when(this.mockedWallsModelPart.getCurrentSector())
      .thenReturn(null, prevSector);
    when(this.mockedGridModelPart.getCurrentPosition())
      .thenReturn(new Point(0,0), new Point(0,0));
    
    this.processor.work();
    this.processor.work();
    
    //   +-------+
    //   |
    //   |   >
    //   +
    Sector expected = new LinkedSector().setWall(Bearing.N)
                                        .setNoWall(Bearing.E)
                                        .setNoWall(Bearing.S)
                                        .setWall(Bearing.W);
    verify(this.mockedWallsModelPart)
      .updateSector(argThat(new hasSameWalls(prevSector)));
    verify(this.mockedWallsModelPart)
      .updateSector(argThat(new hasSameWalls(expected)));
  }
  
  private class hasSameWalls extends ArgumentMatcher<LinkedSector> {
    private Sector expected;
    
    public hasSameWalls() { super(); }
    public hasSameWalls(Sector expected) {
      this();
      this.expected = expected;
    }
    public boolean matches(Object obj) {
      LinkedSector sector = (LinkedSector) obj;
      for(Bearing bearing : Bearing.NESW) {
        if( this.expected.knowsWall(bearing) != sector.knowsWall(bearing) ) {
          return false;
        }
        if( sector.knowsWall(bearing) && 
            ( this.expected.hasWall(bearing)   != sector.hasWall(bearing) ) )
        {
          return false;
        }
      }
      return true;
    }
  }

  // TODO: add more interesting test cases

  
  // construction helpers
  
  private void setup() {
    this.createProcessor();
    this.mockModel();
    this.processor.setModel(this.mockedModel);
  }

  private void createProcessor() {
    this.processor = new WallDetectionModelProcessor();
  }
  
  private void mockModel() {
    this.mockedModel = mock(Model.class);
    // sonar
    this.mockedSonarModelPart = mock(SonarModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.SONAR_MODEL_PART))
      .thenReturn(this.mockedSonarModelPart);
    // walls
    this.mockedWallsModelPart = mock(WallsModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.WALLS_MODEL_PART))
      .thenReturn(this.mockedWallsModelPart);
    // grid
    this.mockedGridModelPart = mock(GridModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.GRID_MODEL_PART))
      .thenReturn(this.mockedGridModelPart);
  }
}
