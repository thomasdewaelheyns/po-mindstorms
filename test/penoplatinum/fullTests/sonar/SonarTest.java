package penoplatinum.fullTests.sonar;

import java.util.Collections;
import penoplatinum.Config;
import penoplatinum.driver.ManhattanDriver;
import penoplatinum.fullTests.hill.MockedGrid;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.grid.Grid;
import penoplatinum.map.Map;
import penoplatinum.map.MapHashed;
import penoplatinum.model.Model;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.navigator.GhostNavigator;
import penoplatinum.navigator.Navigator;
import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulatedEntityFactory;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.view.SimulationView;
import penoplatinum.simulator.view.SwingSimulationView;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SonarTest {

  @Test
  public void testSimulator() {
    Config.load("../../src/java/robot.properties");
    
    Simulator sim = new Simulator();
    SimulationView simView = new SwingSimulationView();
    sim.useMap(makeSquareMap());
    sim.displayOn(simView);

    SonarRobot sonarRobot = new SonarRobot();
    GatewayClient client = new SimulatedGatewayClient();
    sonarRobot.useGatewayClient(client);
    Model spy = spyMockModel(new SonarModel());
    sonarRobot.setModel(spy);
    ManhattanDriver manhattan = new SonarDriver(Sector.SIZE / 100.0);
    Navigator ghostNavigator = new GhostNavigator();
    sonarRobot.useDriver(manhattan).useNavigator(ghostNavigator);
    SimulatedEntity simE = SimulatedEntityFactory.make(sonarRobot);

    simE.putRobotAt(20, 10, -90);
    sim.addSimulatedEntity(simE);
    sim.run(1000 * 6);
    assertEquals(100.03, simE.getPosX(), 0.01);
    assertEquals(100.00, simE.getPosY(), 0.01);
    assertEquals(-548, simE.getDirection(), 1);
  }

  private Model spyMockModel(Model original) {
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
    s1.addBarcode(19, 2);
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
