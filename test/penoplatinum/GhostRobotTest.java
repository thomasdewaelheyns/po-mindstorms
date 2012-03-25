package penoplatinum;

import java.io.FileNotFoundException;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.barcode.BarcodeTranslator;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.pacman.LeftFollowingGhostNavigator;
import penoplatinum.grid.SwingGridView;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.simulator.Bearing;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.SimulatorTest;
import penoplatinum.simulator.Navigator;
import penoplatinum.simulator.PacmanEntity;
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
  public void testInvertBarcode()
  {
    int a = BarcodeTranslator.reverse(12,6);
    int magic = 4;
  }
  
  @Test
  public void testGhostRobotLeftFollowing() throws FileNotFoundException {

    sim.useMap(SimulatorTest.createSectorMap());

    putGhostRobot(20, 20, 0, new LeftFollowingGhostNavigator(), "Michiel");

  }

  @Test
  public void testGhostRobotGhostNavigator() throws FileNotFoundException {
     sim.useMap(SimulatorTest.createSectorMap());

    putGhostRobot(20, 20, 0);

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
    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, 90);
//    putGhostRobot(20 + 5 * 40, 20 + 4 * 40, 0);

//    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
//    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);

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
  public void testBarcodeReverse() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMap());



    String name = r.nextInt() + "";

    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);

//    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
//    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);

  }

  @Test
  public void testBackupGapcorrection() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMap());

    putGhostRobot(9, 20, 90, new LeftFollowingGhostNavigator(), "Michiel");

  }

  @Test
  public void testPacman() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMap());



    String name = r.nextInt() + "";

    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);

    sim.setPacmanEntity(new PacmanEntity(20, 20, 0));
//    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
//    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);

  }

  @Test
  public void testGhostRobotMazeProtocol3Pacman() throws FileNotFoundException {
    sim.useMap(SimulatorTest.createSectorMazeProtocol2());



    String name = r.nextInt() + "";

    SimulatedEntity ent1 = putGhostRobot(20 + 5 * 40, 20 + 2 * 40, 0);
    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);
    putGhostRobot(20 + 5 * 40, 20 + 4 * 40, 0);
    
    

//    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
//    sim.setPacmanEntity(new PacmanEntity(260, 180, 0));
//    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);

  }
  
  private SimulatedEntity putGhostRobot(int x, int y, int angle) {
    return putGhostRobot(x, y, angle, new GhostNavigator(), r.nextInt() + "");
  }

  private SimulatedEntity putGhostRobot(int x, int y, int angle, String name) {
    return putGhostRobot(x, y, angle, new GhostNavigator(), name);

  }

  private SimulatedEntity putGhostRobot(int x, int y, int angle, Navigator nav, String name) {
    GhostRobot robot = new GhostRobot(name, new SwingGridView());
    
   
    SimulatedEntity ent = new SimulatedEntity(robot);
    
    robot.useNavigator(nav);
    robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(ent));
    robot.useGatewayClient(new SimulatedGatewayClient());
    
    ent.setPostition(x, y, angle);
    sim.addSimulatedEntity(ent);

    return ent;
  }
}
