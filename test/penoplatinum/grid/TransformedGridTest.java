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
    Agent reference = this.mockReferenceAgent(Bearing.E);

    // add the ReferenceAgent on both grids
    grid1.add(reference, new Point(1, 0), Bearing.N);
    grid2.add(reference, new Point(1, 0), Bearing.N);

    TransformedGrid t1 = new TransformedGrid(grid1);
    TransformedGrid t2 = new TransformedGrid(grid2);

    // rotate grid2
    t1.setTransformation(TransformationTRT.fromRotation(Rotation.L90));
    t2.setTransformation(TransformationTRT.fromRotation(Rotation.R270));

    // validate that grid1 hasn't changed
    assertEquals(t1.toString(), t2.toString(),
            "grid1 is not equal to grid2 when rotated using opposite windings.");
  }

  public void testTransformGrid() {
    // create two identical grids, but with different sector objects
    Grid grid1 = this.createSquareGridWithFourSectors();
    Grid grid2 = this.createTransformedSquareGridWithFourSectors();

    // create a common ReferenceAgent
    Agent reference = this.mockReferenceAgent(Bearing.E);

    // add the ReferenceAgent on both grids
    grid1.add(reference, new Point(1, 1), Bearing.N);
    grid2.add(reference, new Point(1, 2), Bearing.W);
    
    TransformedGrid t1 = new TransformedGrid(grid1);
    TransformedGrid t2 = new TransformedGrid(grid2);
    

    // transform grid1
    t1.setTransformation(this.createGridTransformation());

    // Check if the transformation was successfull
    assertEquals(t1.toString(), t2.toString(),
            "Transformed grid1 is not identical to grid2");
  }

  public void testAddSector()
  {
    Grid grid = new LinkedGrid();
    
    Sector s = new LinkedSector();
    s.setWall(Bearing.N);
    s.setWall(Bearing.E);
    s.clearWall(Bearing.S);
    s.setNoWall(Bearing.W);
    
    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(createGridTransformation());
    tGrid.add(s, new Point(2,1));
    Sector ts = tGrid.getSectorAt(new Point(1,3));
    
    assertTrue(ts.hasWall(Bearing.W));
    assertTrue(ts.hasWall(Bearing.N));
    assertFalse(ts.knowsWall(Bearing.E));
    assertFalse(ts.hasWall(Bearing.S));
    
  }
  
   public void testAddSector()
  {
    Grid grid = new LinkedGrid();
    
    Sector s = new LinkedSector();
    
    TransformedGrid tGrid = new TransformedGrid(grid);
    tGrid.setTransformation(createGridTransformation());
    tGrid.add(s, new Point(2,1));
    Sector ts = tGrid.getSectorAt();
    
    assertEquals( new Point(1,3), tGrid.getPositionOf(ts));
    assertTrue(ts.hasWall(Bearing.N));
    assertFalse(ts.knowsWall(Bearing.E));
    assertFalse(ts.hasWall(Bearing.S));
    
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

    // Add using addNeighbour
    sector1.addNeighbour(sector2, Bearing.E);
    sector1.addNeighbour(sector3, Bearing.S);
    sector2.addNeighbour(sector4, Bearing.S);

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

    // Add using addNeighbour
    sector1.addNeighbour(sector2, Bearing.E);
    sector1.addNeighbour(sector3, Bearing.S);
    sector2.addNeighbour(sector4, Bearing.S);

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
  private Agent mockReferenceAgent(Bearing bearing) {
    Agent mockedReferenceAgent = mock(Agent.class);
    Point mockedPoint = mock(Point.class);
    when(mockedPoint.getX()).thenReturn(1);
    when(mockedPoint.getY()).thenReturn(0);
//    when(mockedReferenceAgent.getPosition()).thenReturn(mockedPoint);
//    when(mockedReferenceAgent.getBearing()).thenReturn(bearing);
    return mockedReferenceAgent;
  }
}
