/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import penoplatinum.simulator.entities.SimulationRobotAPI;
import penoplatinum.simulator.entities.SimulatedEntity;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.grid.SwingGridView;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.simulator.*;
import penoplatinum.simulator.view.SwingSimulationView;

/**
 *
 * @author Thomas
 */
public class AggregatedGuiTest {

  Simulator sim;
  Random r = new Random(456);
  ArrayList<SwingGridView> grids = new ArrayList<SwingGridView>();

  @Before
  public void setup() {
    sim = new Simulator();
    for (int banaan = 0; banaan < 4; banaan++) {
      grids.add(new SwingGridView());
      grids.get(banaan).disableWindow();
    }
  }

  @After
  public void after() {
    SwingSimulationView view = new SwingSimulationView();
    sim.displayOn(view);
    for (int i = 0; i < 4; i++) {
      view.addGrid(grids.get(i));
    }
    sim.run();
  }

  @Test
  public void testGhostRobotMultiple() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMazeProtocol2());

    GhostRobot robot = new GhostRobot("Michiel", grids.get(0));
  
    GhostNavigator ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);


    SimulatedEntity ent = new SimulatedEntity(robot);
    
      robot.useNavigator(new GhostNavigator());
    robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(ent));
    robot.useGatewayClient(new SimulatedGatewayClient());
    
    ent.setPostition(20, 20, 0);
    sim.addSimulatedEntity(ent);


    robot = new GhostRobot("Christophe", grids.get(1));
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);

    ent = new SimulatedEntity(robot);
    robot.useNavigator(new GhostNavigator());
    robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(ent));
    robot.useGatewayClient(new SimulatedGatewayClient());
    ent.setPostition(60, 60, 0);
    sim.addSimulatedEntity(ent);


    robot = new GhostRobot("Ruben", grids.get(2));
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);

    ent = new SimulatedEntity(robot);
    robot.useNavigator(new GhostNavigator());
    robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(ent));
    robot.useGatewayClient(new SimulatedGatewayClient());
    ent.setPostition(100, 100, 0);
    sim.addSimulatedEntity(ent);

    robot = new GhostRobot("Thomas", grids.get(3));
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);

    ent = new SimulatedEntity(robot);
    robot.useNavigator(new GhostNavigator());
    robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(ent));
    robot.useGatewayClient(new SimulatedGatewayClient());
    ent.setPostition(100, 140, 0);
    sim.addSimulatedEntity(ent);

  }
}
