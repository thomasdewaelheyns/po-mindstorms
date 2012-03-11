package penoplatinum;

import java.io.FileNotFoundException;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.pacman.LeftFollowingGhostNavigator;
import penoplatinum.grid.SwingGridView;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulationRobotAgent;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.SimulatorTest;
import penoplatinum.simulator.mini.Bearing;
import penoplatinum.simulator.mini.Navigator;
import penoplatinum.simulator.view.SwingSimulationView;

/**
 *
 * @author MHGameWork
 */
public class GhostRobotTest {

  Simulator sim;
  Random r = new Random(456);

  @Before
  public void setup() {
    sim = new Simulator();
  }

  @After
  public void after() {

    sim.displayOn(new SwingSimulationView());
    sim.run();
  }

  @Test
  public void testGhostRobotLeftFollowing() throws FileNotFoundException {

    sim.useMap(SimulatorTest.createSectorMap());

    putGhostRobot(20, 20, 0, new LeftFollowingGhostNavigator(), "Michiel");

  }

  @Test
  public void testGhostRobotGhostNavigator() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMap2());
    SwingGridView view = new SwingGridView();

    Random r = new Random();
    final String name = r.nextInt() + "";


    GhostRobot robot = new GhostRobot(name, view);
    final GhostNavigator ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);
    ghostNavigator.setModel(robot.getGhostModel());

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(20 + 1 * 40, 20 + 1 * 40, 0);
    sim.addSimulatedEntity(ent);

  }

  @Test
  public void testGhostRobotMultiple() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMap2());

    GhostRobot robot = new GhostRobot("Michiel", new SwingGridView());
    GhostNavigator ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);


    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(20, 20, 0);
    sim.addSimulatedEntity(ent);


    robot = new GhostRobot("Christophe", new SwingGridView());
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);

    ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(60, 60, 0);
    sim.addSimulatedEntity(ent);


    robot = new GhostRobot("Ruben", new SwingGridView());
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);

    ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(100, 100, 0);
    sim.addSimulatedEntity(ent);

    robot = new GhostRobot("Thomas", new SwingGridView());
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);

    ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(100, 140, 0);
    sim.addSimulatedEntity(ent);

  }

  @Test
  public void testGhostRobotMazeProtocol() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMazeProtocol());
    SwingGridView view = new SwingGridView();

    putGhostRobot(20 + 1 * 40, 20 + 1 * 40, 0);

  }

  @Test
  public void testGhostRobotBarcodeMergeSimple() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMap());



    String name = r.nextInt() + "";

    SimulatedEntity ent1 = putGhostRobot(20 + 0 * 40, 20 + 2 * 40, 0);
    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);
//    putGhostRobot(20 + 5 * 40, 20 + 4 * 40, 0);

    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);

  }

  @Test
  public void testGhostRobotMazeProtocol3() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMazeProtocol());



    String name = r.nextInt() + "";

    SimulatedEntity ent1 = putGhostRobot(20 + 0 * 40, 20 + 2 * 40, 0);
    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);
    putGhostRobot(20 + 5 * 40, 20 + 4 * 40, 0);

//    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
//    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);

  }

  @Test
  public void testGhostRobot3() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMap2());

    GhostRobot robot = new GhostRobot("Michiel", new SwingGridView());
    GhostNavigator ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);


    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(20, 20, 0);
    sim.addSimulatedEntity(ent);


    robot = new GhostRobot("Christophe", new SwingGridView());
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);

    ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(60, 60, 0);
    sim.addSimulatedEntity(ent);


    robot = new GhostRobot("Ruben", new SwingGridView());
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);

    ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(100, 100, 0);
    sim.addSimulatedEntity(ent);


  }

  @Test
  public void testBackupGapcorrection() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMap());

    putGhostRobot(9, 20, 90, new LeftFollowingGhostNavigator(), "Michiel");

  }

  private SimulatedEntity putGhostRobot(int x, int y, int angle) {
    return putGhostRobot(x, y, angle, new GhostNavigator(), r.nextInt() + "");
  }

  private SimulatedEntity putGhostRobot(int x, int y, int angle, String name) {
    return putGhostRobot(x, y, angle, new GhostNavigator(), name);

  }

  private SimulatedEntity putGhostRobot(int x, int y, int angle, Navigator nav, String name) {
    GhostRobot robot = new GhostRobot(name, new SwingGridView());
    robot.useNavigator(new LeftFollowingGhostNavigator());

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(x, y, angle);
    sim.addSimulatedEntity(ent);

    return ent;
  }
}
