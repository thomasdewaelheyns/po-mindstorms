/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import org.junit.Before;
import org.junit.Test;
import penoplatinum.grid.Sector;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.grid.SwingGridView;
import penoplatinum.pacman.TransformationTRT;
import penoplatinum.simulator.mini.Bearing;

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

    baseGrid.displayOn(new SwingGridView());
  }

  @Test
  public void testImportGridRotationE() {

    SimpleGrid grid;

    grid = new SimpleGrid();
    grid.importGrid(baseGrid, new TransformationTRT().setTransformation(0, 0, 0, 0, 1));
    grid.displayOn(new SwingGridView());

    Utils.Sleep(5000);
  }

  @Test
  public void testImportGridRotationS() {

    SimpleGrid grid;

    grid = new SimpleGrid();
    grid.importGrid(baseGrid, new TransformationTRT().setTransformation(0, 0, 0, 0, 2));
    grid.displayOn(new SwingGridView());

    Utils.Sleep(5000);
  }

  @Test
  public void testImportGridRotationW() {

    SimpleGrid grid;

    grid = new SimpleGrid();
    grid.importGrid(baseGrid, new TransformationTRT().setTransformation(0, 0, 0, 0, 3));
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
}
