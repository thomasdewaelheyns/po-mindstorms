package penoplatinum.fullTests.hill;

import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import static org.mockito.Mockito.*;

public class MockedGrid {

  private Sector[][] sectors = new Sector[3][3];
  private GridModelPart mockPart;

  public MockedGrid() {
    mockPart = mock(GridModelPart.class);
    
    int[][] values = new int[][] {{500, 600, 700}, {400,  800, 600}, {2000, 900, 1000}};
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        sectors[i][j] = mock(Sector.class);
        when(sectors[i][j].getValue()).thenReturn(values[i][j]);
        if (i > 0) {
          when(sectors[i - 1][j].getNeighbour(Bearing.S)).thenReturn(sectors[i][j]);
          when(sectors[i][j].getNeighbour(Bearing.N)).thenReturn(sectors[i - 1][j]);
        }
        if (j > 0) {
          when(sectors[i][j - 1].getNeighbour(Bearing.E)).thenReturn(sectors[i][j]);
          when(sectors[i][j].getNeighbour(Bearing.W)).thenReturn(sectors[i][j - 1]);
        }
      }
    }
    when(sectors[0][0].givesAccessTo(Bearing.E)).thenReturn(Boolean.TRUE);
    when(sectors[0][1].givesAccessTo(Bearing.W)).thenReturn(Boolean.TRUE);
    
    when(sectors[0][0].givesAccessTo(Bearing.S)).thenReturn(Boolean.TRUE);
    when(sectors[1][0].givesAccessTo(Bearing.N)).thenReturn(Boolean.TRUE);
    
    when(sectors[1][0].givesAccessTo(Bearing.S)).thenReturn(Boolean.TRUE);
    when(sectors[2][0].givesAccessTo(Bearing.N)).thenReturn(Boolean.TRUE);
    
    when(sectors[0][1].givesAccessTo(Bearing.E)).thenReturn(Boolean.TRUE);
    when(sectors[0][2].givesAccessTo(Bearing.W)).thenReturn(Boolean.TRUE);
    
    when(sectors[0][1].givesAccessTo(Bearing.S)).thenReturn(Boolean.TRUE);
    when(sectors[1][1].givesAccessTo(Bearing.N)).thenReturn(Boolean.TRUE);
    
    when(sectors[0][2].givesAccessTo(Bearing.S)).thenReturn(Boolean.TRUE);
    when(sectors[1][2].givesAccessTo(Bearing.N)).thenReturn(Boolean.TRUE);
    
    when(sectors[1][1].givesAccessTo(Bearing.S)).thenReturn(Boolean.TRUE);
    when(sectors[2][1].givesAccessTo(Bearing.N)).thenReturn(Boolean.TRUE);
    
    when(sectors[2][1].givesAccessTo(Bearing.E)).thenReturn(Boolean.TRUE);
    when(sectors[2][2].givesAccessTo(Bearing.W)).thenReturn(Boolean.TRUE);
    
    Agent agent = new MockedAgent(this, "name-abcde");
    when(mockPart.getMyAgent()).thenReturn(agent);
  }

  public void setPosition(int x, int y) {
    when(mockPart.getMySector()).thenReturn(sectors[y - 1][x - 1]);
  }
  
  public GridModelPart getGridModelPart(){
    return mockPart;
  }
}
