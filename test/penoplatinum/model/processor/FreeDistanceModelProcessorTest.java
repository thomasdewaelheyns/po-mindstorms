package penoplatinum.model.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.part.WallsModelPart;
import penoplatinum.model.Model;
import penoplatinum.model.part.ModelPartRegistry;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class FreeDistanceModelProcessorTest extends TestCase {

  private SonarModelPart mockSonar;
  private WallsModelPart mockWalls;
  private Model mockModel;
  
  @Before
  @Override
  public void setUp() {
    mockSonar = mock(SonarModelPart.class);
    mockWalls = mock(WallsModelPart.class);
    mockModel = mock(Model.class);
    when(mockModel.getPart(ModelPartRegistry.SONAR_MODEL_PART)).thenReturn(mockSonar);
    when(mockModel.getPart(ModelPartRegistry.WALLS_MODEL_PART)).thenReturn(mockWalls);
  }

  /**
   * Test of work method, of class FreeDistanceModelProcessor.
   */
  @Test
  public void testWork() {
    FreeDistanceModelProcessor instance = new FreeDistanceModelProcessor();
    instance.setModel(mockModel);
    int[] angles = new int[]{90, 0, -90};
    int[] distances = new int[]{0, 0, 0};
    makeSweep(angles, distances, 1);
    
    instance.work();;
    verify(mockWalls).setWallLeftDistance(0);
    verify(mockWalls).setWallFrontDistance(0);
    verify(mockWalls).setWallRightDistance(0);
    
    distances[0] = 1;
    distances[1] = 2;
    distances[2] = 3;
    makeSweep(angles, distances, 2);
    
    instance.work();
    verify(mockWalls).setWallLeftDistance(1);
    verify(mockWalls).setWallFrontDistance(2);
    verify(mockWalls).setWallRightDistance(3);
    
    angles = new int[]{-90, -89, -88, 0, 90};
    distances = new int[]{2,4,6,8,10};
    makeSweep(angles, distances, 3);
    instance.work();
    verify(mockWalls).setWallLeftDistance(10);
    verify(mockWalls).setWallFrontDistance(8);
    verify(mockWalls).setWallRightDistance(4);
    
    verifyNoMoreInteractions(mockWalls);
    angles = new int[]{90, 0, -90};
    distances = new int[]{0, 0, 0};
    makeSweep(angles, distances, 3);
    instance.work();
  }

  private void makeSweep(int[] angles, int[] distances, int id) {
    when(mockSonar.getCurrentSweepId()).thenReturn(id);
    List<Integer> dist = new ArrayList<Integer>();
    for(int b : distances){
      dist.add(b);
    }
    when(mockSonar.getAngles()).thenReturn(angles);
    when(mockSonar.getDistances()).thenReturn(dist);
  }
}
