/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

/**
 *
 * @author MHGameWork
 */
public class TransformedGridTest extends TestCase {

  public void testRotateOppositeWindings() {
    // create two identical grids, but with different sector objects
    Grid grid1 = this.createSquareGridWithFourSectors();
    Grid grid2 = this.createSquareGridWithFourSectors();

    // create a common ReferenceAgent
    Agent reference = this.mockAgent();

    // add the ReferenceAgent on both grids
    grid1.add(reference, new Point(1, 0), Bearing.N);
    grid2.add(reference, new Point(1, 0), Bearing.N);

    TransformedGrid t1 = new TransformedGrid(grid1);
    TransformedGrid t2 = new TransformedGrid(grid2);

    // rotate grid2
    t1.setTransformation(TransformationTRT.fromRotation(Rotation.L90));
    t2.setTransformation(TransformationTRT.fromRotation(Rotation.R270));

    // validate that grid1 hasn't changed
    assertEquals("grid1 is not equal to grid2 when rotated using opposite windings.", t1.toString(), t2.toString());
  }

//  public void testTransformGrid() {
//    // create two identical grids, but with different sector objects
//    Grid grid1 = this.createSquareGridWithFourSectors();
//    Grid grid2 = this.createTransformedSquareGridWithFourSectors();
//
//    // create a common ReferenceAgent
//    Agent reference = this.mockAgent();
//
//    // add the ReferenceAgent on both grids
//    grid1.add(reference, new Point(1, 1), Bearing.N);
//    grid2.add(reference, new Point(1, 2), Bearing.W);
//
//    TransformedGrid t1 = new TransformedGrid(grid1);
//    TransformedGrid t2 = new TransformedGrid(grid2);
//
//
//    // transform grid1
//    t1.setTransformation(this.createGridTransformation());
//
//    // Check if the transformation was successfull
//    assertEquals(t1.toString(), t2.toString(),
//            "Transformed grid1 is not identical to grid2");
//  }
  public void testDoubleTransform() {
    Grid grid = this.createSquareGridWithFourSectors();

    TransformedGrid t1 = new TransformedGrid(grid);
    TransformedGrid t2 = new TransformedGrid(t1);

    t1.setTransformation(createGridTransformation());
    t2.setTransformation(createGridTransformation().invert());

    assertEquals(grid.toString(), t2.toString());
  }

  public void testAddSector() {
    Grid grid = new LinkedGrid();

    Sector s = new LinkedSector();
    s.setWall(Bearing.N);
    s.setWall(Bearing.E);
    s.clearWall(Bearing.S);
    s.setNoWall(Bearing.W);


    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(createGridTransformation());
    tGrid.add(s, new Point(2, 1));
    Sector ts = tGrid.getSectorAt(new Point(2, 1));

    assertTrue(ts.hasWall(Bearing.N));
    assertTrue(ts.hasWall(Bearing.E));
    assertFalse(ts.knowsWall(Bearing.S));
    assertTrue(ts.hasNoWall(Bearing.W));

    ts = grid.getSectorAt(new Point(2, 2));

    assertTrue(ts.hasWall(Bearing.E));
    assertTrue(ts.hasWall(Bearing.S));
    assertFalse(ts.knowsWall(Bearing.W));
    assertFalse(ts.hasWall(Bearing.N));

  }

  public void testGetSectorAt() {
    Grid grid = createSquareGridWithFourSectors();
    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(createGridTransformation());

    Sector ts = tGrid.getSectorAt(new Point(0, 2));

    assertTrue(ts.hasWall(Bearing.N));
    assertTrue(ts.hasWall(Bearing.E));
    assertFalse(ts.knowsWall(Bearing.S));
    assertTrue(ts.hasWall(Bearing.W));

  }

  public void testGetPositionOf() {
    Grid grid = new LinkedGrid();

    Sector s = new LinkedSector();
    s.setWall(Bearing.N);
    s.setWall(Bearing.E);
    s.clearWall(Bearing.S);
    s.setNoWall(Bearing.W);

    String serialized = s.toString();

    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(createGridTransformation());
    tGrid.add(s, new Point(2, 1));

    assertEquals(new Point(2, 1), tGrid.getPositionOf(s));

    assertEquals(new Point(2, 1), tGrid.getPositionOf(tGrid.getSectorAt(new Point(2, 1))));

  }

  public void testAddAgent() {
    Agent a = mockAgent();

    Grid g = new LinkedGrid();
    TransformedGrid tg = new TransformedGrid(g);
    tg.setTransformation(createGridTransformation());
    tg.add(a, new Point(1, 2), Bearing.N);

    assertEquals(a, g.getAgentAt(new Point(1, 1), a.getClass()));
    assertEquals(Bearing.E, g.getBearingOf(a));



  }

  public void testMoveTo() {
    Agent a = mockAgent();

    Grid g = new LinkedGrid();
    g.add(a, new Point(0, 2), Bearing.S);


    TransformedGrid tg = new TransformedGrid(g);
    tg.setTransformation(createGridTransformation());
    tg.moveTo(a, new Point(1, 2), Bearing.N);

    assertEquals(a, g.getAgentAt(new Point(1, 1),a.getClass()));
    assertEquals(Bearing.E, g.getBearingOf(a));
  }

  public void testSectorOf() {
    Agent a = mockAgent();
    Grid grid = createGridWithAgent(a);
    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(createGridTransformation());

    assertEquals(new Point(1, 1), tGrid.getPositionOf(a));

    assertEquals(null, tGrid.getPositionOf(mockAgent()));

  }

