/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

/**
 *
 * @author MHGameWork
 */
public class TransformedGridTest {
   public void testImportOfIdenticalRotatedGridDoesntChangeOriginalGrid() {
    // create two identical grids, but with different sector objects
    Grid grid1 = this.createSquareGridWithFourSectors();
    Grid grid2 = this.createSquareGridWithFourSectors();

    // create a common ReferenceAgent
    Agent reference = this.mockReferenceAgent(Bearing.E);

    // add the ReferenceAgent on both grids
    grid1.add(reference, new Point(1, 0));
    grid2.add(reference, new Point(1, 0));

    // rotate grid2
    grid2.setTransformation(TransformationTRT.fromRotation(Rotation.L90));

    // create a snapshot of grid1
    String original = grid1.toString();

    // import grid2 into grid1 based on the reference point
    grid2.copyTo(grid1);

    // validate that grid1 hasn't changed
    assertEquals(grid1.toString(), original,
            "grid1 has changed after import of identical grid.");
  }

  public void testSetTransformationReversible() {
    // create two identical grids, but with different sector objects
    Grid grid1 = this.createSquareGridWithFourSectors();

    // create a snapshot of grid1
    String original = grid1.toString();

    // create a common ReferenceAgent
    Agent reference = this.mockReferenceAgent(Bearing.E);

    // add the ReferenceAgent on both grids
    grid1.add(reference, new Point(1, 0));

    // rotate grid2
    grid1.setTransformation(TransformationTRT.fromRotation(Rotation.L90));
    grid1.setTransformation(TransformationTRT.fromRotation(Rotation.NONE));


    // validate that grid1 hasn't changed
    assertEquals(grid1.toString(), original,
            "grid1 has changed after setting transformations.");
  }

  public void testRotatedGridsEqual() {
    // create two identical grids, but with different sector objects
    Grid grid1 = this.createSquareGridWithFourSectors();
    Grid grid2 = this.createSquareGridWithFourSectors();

    // create a common ReferenceAgent
    Agent reference = this.mockReferenceAgent(Bearing.E);

    // add the ReferenceAgent on both grids
    grid1.add(reference, new Point(1, 0));
    grid2.add(reference, new Point(1, 0));

    // rotate grid2
    grid1.setTransformation(TransformationTRT.fromRotation(Rotation.L90));
    grid2.setTransformation(TransformationTRT.fromRotation(Rotation.R270));

    // validate that grid1 hasn't changed
    assertEquals(grid1.toString(), grid2.toString(),
            "grid1 is not equal to grid2 when rotated using opposite windings.");
  }

  public void testTransformGrid() {
    // create two identical grids, but with different sector objects
    Grid grid1 = this.createSquareGridWithFourSectors();
    Grid grid2 = this.createTransformedSquareGridWithFourSectors();

    // create a common ReferenceAgent
    Agent reference = this.mockReferenceAgent(Bearing.E);

    // add the ReferenceAgent on both grids
    grid1.add(reference, new Point(1, 0));
    grid2.add(reference, new Point(1, 0));

    // transform grid1
    grid1.setTransformation(this.createGridTransformation());

    // Check if the transformation was successfull
    assertEquals(grid1.toString(), grid2.toString(),
            "Transformed grid1 is not identical to grid2");
  }

  public void testAddSectorPosition() {
    Grid grid = new LinkedGrid();

    LinkedSector s1 = new LinkedSector();
    LinkedSector s2 = new LinkedSector();

    // Add first sector
    grid.add(s1, new Point(2, 2));

    // Should just add the one sector
    assertEquals(s1, grid.get(new Point(2, 2)));

    // Add second sector, should create a path
    grid.add(s2, new Point(3, 3));

    assertEquals(s1, grid.get(new Point(2, 2)));
    assertEquals(s2, grid.get(new Point(3, 3)));
    assertNotNull("s1 has no neighbour!", getSingleNeighbour(s1));
    assertEquals("There is no path from s1 to s2!!", s2, getSingleNeighbour(getSingleNeighbour(s1)));

  }

  public void testAgents() {
    Grid g = new LinkedGrid();

    Agent a1 = mock(Agent.class);
    Agent a2 = mock(Agent.class);
    Agent a3 = mock(Agent.class);

    Agent[] agents = new Agent[]{a1, a2, a3};
    Point[] points = new Point[]{new Point(1, 1), new Point(1, 1), new Point(1, 1)};

    g.add(a1, points[0]);
    g.add(a2, points[1]);
    g.add(a3, points[2]);

    assertNotNull(g.get(points[0]));
    assertNotNull(g.get(points[1]));
    assertNotNull(g.get(points[2]));


    for (Agent a : g.getAgentsIterator()) {
      boolean found = false;
      for (int i = 0; i < agents.length; i++) {
        if (a != agents[i]) {
          continue;
        }
        found = true;
        assertEquals(points[i], g.getAgentPosition(a));
      }
      assertTrue(found);

    }

    //TODO: maybe check if each actor only once in iterator


  }

  private Sector getSingleNeighbour(Sector s) {
    for (Bearing b : Bearing.values()) {
      if (s.hasNeighbour(b)) {
        return s.getNeighbour(b);
      }
    }
    return null;
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
    sector1.withWall(Bearing.N).withWall(Bearing.W);
    sector2.withWall(Bearing.N).withWall(Bearing.E).withWall(Bearing.S);
    sector3.withWall(Bearing.S).withWall(Bearing.W);
    sector4.withWall(Bearing.S).withWall(Bearing.E).withWall(Bearing.N);

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
    sector1.withWall(Bearing.N).withWall(Bearing.W).withWall(Bearing.E);
    sector2.withWall(Bearing.N).withWall(Bearing.W).withWall(Bearing.E);
    sector3.withWall(Bearing.S).withWall(Bearing.W);
    sector4.withWall(Bearing.S).withWall(Bearing.E);

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
