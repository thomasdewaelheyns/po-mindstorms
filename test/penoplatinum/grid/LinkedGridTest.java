package penoplatinum.grid;

import penoplatinum.grid.agent.Agent;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
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

  @Test
  public void testAddSector() {
    Grid grid = new LinkedGrid();
    LinkedSector s1 = new LinkedSector();

    // Add sector
    grid.add(s1, new Point(2, 2));
    assertEquals(s1, grid.getSectorAt(new Point(2, 2)));
    assertEquals(new Point(2, 2), grid.getPositionOf(s1));
  }

  @Test
  public void testAddInvalidSector() {
    Grid grid = new LinkedGrid();
    Sector s = mock(Sector.class);
    boolean failed = true;
    try {
      grid.add(s, new Point(2, 2));
    } catch (IllegalArgumentException e) {
      failed = false;
    }
    assertFalse(failed);
  }

  @Test
  public void testAddSectorCreatePath() {
    Grid grid2Sector = new LinkedGrid();
    LinkedSector s1 = new LinkedSector();
    LinkedSector s2 = new LinkedSector();

    grid2Sector.add(s1, new Point(0, 0)); // This must be the origin!
    grid2Sector.add(s2, new Point(1, 1));

    assertNotNull("s1 has no neighbour!", getSingleNeighbour(s1));
    assertEquals("There is no path from s1 to s2!!", getSingleNeighbour(s1), getSingleNeighbour(s2));
  }

  @Test
  public void testAddAgent() {
    Agent a = mockAgent();

    Grid grid = new LinkedGrid();
    Point pos = new Point(4, 4);
    grid.add(a, pos, Bearing.E);

    assertEquals(Bearing.E, grid.getBearingOf(a));

    assertEquals(a, grid.getAgentAt(pos, a.getClass()));
    assertNull(grid.getAgentAt(pos, LinkedGridTest.class));

    assertNotNull(grid.getSectorAt(pos)); // Sector must be created
    assertEquals(pos, grid.getPositionOf(a));
  }

  @Test
  public void testAddNullAgent() {
    Agent a = null;

    Grid grid = new LinkedGrid();
    Point pos = new Point(4, 4);
    try{
      grid.add(a, pos, Bearing.E);
      fail("Throws exception");
    } catch(IllegalArgumentException e){
    }
  }

  @Test
  public void testMoveTo() {
    Agent a = mockAgent();

    Grid grid = new LinkedGrid();
    Point pos = new Point(4, 4);
    grid.add(a, pos, Bearing.E);
    Point newPos = new Point(1, 1);

    // done setup

    grid.moveTo(a, newPos, Bearing.W);


    assertEquals(Bearing.W, grid.getBearingOf(a));

    assertNull(grid.getAgentAt(pos, a.getClass()));
    assertEquals(a, grid.getAgentAt(newPos, a.getClass()));

    assertNotNull(grid.getSectorAt(pos)); // Old sector must exist
    assertNotNull(grid.getSectorAt(newPos)); // Old sector must exist
    assertEquals(newPos, grid.getPositionOf(a));




  }

  @Test
  public void testAgentsSamePosition() {
    Agent a = mockAgent();
    Agent subA = mockSubAgent();



    Grid grid = new LinkedGrid();
    Point pos = new Point(4, 4);

    grid.add(a, pos, Bearing.E);
    grid.add(subA, pos, Bearing.N);

    assertEquals(pos, grid.getPositionOf(a));
    assertEquals(pos, grid.getPositionOf(subA));
    assertEquals(Bearing.E, grid.getBearingOf(a));
    assertEquals(Bearing.N, grid.getBearingOf(subA));

    assertEquals(a, grid.getAgentAt(pos, a.getClass()));
    assertEquals(subA, grid.getAgentAt(pos, subA.getClass()));
    
  }

  @Test
  public void testGetAgent() {
    Agent a1 = mockAgent("Steve");
    Agent a2 = mockAgent("Mike");

    Grid grid = new LinkedGrid();

    grid.add(a1, new Point(2, 3), Bearing.S);
    grid.add(a2, new Point(-2, -3), Bearing.N);

    assertEquals(a2, grid.getAgent("Mike"));
    assertEquals(a1, grid.getAgent("Steve"));

  }

  @Test
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

  @Test
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
    for (Agent a : agents){
      assertTrue(list.contains(a));
    }
  }

  @Test
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

  @Test
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

  @Test
  public void testGetPositionOf(){
    Grid g = new LinkedGrid();
    Agent a1 = mock(Agent.class);
    Point p1 = new Point(0, 0);
    g.add(a1, p1, Bearing.N);
    
    Point p = g.getPositionOf(a1).translate(1, 1);
    assertNotSame(p1, p);
    assertEquals(new Point(0, 0), g.getPositionOf(a1));
  }
  
    @Test
  public void testGetPositionOfSector(){
    Grid g = new LinkedGrid();
    Sector a1 = new LinkedSector();
    Point p1 = new Point(0, 0);
    g.add(a1, p1);
    
    Point p = g.getPositionOf(a1).translate(1, 1);
    assertNotSame(p1, p);
    assertEquals(new Point(0, 0), g.getPositionOf(a1));
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

  private Agent mockSubAgent() {
    return mock(SubAgent.class);
  }

  interface SubAgent extends Agent {
  }
}
