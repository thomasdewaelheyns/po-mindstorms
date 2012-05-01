package penoplatinum.fullTests;

import java.util.Collections;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import penoplatinum.driver.ManhattanDriver;
import penoplatinum.grid.Grid;
import penoplatinum.map.Map;
import penoplatinum.map.MapHashed;
import penoplatinum.model.Model;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.navigator.GhostNavigator;
import penoplatinum.navigator.Navigator;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulatedEntityFactory;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.view.SimulationView;
import penoplatinum.simulator.view.SwingSimulationView;

public class HillTest {

  @Test
  public void testSimulator() {
    Simulator sim = new Simulator();
    sim.useMap(makeSquareMap());
    
    HillRobot hill = new HillRobot();
    hill.setModel(spyMockModel(hill.getModel()));
    ManhattanDriver manhattan = new LineDriver(Sector.SIZE/100.0);
    Navigator ghostNavigator = new GhostNavigator();
    hill.useDriver(manhattan).useNavigator(ghostNavigator);
    SimulatedEntity simE = SimulatedEntityFactory.make(hill);
    
    simE.putRobotAt(20, 20, -90);
    sim.addSimulatedEntity(simE);
    sim.run(1000*6);
    assertEquals(100.03, simE.getPosX(), 0.01);
    assertEquals(100.00, simE.getPosY(), 0.01);
    assertEquals(-548, simE.getDirection(), 1);
  }
  
  private Model spyMockModel(Model original){
    Model out = spy(original);
    MockedGrid g = new MockedGrid();
    Grid mockGrid = mock(Grid.class);
    when(mockGrid.getSectors()).thenReturn(Collections.EMPTY_LIST);
    when(g.getGridModelPart().getMyGrid()).thenReturn(mockGrid);
    doReturn(g.getGridModelPart()).when(out).getPart(ModelPartRegistry.GRID_MODEL_PART);
    return out;
  }
  

  private Map makeSquareMap() {
    MapHashed out = new MapHashed();
    Sector s1 = new Sector();
    s1.addWall(0);
    s1.addWall(3);
    out.put(s1, 1, 1);

    s1 = new Sector();
    s1.addWall(0);
    out.put(s1, 2, 1);

    s1 = new Sector();
    s1.addWall(1);
    s1.addWall(3);
    out.put(s1, 1, 2);

    s1 = new Sector();
    s1.addWall(1);
    s1.addWall(3);
    out.put(s1, 2, 2);
    
    s1 = new Sector();
    s1.addWall(1);
    s1.addWall(2);
    s1.addWall(3);
    out.put(s1, 1, 3);

    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(1);
    out.put(s1, 3, 1);

    s1 = new Sector();
    s1.addWall(1);
    s1.addWall(2);
    s1.addWall(3);
    out.put(s1, 3, 2);

    s1 = new Sector();
    s1.addWall(2);
    s1.addWall(3);
    out.put(s1, 2, 3);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(1);
    s1.addWall(2);
    out.put(s1, 3, 3);

    return out;
  }
}
