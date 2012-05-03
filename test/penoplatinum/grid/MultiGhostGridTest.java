/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;
import penoplatinum.util.Utils;
import static org.mockito.Mockito.*;

/**
 *
 * @author Florian
 */
public class MultiGhostGridTest extends TestCase {

  private Grid mergeGoalGrid = GridTestUtil.createMergedGrid();
  private Grid mergeGoalGrid2 = GridTestUtil.createMergedGrid2();

  @Test
  public void testSingleGhost() {
    MultiGhostGrid multiGrid = new MultiGhostGrid("flor");
    AggregatedGrid agg = new AggregatedGrid();
    multiGrid.useAggregatedGrid(agg);
    GridTestUtil.createGridNorth(multiGrid.getGhostGrid("flor"));

//    (new SwingGridView()).display(multiGrid.getGhostGrid("flor"));
//    (new SwingGridView()).display(multiGrid);

    assertEquals("the merge wasn't succesful", multiGrid.toString(), GridTestUtil.createGridNorth(new LinkedGrid()).toString());
//    Utils.Sleep(1000000);



  }

  @Test
  public void testGridMergingMocked() {
    MultiGhostGrid multiGrid = new MultiGhostGrid("flor");
    AggregatedGrid agg = mock(AggregatedGrid.class);
    multiGrid.useAggregatedGrid(agg);
    GridTestUtil.createGridNorth(multiGrid.getGhostGrid("flor"));
    GridTestUtil.createGridEast(multiGrid.getGhostGrid("michiel"));
    TransformationTRT trans = new TransformationTRT().setTransformation(0, 0, Rotation.L90, 0, 0);

    verify(agg, never()).activateSubGrid(eq(multiGrid.getGhostGrid("michiel")), any(TransformationTRT.class));

    multiGrid.getGhostGrid("michiel").add(BarcodeAgent.getBarcodeAgent(19), new Point(0, 1), Bearing.E);

    verify(agg).activateSubGrid(multiGrid.getGhostGrid("michiel"), trans);
  }

  @Test
  public void testGridMerging() {
    MultiGhostGrid multiGrid = new MultiGhostGrid("flor");
    AggregatedGrid agg = new AggregatedGrid();//mock(AggregatedGrid.class);
    multiGrid.useAggregatedGrid(agg);
    GridTestUtil.createGridNorth(multiGrid.getGhostGrid("flor"));
    GridTestUtil.createGridEast(multiGrid.getGhostGrid("michiel"));
    
    multiGrid.getGhostGrid("michiel").add(BarcodeAgent.getBarcodeAgent(19), new Point(0, 1), Bearing.E);
    (new SwingGridView()).display(multiGrid.getGhostGrid("flor"));
    (new SwingGridView()).display(multiGrid.getGhostGrid("michiel"));
    (new SwingGridView()).display(multiGrid);

    Utils.Sleep(1000000);
    assertEquals("the merge wasn't succesful", multiGrid.toString(), mergeGoalGrid.toString());


  }

  @Test
  public void testGridMerging2() {
    MultiGhostGrid multiGrid = new MultiGhostGrid("flor");
    GridTestUtil.createGridNorth(multiGrid.getGhostGrid("flor"));
    GridTestUtil.createGridEast(multiGrid.getGhostGrid("michiel"));
    assertFalse("should be false", multiGrid.toString().equals(mergeGoalGrid.toString()));
    multiGrid.getGhostGrid("michiel").add(BarcodeAgent.getBarcodeAgent(19), new Point(0, 1), Bearing.E);
    assertEquals("the merge wasn't succesful", multiGrid.toString(), mergeGoalGrid.toString());
  }

  @Test
  public void testGridMerging3() {
    MultiGhostGrid multiGrid = new MultiGhostGrid("flor");
    GridTestUtil.createGridNorth(multiGrid.getGhostGrid("flor"));
    GridTestUtil.createGridEast(multiGrid.getGhostGrid("michiel"));
    assertFalse("should be false", multiGrid.toString().equals(mergeGoalGrid.toString()));
    multiGrid.getGhostGrid("michiel").add(BarcodeAgent.getBarcodeAgent(19), new Point(0, 1), Bearing.E);
    assertEquals("the merge wasn't succesful", multiGrid.toString(), mergeGoalGrid.toString());
    GridTestUtil.createGridWest(multiGrid.getGhostGrid("ruben"));
    assertEquals("double merge unsuccesful", multiGrid.toString(), mergeGoalGrid2.toString());

  }
}
