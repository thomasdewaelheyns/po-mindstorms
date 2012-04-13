/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import junit.framework.Assert;
import penoplatinum.util.Utils;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.grid.Sector;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.grid.SwingGridView;
import penoplatinum.util.TransformationTRT;
import penoplatinum.simulator.Bearing;

/**
 *
 * @author MHGameWork
 */
public class SimpleGridTest {

  SimpleGrid baseGrid;

  @Before
  public void before() {
    baseGrid = new SimpleGrid();

    baseGrid.addSector(new Sector().setCoordinates(0, 1).addWall(Bearing.W));
    baseGrid.addSector(new Sector().setCoordinates(0, 0).addWall(Bearing.N).addWall(Bearing.W));
    baseGrid.addSector(new Sector().setCoordinates(1, 0).addWall(Bearing.N));
    SwingGridView swingGridView = new SwingGridView();

    baseGrid.displayOn(swingGridView);
swingGridView.setTitle("Base");    
  }

  @Test
  public void testImportGridRotationE() {

    SimpleGrid grid;

    grid = new SimpleGrid();
    grid.importGrid(baseGrid, new TransformationTRT().setTransformation(0, 0, 1, 0, 0));
    grid.displayOn(new SwingGridView());

    Utils.Sleep(5000);
  }

  @Test
  public void testImportGridRotationS() {

    SimpleGrid grid;

    grid = new SimpleGrid();
    grid.importGrid(baseGrid, new TransformationTRT().setTransformation(0, 0, 2, 0, 0));
    grid.displayOn(new SwingGridView());

    Utils.Sleep(5000);
  }

  @Test
  public void testImportGridRotationW() {

    SimpleGrid grid;

    grid = new SimpleGrid();
    grid.importGrid(baseGrid, new TransformationTRT().setTransformation(0, 0, 3, 0, 0));
    grid.displayOn(new SwingGridView());

    Utils.Sleep(5000);
  }

  @Test
  public void testImportTranslated() {

    // First create a rotated corner
    SimpleGrid grid;

    grid = new SimpleGrid();
    grid.importGrid(baseGrid, new TransformationTRT().setTransformation(0, 0, 0, 0, 1));
    grid.displayOn(new SwingGridView());

    baseGrid.importGrid(grid, new TransformationTRT().setTransformation(0, 1, 0, -1, 0));
    baseGrid.refresh();

    Utils.Sleep(5000);
  }

  @Test
  public void testImportTranslatedRotated() {

    // First create a copy
    SimpleGrid grid;

    grid = new SimpleGrid();
    grid.importGrid(baseGrid, new TransformationTRT().setTransformation(0, 0, 0, 0, 0));
    grid.displayOn(new SwingGridView());

    baseGrid.importGrid(grid, new TransformationTRT().setTransformation(0, 1, 1, 0, 1));
    baseGrid.refresh();

    Utils.Sleep(5000);
  }

  @Test
  public void testRotationConsistency() {
    // Rotate 4 times, grid should be thesame in the end

    for (int i = 0; i < 4; i++) {
      System.out.println(i);
      TransformationTRT transform = new TransformationTRT().setTransformation(0, 0, i, 0, 0);
      SimpleGrid grid1 = new SimpleGrid();
      grid1.importGrid(baseGrid, transform);
      grid1.displayOn(new SwingGridView());
      transform = new TransformationTRT().setTransformation(0, 0, (4 - i) % 4, 0, 0);

      SimpleGrid grid2 = new SimpleGrid();
      grid2.importGrid(grid1, transform);
      grid2.displayOn(new SwingGridView());
      Assert.assertTrue(baseGrid.areSectorsEqual(grid2));

    }
  }

  @Test
  public void testAreSectorsEqual() {
    Assert.assertTrue(baseGrid.areSectorsEqual(baseGrid));
  }
}
