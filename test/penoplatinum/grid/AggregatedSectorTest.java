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

/**
 *
 * @author MHGameWork
 */
public class AggregatedSectorTest extends TestCase {

  public void testAggregateWalls() {

    Sector s;

    // Simple 4 different wall merge
    s = createAggregatedSector(new Sector[]{
              new LinkedSector().setWall(Bearing.E),
              new LinkedSector().setWall(Bearing.N),
              new LinkedSector().setWall(Bearing.W),
              new LinkedSector().setWall(Bearing.S)});

    assertTrue(s.hasWall(Bearing.N));
    assertTrue(s.hasWall(Bearing.E));
    assertTrue(s.hasWall(Bearing.S));
    assertTrue(s.hasWall(Bearing.W));


    // Simple unknown test + double wall + double nowall
    s = createAggregatedSector(new Sector[]{
              new LinkedSector().setWall(Bearing.E),
              new LinkedSector().setWall(Bearing.E),
              new LinkedSector().setNoWall(Bearing.S),
              new LinkedSector().setNoWall(Bearing.S)});

    assertTrue(!s.knowsWall(Bearing.N));
    assertTrue(s.hasWall(Bearing.E));
    assertTrue(!s.hasWall(Bearing.S));

    // Simple nowall + wall = unkown AND doublenowall + double wall = unkown

    s = createAggregatedSector(new Sector[]{
              new LinkedSector().setWall(Bearing.E).setNoWall(Bearing.W),
              new LinkedSector().setNoWall(Bearing.E).setNoWall(Bearing.W),
              new LinkedSector().setWall(Bearing.W),
              new LinkedSector().setWall(Bearing.W)});

    assertTrue(!s.knowsWall(Bearing.E));
    assertTrue(!s.knowsWall(Bearing.W));
    assertTrue(!s.knowsWall(Bearing.N));
    assertTrue(!s.knowsWall(Bearing.S));
  }

  public void testAggregatedEmptySector() {
    ArrayList<Grid> grids = new ArrayList<Grid>();
    grids.add(mockEmptyGrid());
    grids.add(mockEmptyGrid());

    AggregatedGrid agg = mockAggregatedGrid(grids);

    Sector s = new AggregatedSector(agg, new Point(10,10));
    for (Bearing b : Bearing.NESW)
      assertFalse(s.knowsWall(b));

  }

  private Grid mockEmptyGrid() {
    Grid g = mock(Grid.class);
    when(g.getSectorAt(any(Point.class))).thenReturn(null);
    return g;
  }

  private AggregatedGrid mockAggregatedGrid(List<Grid> activeGrids) {
    AggregatedGrid grid = mock(AggregatedGrid.class);
    when(grid.getActiveGrids()).thenReturn(activeGrids);
    return grid;
  }

  private Sector createAggregatedSector(Sector[] sectors) {
    List<Grid> grids = new ArrayList<Grid>();

    for (Sector s : sectors) {
      Grid g = mock(Grid.class);
      when(g.getSectorAt(new Point(0, 0))).thenReturn(s);
      grids.add(g);
    }
    AggregatedGrid agg = mockAggregatedGrid(grids);
    return new AggregatedSector(agg, new Point(0, 0));
  }
}
