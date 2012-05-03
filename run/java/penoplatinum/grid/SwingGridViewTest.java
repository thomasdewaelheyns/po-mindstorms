/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java.penoplatinum.grid;

import junit.framework.TestCase;
import org.junit.Test;
import penoplatinum.grid.Grid;
import penoplatinum.grid.GridBoard;
import penoplatinum.grid.GridTestUtil;
import penoplatinum.grid.LinkedGrid;
import penoplatinum.grid.SwingGridView;

/**
 *
 * @author Florian
 */
public class SwingGridViewTest extends TestCase {

  @Test
  public static void testRenderMap() {


    Grid grid1 = new LinkedGrid();
    GridTestUtil.createGridNorth(grid1);
    Grid grid2 = new LinkedGrid();
    GridTestUtil.createGridEast(grid2);
    Grid grid3 = new LinkedGrid();
    GridTestUtil.createGridEast(grid3);
    Grid grid4 = GridTestUtil.createMergedGrid();
    Grid grid5 = GridTestUtil.createMergedGrid2();
    
    SwingGridView showme = new SwingGridView();
    SwingGridView showme2 = new SwingGridView();
    SwingGridView showme3 = new SwingGridView();
    SwingGridView showme4 = new SwingGridView();
    SwingGridView showme5 = new SwingGridView();
    showme.display(grid1);
    showme2.display(grid2);
    showme3.display(grid3);
    showme4.display(grid4);
    showme5.display(grid5);

  }

  ;

    

  public static void main(String[] args) {
    testRenderMap();
  }
}
