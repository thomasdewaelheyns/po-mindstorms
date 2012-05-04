/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.protocol;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import penoplatinum.grid.Agent;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.model.GhostModel;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

/**
 *
 * @author Thomas
 */
public class GhostExternalEventHandlerTest extends TestCase{
  
  public GhostExternalEventHandlerTest(String name) {
    super(name);
  }
  
  public void testUseModel(){
    Model model = mockGhostModel();
    GhostExternalEventHandler handler = new GhostExternalEventHandler();
    try{ handler.useModel(null); fail();}catch(Exception e){/*Expected*/};
    handler.useModel(model);
    assertEquals(handler.getModel(), model);
  }
  
  public void testHandleActivation(){
    Model model = mockGhostModel();
    GridModelPart gridPart = mockGridModelPart();
    when(model.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    
    GhostExternalEventHandler handler = new GhostExternalEventHandler();
    handler.useModel(model);
    handler.handleActivation();
    verify(GridModelPart.from(model)).getMyAgent();
  }
  
  public void testHandleSectorInfo(){
    Point position = new Point(2,2);
    Model model = mockGhostModel();
    Grid grid = mockedGrid();
    GridModelPart gridPart = mockGridModelPart();
    Sector sec = mockedSector();
    when(model.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    when(gridPart.getMyAgent()).thenReturn(mockAgent());
    when(gridPart.getGridOf("Terminator")).thenReturn(grid);
    when(grid.getSectorAt(new Point(2,2))).thenReturn(sec);
    
    GhostExternalEventHandler handler = new GhostExternalEventHandler();
    handler.useModel(model);
    handler.handleSectorInfo("Terminator", position, false, false, true, true, true, false, false, false);
    
    verify(sec).setWall(Bearing.E);
    verify(sec).setNoWall(Bearing.S);
    verifyNoMoreInteractions(sec);
  }
  
  public void testHandleNewAgent(){
    Model model = mockGhostModel();
    GridModelPart gridPart = mockGridModelPart();
    when(model.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    
    GhostExternalEventHandler handler = new GhostExternalEventHandler();
    handler.useModel(model);
    handler.handleNewAgent("Terminator");
    verify(GridModelPart.from(model)).getGridOf("Terminator");
  }
  
  public void testHandleAgentInfo(){
    Point position = new Point(2,2);
    Model model = mockGhostModel();
    Grid grid = mockedGrid();
    GridModelPart gridPart = mockGridModelPart();
    Sector sec = mockedSector();
    Agent agent = mockAgent();
    when(model.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    when(gridPart.getMyAgent()).thenReturn(mockAgent());
    when(gridPart.getGridOf("Terminator")).thenReturn(grid);
    when(grid.getSectorAt(new Point(2,2))).thenReturn(sec);
    when(grid.getAgent("Terminator")).thenReturn(agent);
    
    GhostExternalEventHandler handler = new GhostExternalEventHandler();
    handler.useModel(model);
    handler.handleAgentInfo("Terminator", position, 0, Bearing.E);
    verify(grid).moveTo(agent, position, Bearing.E);
    
  }
  
  public void testHandleTargetInfo(){
    Model model = mockGhostModel();
    GridModelPart gridPart = mockGridModelPart();
    when(model.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    when(gridPart.getMyAgent()).thenReturn(mockAgent());
    
    GhostExternalEventHandler handler = new GhostExternalEventHandler();
    handler.useModel(model);
    Point point = new Point(2,2);
    handler.handleTargetInfo("TestRobot1", point);
    verify(GridModelPart.from(model)).setPacman(point);
  }
  
  private Model mockGhostModel(){
    GhostModel model = mock(GhostModel.class);
    return model;
  }
  
  private GridModelPart mockGridModelPart(){
    GridModelPart gridPart = mock(GridModelPart.class);
    when(gridPart.getMyAgent()).thenReturn(mockAgent());
    return gridPart;
  }
  
  private Agent mockAgent(){
    Agent agent = mock(Agent.class);
    return agent;
  }
  
  private Grid mockedGrid(){
    Grid grid = mock(Grid.class);
    return grid;
  }
  
  private Sector mockedSector(){
    Sector sec = mock(Sector.class);
    return sec;
  }
}
