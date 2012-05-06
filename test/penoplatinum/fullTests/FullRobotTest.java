package penoplatinum.fullTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import junit.framework.TestCase;
import org.junit.Test;

import penoplatinum.Config;

import penoplatinum.driver.Driver;
import penoplatinum.driver.ManhattanDriver;
import penoplatinum.driver.behaviour.*;

import penoplatinum.reporter.Reporter;
import penoplatinum.reporter.DashboardReporter;

import penoplatinum.gateway.GatewayClient;

import penoplatinum.map.Map;
import penoplatinum.map.MapHashed;

import penoplatinum.model.part.GridModelPart;

import penoplatinum.navigator.Navigator;
import penoplatinum.navigator.GhostNavigator;

import penoplatinum.robot.GhostRobot;

import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.Simulator;

import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulatedEntityFactory;

import penoplatinum.simulator.tiles.Sector;

import penoplatinum.simulator.view.SimulationView;
import penoplatinum.simulator.view.SwingSimulationView;

import penoplatinum.grid.view.SwingGridView;
import penoplatinum.map.MapFactory;
import penoplatinum.map.mazeprotocol.ProtocolMapFactory;


public class FullRobotTest extends TestCase {

  // @Test
  // public void testMerge() throws FileNotFoundException {
  //   Config.load("test.properties");
  // 
  //   // setup simulator
  //   Simulator       simulator = this.createSimulator(makeLongMap());    
  // 
  //   // add four ghosts ...
  //   GhostRobot robot1 = this.createGhostRobot("robot1", simulator, 260, 20, -90);
  //   GhostRobot robot2 = this.createGhostRobot("robot2", simulator, 20, 20, 90);
  //   GhostRobot robot3 = this.createGhostRobot("robot3", simulator, 260, 60, -90);
  //   GhostRobot robot4 = this.createGhostRobot("robot4", simulator, 20, 60, -90);
  // 
  //   // run the simulator for 30000 steps
  //   simulator.run(30000);
  //   
  //   // If we remove delays in the simulator and its view, and if we then add
  //   // some assertions here, these would be real unit tests ;-)
  // }
  
  @Test
  public void testSimulator() throws FileNotFoundException {
    Config.load("test.properties");

    // setup simulator
    Simulator       simulator = this.createSimulator(makeMap());    

    GhostRobot robot1 = this.createGhostRobot("robot1", simulator, 220, 220, -180);
    GhostRobot robot2 = this.createGhostRobot("robot2", simulator, 220, 20, -90);
    GhostRobot robot3 = this.createGhostRobot("robot3", simulator, 20, 220, 0);
    GhostRobot robot4 = this.createGhostRobot("robot4", simulator, 60, 20, 90);

    // run the simulator for 30000 steps
    simulator.run(30000);

    // If we remove delays in the simulator and its view, and if we then add
    // some assertions here, these would be real unit tests ;-)
  }

  private Simulator createSimulator(Map map) {
    Simulator simulator    = new Simulator();
    SimulationView simView = new SwingSimulationView();
    simulator.useMap(map);
    simulator.displayOn(simView);
    return simulator;
  }

  private GhostRobot createGhostRobot(String name, Simulator simulator, int x, int y, int b) {
    // create the robot with all of its parts...
    GhostRobot robot = this.createRobot(name);
    // setup simulator and simulated entity for the robot
    SimulatedEntity entity = this.createSimulatedEntityFor(robot);
    entity.putRobotAt(x, y, b);
    simulator.addSimulatedEntity(entity);

    SwingGridView gridView = new SwingGridView();
    gridView.displayWithoutWindow(GridModelPart.from(robot.getModel()).getFullGrid());
    ((SwingSimulationView) simulator.getView()).addGrid(gridView);

    return robot;
  }
  
  private GhostRobot createRobot(String name) {
    GhostRobot    robot     = new GhostRobot(name);
    Driver        driver    = this.createDriver();
    Navigator     navigator = this.createNavigator();
    GatewayClient client    = this.createGatewayClient();
    Reporter      reporter  = this.createReporter();
    robot.useDriver(driver)
         .useNavigator(navigator)
         .useGatewayClient(client)
         .useReporter(reporter);
    return robot;
  }
  
  private Driver createDriver() {
    return new ManhattanDriver(0.4)
	    .addBehaviour(new FrontProximityDriverBehaviour())
            .addBehaviour(new SideProximityDriverBehaviour())
            .addBehaviour(new BarcodeDriverBehaviour())
            .addBehaviour(new LineDriverBehaviour());
  }
  
  private Navigator createNavigator() {
    return new GhostNavigator();
  }
  
  private GatewayClient createGatewayClient() {
    return new SimulatedGatewayClient();    
  }
  
  private Reporter createReporter() {
    return new DashboardReporter();
  }
  
  private SimulatedEntity createSimulatedEntityFor(GhostRobot robot) {
    return SimulatedEntityFactory.make(robot);
  }
  
  private Map makeMap() throws FileNotFoundException{
    MapFactory mapFac = new ProtocolMapFactory();
    Scanner sc = new Scanner(new File("../maps/wolfraam.txt"));
    return mapFac.getMap(sc);
  }
  private Map makeLongMap(){
    MapHashed out = new MapHashed();
    Sector s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    s1.addWall(3);
    out.put(s1, 1, 1);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    out.put(s1, 2, 1);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    out.put(s1, 3, 1);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    s1.addBarcode(15, 3);
    out.put(s1, 4, 1);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    out.put(s1, 5, 1);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    out.put(s1, 6, 1);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    s1.addWall(1);
    out.put(s1, 7, 1);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    s1.addWall(3);
    out.put(s1, 1, 2);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    out.put(s1, 2, 2);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    out.put(s1, 3, 2);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    s1.addBarcode(14, 3);
    out.put(s1, 4, 2);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    out.put(s1, 5, 2);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    out.put(s1, 6, 2);
    
    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(2);
    s1.addWall(1);
    out.put(s1, 7, 2);
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
