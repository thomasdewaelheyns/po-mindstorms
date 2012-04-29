package penoplatinum.grid;

import java.util.ArrayList;
import java.util.List;
import junit.framework.*;
import static org.mockito.Mockito.*;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class LinkedGridTest extends TestCase {

  public LinkedGridTest(String name) {
    super(name);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testAddSector() {
    Grid grid = new LinkedGrid();

    LinkedSector s1 = new LinkedSector();

    // Add sector
    grid.add(s1, new Point(2, 2));

    assertEquals(s1, grid.getSectorAt(new Point(2, 2)));
    assertEquals(new Point(2, 2), grid.getPositionOf(s1));

  }

  public void testAddSectorCreatePath() {

    Grid grid2Sector = new LinkedGrid();

    LinkedSector s1 = new LinkedSector();
    LinkedSector s2 = new LinkedSector();

    grid2Sector.add(s1, new Point(0, 0)); // This must be the origin!
    grid2Sector.add(s2, new Point(1, 1));

    assertNotNull("s1 has no neighbour!", getSingleNeighbour(s1));
    assertEquals("There is no path from s1 to s2!!", getSingleNeighbour(s1), getSingleNeighbour(s2));

  }

  public void testAddAgent() {
    Agent a = mockAgent();

    Grid grid = new LinkedGrid();
    Point pos = new Point(4, 4);
    grid.add(a, pos, Bearing.E);

    assertEquals(Bearing.E, grid.getBearingOf(a));

    assertEquals(a, grid.getAgentAt(pos));

    assertNotNull(grid.getSectorAt(pos)); // Sector must be created
    assertEquals(grid.getSectorAt(pos), grid.getSectorOf(a));

  }

  public void testMoveTo() {
    Agent a = mockAgent();

    Grid grid = new LinkedGrid();
    Point pos = new Point(4, 4);
    grid.add(a, pos, Bearing.E);
    Point newPos = new Point(1, 1);

    // done setup

    grid.moveTo(a, newPos, Bearing.W);


    assertEquals(Bearing.W, grid.getBearingOf(a));

    assertNull(grid.getAgentAt(pos));
    assertEquals(a, grid.getAgentAt(newPos));

    assertNotNull(grid.getSectorAt(pos)); // Old sector must exist
    assertNotNull(grid.getSectorAt(newPos)); // Old sector must exist
    assertEquals(grid.getSectorAt(newPos), grid.getSectorOf(a));




  }

  public void testGetAgent() {
    Agent a1 = mockAgent("Steve");
    Agent a2 = mockAgent("Mike");

    Grid grid = new LinkedGrid();

    grid.add(a1, new Point(2, 3), Bearing.S);
    grid.add(a2, new Point(-2, -3), Bearing.N);

    assertEquals(a2, grid.getAgent("Mike"));
    assertEquals(a1, grid.getAgent("Steve"));

  }

  public void testGetSectors() {
    Grid g = new LinkedGrid();

    Sector s1 = new LinkedSector();
    Sector s2 = new LinkedSector();
    Sector s3 = new LinkedSector();

    Sector[] sectors = new Sector[]{s1, s2, s3};
    Point[] points = new Point[]{new Point(0, 0), new Point(0, 1), new Point(0, 2)};
    // Note that the sectors must be next to eachother and starting at (0,0) 
    //     otherwise extra sectors are created

    g.add(s1, points[0]);
    g.add(s2, points[1]);
    g.add(s3, points[2]);


    List<Sector> list = new ArrayList<Sector>();


    for (Sector a : g.getSectors())
      list.add(a);


    assertEquals(sectors.length, list.size());


    for (Sector a : sectors)
      assertTrue(list.contains(a));
  }

  public void testGetAgents() {
    Grid g = new LinkedGrid();

    Agent a1 = mock(Agent.class);
    Agent a2 = mock(Agent.class);
    Agent a3 = mock(Agent.class);

    Agent[] agents = new Agent[]{a1, a2, a3};
    Point[] points = new Point[]{new Point(1, 1), new Point(1, 2), new Point(1, 3)};

    g.add(a1, points[0], Bearing.N);
    g.add(a2, points[1], Bearing.E);
    g.add(a3, points[2], Bearing.S);


    List<Agent> list = new ArrayList<Agent>();


    for (Agent a : g.getAgents()) {
      list.add(a);
    }

    assertEquals(agents.length, list.size());


    for (Agent a : agents)
      assertTrue(list.contains(a));


  }

  public void testMinMaxBounds() {
    Grid grid = new LinkedGrid();

    // Note that (0,0) is always in the grid

    grid.add(new LinkedSector(), new Point(-3, 0));
    assertEquals(-3, grid.getMinLeft());
    assertEquals(0, grid.getMinTop());
    assertEquals(0, grid.getMaxLeft());
    assertEquals(0, grid.getMaxTop());

    grid.add(new LinkedSector(), new Point(2, 1));
    assertEquals(-3, grid.getMinLeft());
    assertEquals(0, grid.getMinTop());
    assertEquals(2, grid.getMaxLeft());
    assertEquals(1, grid.getMaxTop());

    grid.add(new LinkedSector(), new Point(-4, -5));
    assertEquals(-4, grid.getMinLeft());
    assertEquals(-5, grid.getMinTop());
    assertEquals(2, grid.getMaxLeft());
    assertEquals(1, grid.getMaxTop());

    grid.add(new LinkedSector(), new Point(5, 4));
    assertEquals(-4, grid.getMinLeft());
    assertEquals(-5, grid.getMinTop());
    assertEquals(5, grid.getMaxLeft());
    assertEquals(4, grid.getMaxTop());


  }

  public void testGetSize() {
    Grid grid = new LinkedGrid();

    Sector s1 = new LinkedSector();
    Sector s2 = new LinkedSector();
    Sector s3 = new LinkedSector();

    Sector[] sectors = new Sector[]{s1, s2, s3};
    Point[] points = new Point[]{new Point(0, 0), new Point(0, 1), new Point(0, 2)};
    for (int i = 0; i < sectors.length; i++)
      grid.add(sectors[i], points[i]);

    assertEquals(3, grid.getSize());

  }

  //
  //
  //
  //
  private Sector getSingleNeighbour(Sector s) {
    for (Bearing b : Bearing.NESW) {
      if (s.hasNeighbour(b)) {
        return s.getNeighbour(b);
      }
    }
    return null;
  }

  private Agent mockAgent() {
    return mockAgent("Johnny");
  }

  private Agent mockAgent(String name) {
    Agent ag = mock(Agent.class);
    when(ag.getName()).thenReturn(name);
    return ag;
  }

  //
  //
  //
  //
  //
  //
  //
  //
  //
  // Transformation tests
  //
  //
  //
  //
  //
  //
  //
  //
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
