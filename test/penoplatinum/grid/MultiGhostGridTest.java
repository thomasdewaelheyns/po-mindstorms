/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

/**
 *
 * @author Florian
 */
public class MultiGhostGridTest extends TestCase {

  private Grid mergeGoalGrid = GridTestUtil.createMergedGrid();
  private Grid mergeGoalGrid2 = GridTestUtil.createMergedGrid2();

  @Test
  public void testGridMerging() {
    MultiGhostGrid multiGrid = new MultiGhostGrid("flor");
    GridTestUtil.createGridNorth(multiGrid.getGhostGrid("flor"));
    GridTestUtil.createGridEast(multiGrid.getGhostGrid("michiel").add(new BarcodeAgent(19), new Point(0, 1), Bearing.E));
    assertEquals("the merge wasn't succesful", multiGrid.toString(), mergeGoalGrid.toString());


  }

  @Test
  public void testGridMerging2() {
    MultiGhostGrid multiGrid = new MultiGhostGrid("flor");
    GridTestUtil.createGridNorth(multiGrid.getGhostGrid("flor"));
    GridTestUtil.createGridEast(multiGrid.getGhostGrid("michiel"));
    assertFalse("should be false", multiGrid.toString().equals(mergeGoalGrid.toString()));
    multiGrid.getGhostGrid("michiel").add(new BarcodeAgent(19), new Point(0, 1), Bearing.E);
    assertEquals("the merge wasn't succesful", multiGrid.toString(), mergeGoalGrid.toString());
  }
    @Test
  public void testGridMerging3() {
    MultiGhostGrid multiGrid = new MultiGhostGrid("flor");
    GridTestUtil.createGridNorth(multiGrid.getGhostGrid("flor"));
    GridTestUtil.createGridEast(multiGrid.getGhostGrid("michiel"));
    assertFalse("should be false", multiGrid.toString().equals(mergeGoalGrid.toString()));
    multiGrid.getGhostGrid("michiel").add(new BarcodeAgent(19), new Point(0, 1), Bearing.E);
    assertEquals("the merge wasn't succesful", multiGrid.toString(), mergeGoalGrid.toString());
    GridTestUtil.createGridWest(multiGrid.getGhostGrid("ruben"));
    assertEquals("double merge unsuccesful",multiGrid.toString(), mergeGoalGrid2.toString());

    }
  
}
