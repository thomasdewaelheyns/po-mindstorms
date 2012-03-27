package penoplatinum.grid;

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.util.Point;
import penoplatinum.grid.Grid;


public class testGrid() {

  @Test
  public void testImportOfIdenticalRotatedGridDoesntChangeOriginalGrid() {
    // create two identical grids, but with different sector objects
    Grid grid1 = this.createSquareGridWithFourSectors();
    Grid grid2 = this.createSquareGridWithFourSectors();

    // create a common ReferenceAgent
    Agent reference = this.mockReferenceAgent(Bearing.E);

    // add the ReferenceAgent on both grids
    grid1.put(reference, grid1.getSector(1, 0));
    grid2.put(reference, grid2.getSector(1, 0));

    // rotate grid2
    grid2.rotate(90);

    // create a snapshot of grid1
    String original = grid1.toString();

    // import grid2 into grid1 based on the reference point
    grid1.import(grid2, reference);

    // validate that grid1 hasn't changed
    assertEquals( grid1.toString(), original, 
                 "grid1 has changed after import of identical grid." );
  }

  // utility methods to setup basic components

  private Grid createSquareGridWitFourSectors() {
    Grid grid = new Grid();
    Sector sector1 = new Sector().setCoordinates(0,0);
    Sector sector2 = new Sector();
    Sector sector3 = new Sector();
    Sector sector4 = new Sector();

    sector1.addNeighbour(sector2, Bearing.E);
    sector1.addNeighbour(sector3, Bearing.S);
    sector2.addNeighbour(sector4, Bearing.S);

    /* result looks like this:
    *    +--+--+
    *    |     |
    *    +  +--+
    *    |     +
    *    +--+--+
    */

    grid.add(sector1.withWall(Bearing.N)
                    .withWall(Bearing.W));
    grid.add(sector2.withWall(Bearing.N)
                    .withWall(Bearing.E)
                    .withWall(Bearing.S));
    grid.add(sector3.withWall(Bearing.S)
                    .withWall(Bearing.W));
    grid.add(sector4.withWall(Bearing.S)
                    .withWall(Bearing.E)
                    .withWall(Bearing.N)); // this one is already there ;-)
    return grid;
  }

  // create a mock that answers what a perfectly working ReferenceAgent
  // should answer when used to import a Grid in a Grid
  private ReferenceAgent mockReferenceAgent(int bearing) {
    ReferenceAgent mockedReferenceAgent = mock(ReferenceAgent.class);
    Point mockedPoint = mock(Point.class);
    when(mockedPoint.getLeft()).thenReturn(0);
    when(mockedPoint.getTop()).thenReturn(0);
    when(mockedReferenceAgent.getPosition()).thenReturn(mockecPoint);
    when(mockedReferenceAgent.getBearing()).thenReturn(bearing);
    return mockedReferenceAgent;
  }

}
