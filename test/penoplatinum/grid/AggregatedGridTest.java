/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

import junit.framework.TestCase;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

/**
 *
 * @author MHGameWork
 */
public class AggregatedGridTest extends TestCase {

  private Grid mainGrid;
  private Agent mainAgent;
  private Grid grid1;
  private Agent agent1;
  private Grid grid2;
  private Agent agent2;
  private Agent agent3;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    mainAgent = mockAgent("main");
    agent1 = mockAgent("ghost1");
    agent2 = mockAgent("ghost2");
    agent3 = mockSubAgent("ghost3");

    mainGrid = new LinkedGrid();
    mainGrid.add(new LinkedSector().setWall(Bearing.N).setWall(Bearing.E).setNoWall(Bearing.S), new Point(0, 0));
    mainGrid.add(mainAgent, new Point(1, 0), Bearing.N);

    grid1 = new LinkedGrid();
    grid1.add(new LinkedSector().setWall(Bearing.N).setWall(Bearing.E).setNoWall(Bearing.S), new Point(0, 0));
    grid1.add(agent1, new Point(0, 1), Bearing.N);

    grid2 = new LinkedGrid();
    grid2.add(agent2, new Point(0, 1), Bearing.E);
    grid2.add(agent3, new Point(1, 0), Bearing.W);


  }

  public void testMainGrid() {
    Grid g = mockGrid();
    AggregatedGrid agg = new AggregatedGrid(g);
    assertEquals(1, agg.getActiveGrids().size());
    assertEquals(g, ((TransformedGrid) agg.getActiveGrids().get(0)).getGrid());
    assertEquals(TransformationTRT.Identity,
            ((TransformedGrid) agg.getActiveGrids().get(0)).getTransformation());

    assertEquals(g.toString(), agg.toString());

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

  public void testGetSectorAtEmptySpot() {
    AggregatedGrid agg = new AggregatedGrid(mainGrid);
    agg.activateSubGrid(grid1, TransformationTRT.Identity);

    Sector s = agg.getSectorAt(new Point(10, 10));
    assertNotNull(s);

  }

  public void testGetPositionOfSector() {
    // Durr
  }

  public void testGetSectors() {
    AggregatedGrid agg = new AggregatedGrid(mainGrid);
    agg.activateSubGrid(grid1, TransformationTRT.Identity);
    
    int count = 0;
    for (Sector s : agg.getSectors())
      count++;
    
    assertEquals(4, count);
  }

  public void testGetSectorOf() {

    AggregatedGrid grid = new AggregatedGrid(mainGrid);

    assertEquals(new Point(1, 0), grid.getPositionOf(mainAgent));

    assertNull(null, grid.getPositionOf(agent1));
    assertNull(null, grid.getPositionOf(agent2));
    assertNull(null, grid.getPositionOf(agent3));

    grid.activateSubGrid(grid1, TransformationTRT.Identity);

    assertEquals(new Point(1, 0), grid.getPositionOf(mainAgent));
    assertEquals(new Point(0, 1), grid.getPositionOf(agent1));

    assertNull(null, grid.getPositionOf(agent2));
    assertNull(null, grid.getPositionOf(agent3));

    grid.activateSubGrid(grid2, TransformationTRT.Identity);

    assertEquals(new Point(1, 0), grid.getPositionOf(mainAgent));
    assertEquals(new Point(0, 1), grid.getPositionOf(agent1));
    assertEquals(new Point(0, 1), grid.getPositionOf(agent2));
    assertEquals(new Point(1, 0), grid.getPositionOf(agent3));




  }

  public void testGetBearingOf() {
    AggregatedGrid grid = new AggregatedGrid(mainGrid);

    assertEquals(Bearing.N, grid.getBearingOf(mainAgent));

    grid.activateSubGrid(grid1, TransformationTRT.Identity);

    assertEquals(Bearing.N, grid.getBearingOf(mainAgent));
    assertEquals(Bearing.N, grid.getBearingOf(agent1));

    assertEquals(new Point(1, 0), grid.getPositionOf(mainAgent));
    assertEquals(new Point(0, 1), grid.getPositionOf(agent1));

    grid.activateSubGrid(grid2, TransformationTRT.Identity);

    assertEquals(Bearing.N, grid.getBearingOf(mainAgent));
    assertEquals(Bearing.N, grid.getBearingOf(agent1));
    assertEquals(Bearing.E, grid.getBearingOf(agent2));
    assertEquals(Bearing.W, grid.getBearingOf(agent3));


  }

  public void testGetAgent() {

    AggregatedGrid grid = new AggregatedGrid(mainGrid);

    assertEquals(mainAgent, grid.getAgent("main"));
    assertEquals(null, grid.getAgent("ghost1"));
    assertEquals(null, grid.getAgent("ghost2"));
    assertEquals(null, grid.getAgent("ghost3"));

    grid.activateSubGrid(grid1, TransformationTRT.Identity);

    assertEquals(mainAgent, grid.getAgent("main"));
    assertEquals(agent1, grid.getAgent("ghost1"));
    assertEquals(null, grid.getAgent("ghost2"));
    assertEquals(null, grid.getAgent("ghost3"));

    grid.activateSubGrid(grid2, TransformationTRT.Identity);

    assertEquals(mainAgent, grid.getAgent("main"));
    assertEquals(agent1, grid.getAgent("ghost1"));
    assertEquals(agent2, grid.getAgent("ghost2"));
    assertEquals(agent3, grid.getAgent("ghost3"));

  }

  public void testBounds() {
    AggregatedGrid grid = new AggregatedGrid(mainGrid);
    assertEquals(0, grid.getMinLeft());
    assertEquals(1, grid.getMaxLeft());
    assertEquals(0, grid.getMinTop());
    assertEquals(0, grid.getMaxTop());
    assertEquals(2, grid.getWidth());
    assertEquals(1, grid.getHeight());

    grid.activateSubGrid(grid1, TransformationTRT.Identity);

    assertEquals(0, grid.getMinLeft());
    assertEquals(1, grid.getMaxLeft());
    assertEquals(0, grid.getMinTop());
    assertEquals(1, grid.getMaxTop());
    assertEquals(2, grid.getWidth());
    assertEquals(2, grid.getHeight());

    grid.activateSubGrid(grid2, TransformationTRT.Identity);

    assertEquals(0, grid.getMinLeft());
    assertEquals(1, grid.getMaxLeft());
    assertEquals(0, grid.getMinTop());
    assertEquals(1, grid.getMaxTop());
    assertEquals(2, grid.getWidth());
    assertEquals(2, grid.getHeight());
  }

  public void testSize() {
  }

  public void testGetAgentAt() {

    Class clsAgent = mainAgent.getClass();
    Class clsSubAgent = agent3.getClass();

    AggregatedGrid grid = new AggregatedGrid(mainGrid);

    assertEquals(mainAgent, grid.getAgentAt(new Point(1, 0), clsAgent));
    assertNull(grid.getAgentAt(new Point(0, 1), clsAgent));
    assertNull(grid.getAgentAt(new Point(1, 0), clsSubAgent));

    assertNull(grid.getAgentAt(new Point(1, 0), clsSubAgent));
    assertNull(grid.getAgentAt(new Point(0, 1), clsSubAgent));

    grid.activateSubGrid(grid1, TransformationTRT.Identity);

    assertEquals(mainAgent, grid.getAgentAt(new Point(1, 0), clsAgent));
    assertEquals(agent1, grid.getAgentAt(new Point(0, 1), clsAgent));
    assertNull(grid.getAgentAt(new Point(1, 0), clsSubAgent));

    assertNull(grid.getAgentAt(new Point(1, 0), clsSubAgent));
    assertNull(grid.getAgentAt(new Point(0, 1), clsSubAgent));

    grid.activateSubGrid(grid2, TransformationTRT.Identity);

    assertEquals(mainAgent, grid.getAgentAt(new Point(1, 0), clsAgent));
    assertEquals(agent1, grid.getAgentAt(new Point(0, 1), clsAgent));
    assertEquals(agent3, grid.getAgentAt(new Point(1, 0), clsSubAgent));

    assertNull(grid.getAgentAt(new Point(0, 1), clsSubAgent));
  }

  private Grid mockGrid() {
    return mock(Grid.class);
  }

  private TransformationTRT mockTransformationTRT() {
    return new TransformationTRT().setTransformation(-1, -1, Rotation.L90, 1, 2);
  }

  private Agent mockAgent(String string) {
    Agent ret = mock(Agent.class);
    when(ret.getName()).thenReturn(string);
    return ret;
  }

  private Agent mockSubAgent(String string) {
    Agent ret = mock(SubAgent.class);
    when(ret.getName()).thenReturn(string);
    return ret;
  }

  interface SubAgent extends Agent {
  }
}
