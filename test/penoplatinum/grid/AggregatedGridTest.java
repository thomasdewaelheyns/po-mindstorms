/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

import junit.framework.TestCase;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

/**
 *
 * @author MHGameWork
 */
public class AggregatedGridTest extends TestCase {

  public void testMainGrid() {
    Grid g = mockGrid();
    AggregatedGrid agg = new AggregatedGrid(g);
    assertEquals(1, agg.getActiveGrids().size());
    assertEquals(g, ((TransformedGrid) agg.getActiveGrids().get(0)).getGrid());
    assertEquals(TransformationTRT.Identity,
            ((TransformedGrid) agg.getActiveGrids().get(0)).getTransformation());
  }

  public void testActivateSubGrid() {
    Grid g = mockGrid();
    AggregatedGrid agg = new AggregatedGrid(mockGrid());
    agg.activateSubGrid(g, mockTransformationTRT());
    assertEquals(2, agg.getActiveGrids().size());
    assertEquals(g, ((TransformedGrid) agg.getActiveGrids().get(1)).getGrid());
    assertEquals(mockTransformationTRT(),
            ((TransformedGrid) agg.getActiveGrids().get(1)).getTransformation());

    agg.activateSubGrid(g, TransformationTRT.Identity);
    assertEquals(2, agg.getActiveGrids().size());
    assertEquals(g, ((TransformedGrid) agg.getActiveGrids().get(1)).getGrid());
    assertEquals(TransformationTRT.Identity,
            ((TransformedGrid) agg.getActiveGrids().get(1)).getTransformation());
  }

  public void testDeactivateSubGrid() {
    Grid g = mockGrid();
    Grid mainGrid = mockGrid();
    AggregatedGrid agg = new AggregatedGrid(mainGrid);
    agg.activateSubGrid(g, mockTransformationTRT());

    agg.deactivateSubGrid(g);
    assertEquals(1, agg.getActiveGrids().size());
    assertEquals(mainGrid, ((TransformedGrid) agg.getActiveGrids().get(0)).getGrid());
    assertEquals(TransformationTRT.Identity,
            ((TransformedGrid) agg.getActiveGrids().get(0)).getTransformation());
  }

  public void testWriteMethods() {
    //TODO
    //testAddSector
    //testAddAgent
    //testMoveAgentTo
  }

  public void testGetSectorAt() {
    //DURR
  }

  public void testGetPositionOfSector() {
    // Durr
  }

  public void testGetSectors() {
    fail();
  }

  public void testGetSectorOf() {
    
  }

  public void testGetBearingOf() {
  }

  public void testGetAgent() {
  }

  public void testGetAgentAt() {
  }

  public void testBounds() {
  }

  public void testSize() {
  }

  public void testHasAgentOn() {
  }

  private Grid mockGrid() {
    return mock(Grid.class);
  }

  private TransformationTRT mockTransformationTRT() {
    return new TransformationTRT().setTransformation(-1, -1, Rotation.L90, 1, 2);
  }
}
