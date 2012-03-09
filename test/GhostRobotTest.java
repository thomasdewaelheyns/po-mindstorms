
import java.io.FileNotFoundException;
import java.util.Random;
import org.junit.Test;
import penoplatinum.ghost.GhostRobot;
import penoplatinum.ghost.LeftFollowingGhostNavigator;
import penoplatinum.grid.SwingGridView;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulationRobotAgent;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.SimulatorTest;
import penoplatinum.simulator.view.SwingSimulationView;

/**
 *
 * @author MHGameWork
 */
public class GhostRobotTest {

  @Test
  public void testGhostRobotLeftFollowing() throws FileNotFoundException {
    Simulator sim = new Simulator();
    sim.useMap(SimulatorTest.createSectorMap());

    GhostRobot robot = new GhostRobot("Michiel");
    robot.useNavigator(new LeftFollowingGhostNavigator(robot.getGhostModel()));

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(20, 20, 0);
    sim.addSimulatedEntity(ent);

    sim.useStepRunnable(new Runnable() {

      @Override
      public void run() {
      }
    });

    sim.displayOn(new SwingSimulationView());
    sim.run();


  }

  @Test
  public void testGhostRobotGhostNavigator() throws FileNotFoundException {
    Simulator sim = new Simulator();
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

    sim.useStepRunnable(new Runnable() {

      @Override
      public void run() {
      }
    });

    sim.displayOn(new SwingSimulationView());
    sim.run();
  }

  @Test
  public void testGhostRobotMultiple() throws FileNotFoundException {
    Simulator sim = new Simulator();
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

    sim.useStepRunnable(new Runnable() {

      @Override
      public void run() {
      }
    });

    sim.displayOn(new SwingSimulationView());
    sim.run();
  }
  
  @Test
  public void testGhostRobotMazeProtocol() throws FileNotFoundException {
    Simulator sim = new Simulator();
    sim.useMap(SimulatorTest.createSectorMazeProtocol());
    SwingGridView view = new SwingGridView();

    Random r = new Random();
    final String name = r.nextInt() + "";


    GhostRobot robot = new GhostRobot(name, view);
    final GhostNavigator ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);
    ghostNavigator.setModel(robot.getGhostModel());

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(name), robot);
    ent.setPostition(20 + 1 * 40, 20 + 1 * 40, 0);
    sim.addSimulatedEntity(ent);

    sim.useStepRunnable(new Runnable() {

      @Override
      public void run() {
      }
    });

    sim.displayOn(new SwingSimulationView());
    sim.run();
  }

   @Test
  public void testGhostRobot3() throws FileNotFoundException {
    Simulator sim = new Simulator();
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


    sim.useStepRunnable(new Runnable() {

      @Override
      public void run() {
      }
    });

    sim.displayOn(new SwingSimulationView());
    sim.run();
  }

  
  @Test
  public void testBackupGapcorrection() throws FileNotFoundException {
    Simulator sim = new Simulator();
    sim.useMap(SimulatorTest.createSectorMap());

    GhostRobot robot = new GhostRobot("Michiel");
    robot.useNavigator(new LeftFollowingGhostNavigator(robot.getGhostModel()));

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(9, 20, 90);
    sim.addSimulatedEntity(ent);

    sim.useStepRunnable(new Runnable() {

      @Override
      public void run() {
      }
    });

    sim.displayOn(new SwingSimulationView());
    sim.run();


  }
}