  public void testBearingOf() {
    Agent a = mockAgent();
    Grid grid = createGridWithAgent(a);
    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(createGridTransformation());

    assertEquals(Bearing.N, tGrid.getBearingOf(a));
    assertNull(tGrid.getBearingOf(mockAgent()));

  }

  public void testGetAgent() {
    //Nananaaaaa! : http://www.youtube.com/watch?v=BE1gYMF8kgs   !!!
  }

  public void testGetAgentAt() {
    Agent a = mockAgent();
    Grid grid = createGridWithAgent(a);
    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(createGridTransformation());

    assertEquals(a, tGrid.getAgentAt(new Point(1, 1),a.getClass()));
    assertEquals(null, tGrid.getAgentAt(new Point(2, 1),a.getClass()));
    assertEquals(null, tGrid.getAgentAt(new Point(0, 0),a.getClass()));
  }

  public void testGetBounds() {
    Grid grid = createRectangularGrid();
    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(TransformationTRT.Identity);
    assertEquals(-1, tGrid.getMinLeft());
    assertEquals(3, tGrid.getMaxLeft());
    assertEquals(0, tGrid.getMinTop());
    assertEquals(1, tGrid.getMaxTop());
    assertEquals(5, tGrid.getWidth());
    assertEquals(2, tGrid.getHeight());
    tGrid.setTransformation(createGridTransformation());
    assertEquals(0, tGrid.getMinLeft());
    assertEquals(1, tGrid.getMaxLeft());
    assertEquals(0, tGrid.getMinTop());
    assertEquals(4, tGrid.getMaxTop());
    assertEquals(2, tGrid.getWidth());
    assertEquals(5, tGrid.getHeight());


  }

  public void testHasAgentOn() {
  }

  private Grid createRectangularGrid() {
    LinkedGrid grid = new LinkedGrid();
    grid.add(new LinkedSector(), new Point(-1, 0));
    grid.add(new LinkedSector(), new Point(0, 0));
    grid.add(new LinkedSector(), new Point(0, 1));
    grid.add(new LinkedSector(), new Point(1, 0));
    grid.add(new LinkedSector(), new Point(2, 0));
    grid.add(new LinkedSector(), new Point(3, 0));


    return grid;

  }

  // utility methods to setup basic components
  private LinkedGrid createGridWithAgent(Agent a) {
    LinkedGrid grid = new LinkedGrid();

    grid.add(a, new Point(2, 1), Bearing.E);

    return grid;
  }

  // utility methods to setup basic components
  private LinkedGrid createSquareGridWithFourSectors() {
    LinkedGrid grid = new LinkedGrid();
    Sector sector1 = new LinkedSector();
    Sector sector2 = new LinkedSector();
    Sector sector3 = new LinkedSector();
    Sector sector4 = new LinkedSector();

    // Add root sector to grid, rest will follow
    grid.add(sector1, new Point(0, 0));
    grid.add(sector2, new Point(1, 0));
    grid.add(sector3, new Point(0, 1));
    grid.add(sector4, new Point(1, 1));

    // Set the walls
    sector1.setWall(Bearing.N).setWall(Bearing.W);
    sector2.setWall(Bearing.N).setWall(Bearing.E).setWall(Bearing.S);
    sector3.setWall(Bearing.S).setWall(Bearing.W);
    sector4.setWall(Bearing.S).setWall(Bearing.E).setWall(Bearing.N);

    /* result looks like this:
     *    +--+--+
     *    |     |
     *    +  +--+
     *    |     +
     *    +--+--+
     */

    return grid;
  }

  private LinkedGrid createTransformedSquareGridWithFourSectors() {
    LinkedGrid grid = new LinkedGrid();
    Sector sector1 = new LinkedSector();
    Sector sector2 = new LinkedSector();
    Sector sector3 = new LinkedSector();
    Sector sector4 = new LinkedSector();

    // Add root sector to grid, rest will follow
    grid.add(sector1, new Point(0, 2));
    grid.add(sector2, new Point(1, 2));
    grid.add(sector3, new Point(0, 3));
    grid.add(sector4, new Point(1, 3));

    // Set the walls
    sector1.setWall(Bearing.N).setWall(Bearing.W).setWall(Bearing.E);
    sector2.setWall(Bearing.N).setWall(Bearing.W).setWall(Bearing.E);
    sector3.setWall(Bearing.S).setWall(Bearing.W);
    sector4.setWall(Bearing.S).setWall(Bearing.E);

    /* Original looks like this:
     *    +--+--+
     *    |     |
     *    +  +--+
     *    |     +
     *    +--+--+
     * 
     * with topleft (0,0)
     * 
     * Transformation: (-1,-1,Rotation.L90,1,2)
     * 
     * Should look like:
     * 
     *    +--++++
     *    |  |  |
     *    +  +  +
     *    |     |
     *    +--+--+
     * 
     * with topleft (0,2)
     */

    return grid;
  }

  private TransformationTRT createGridTransformation() {
    return new TransformationTRT().setTransformation(-1, -1, Rotation.L90, 1, 2);
  }

  // create a mock that answers what a perfectly working ReferenceAgent
  // should answer when used to import a Grid in a Grid
  private Agent mockAgent() {
    Agent mockedReferenceAgent = mock(Agent.class);
    return mockedReferenceAgent;
  }

  private Grid mockGrid() {
    return mock(Grid.class);
  }
}
