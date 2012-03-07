
import java.io.FileNotFoundException;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent("Test"), robot);
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
    sim.useMap(SimulatorTest.createSectorMap());
    SwingGridView view = new SwingGridView();

    GhostRobot robot = new GhostRobot("Michiel", view);
    final GhostNavigator ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);
    ghostNavigator.setModel(robot.getGhostModel());

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent("Test"), robot);
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
  public void testGhostRobotGhostNavigator2() throws FileNotFoundException {
    Simulator sim = new Simulator();
    sim.useMap(SimulatorTest.createSectorMap2());
    SwingGridView view = new SwingGridView();

    GhostRobot robot = new GhostRobot("Michiel", view);
    final GhostNavigator ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);
    ghostNavigator.setModel(robot.getGhostModel());

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent("Test"), robot);
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
  public void testBackupGapcorrection() throws FileNotFoundException {
    Simulator sim = new Simulator();
    sim.useMap(SimulatorTest.createSectorMap());

    GhostRobot robot = new GhostRobot("Michiel");
    robot.useNavigator(new LeftFollowingGhostNavigator(robot.getGhostModel()));

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent("Test"), robot);
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
