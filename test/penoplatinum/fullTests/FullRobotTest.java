package penoplatinum.fullTests;

import org.junit.Test;
import penoplatinum.Config;
import penoplatinum.driver.GhostDriver;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.map.Map;
import penoplatinum.map.MapHashed;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.navigator.GhostNavigator;
import penoplatinum.robot.GhostRobot;
import penoplatinum.robot.GhostRobotNewFactory;
import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulatedEntityFactory;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.view.SimulationView;
import penoplatinum.simulator.view.SwingSimulationView;

public class FullRobotTest {
  
  @Test
  public void testSimulator() {
    Config.load("../../src/java/robot.properties");
    
    Simulator sim = new Simulator();
    SimulationView simView = new SwingSimulationView();
    sim.useMap(makeSquareMap());
    sim.displayOn(simView);
    
    
    GhostRobot robot = GhostRobotNewFactory.make();
    GhostDriver driver = new GhostDriver();
    GhostNavigator navigator = new GhostNavigator();
    GatewayClient gateway = new SimulatedGatewayClient();
    robot.useDriver(driver).useNavigator(navigator).useGatewayClient(gateway);
    SimulatedEntity entity = SimulatedEntityFactory.make(robot);
    sim.addSimulatedEntity(entity);
    entity.putRobotAt(20, 20, -90);
    
    MessageModelPart.from(robot.getModel()).getProtocolHandler().handleStart();
    sim.run(1000*6);
    
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
