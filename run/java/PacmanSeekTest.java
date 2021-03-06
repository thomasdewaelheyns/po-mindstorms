
import java.io.FileNotFoundException;
import java.util.Random;
import org.junit.Test;
import penoplatinum.grid.view.SwingGridView;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.simulator.entities.PacmanEntity;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulationRobotAPI;
import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.SimulatorTest;
import penoplatinum.simulator.view.SwingSimulationView;

public class PacmanSeekTest {

  @Test
  public void testGhostRobotMazeProtocol3() throws FileNotFoundException {
    Simulator sim = new Simulator();
    PacmanEntity p = new PacmanEntity(60, 60, 0);
    sim.useMap(SimulatorTest.createSectorMap2());
    sim.setPacmanEntity(p);
    Random r = new Random();
    String name = r.nextInt() + "";

    GhostRobot robot = new GhostRobot(name, new SwingGridView());
    GhostNavigator ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);
    robot.useRobotAPI(new SimulationRobotAPI());
    robot.useGatewayClient(new SimulatedGatewayClient());;
    ghostNavigator.setModel(robot.getGhostModel());
    SimulatedEntity ent = new SimulatedEntity(robot);
    ent.setPostition(20 + 3 * 40, 20 + 3 * 40, 180);
    //sim.addSimulatedEntity(ent);

    name = r.nextInt() + "";
    robot = new GhostRobot(name, new SwingGridView());
    ghostNavigator = new GhostNavigator();
    robot.useNavigator(ghostNavigator);
    robot.useRobotAPI(new SimulationRobotAPI());
    robot.useGatewayClient(new SimulatedGatewayClient());;
    ghostNavigator.setModel(robot.getGhostModel());
    ent = new SimulatedEntity(robot);
    ent.setPostition(20 + 2 * 40, 20 + 3 * 40, 90);

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
