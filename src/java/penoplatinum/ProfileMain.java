package penoplatinum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import penoplatinum.ghost.GhostRobot;
import penoplatinum.grid.SwingGridView;
import penoplatinum.map.MapFactorySector;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.simulator.Map;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulationRobotAgent;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.view.SwingSimulationView;

/**
 * SimulationRunner
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation.
 * 
 * @author: Team Platinum
 */
public class ProfileMain {

  public static Map createSectorMap() throws FileNotFoundException {
    Map m;
    File f = new File("..\\..\\src\\java\\penoplatinum\\simulator\\map2.track");
    Scanner sc = new Scanner(f);
    MapFactorySector fact = new MapFactorySector();
    m = fact.getMap(sc);
    return m;
  }

  public static void main(String[] args) throws FileNotFoundException {
    Simulator sim = new Simulator();
    sim.useMap(createSectorMap());

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
}
