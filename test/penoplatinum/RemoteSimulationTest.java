package penoplatinum;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.SimulationRunner;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.view.SwingSimulationView;

/**
 *
 * @author: Team Platinum
 */
public class RemoteSimulationTest {

  public RemoteSimulationTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testRemoteSimulation() throws IOException, InterruptedException {
    throw new UnsupportedOperationException();
//    Simulator sim1 = new Simulator();
//    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulatedGatewayClient(), new NavigatorRobot(new BehaviourNavigator()));
//    ent.setPostition(200, 200, 10);
//    sim1.addSimulatedEntity(ent);
//    sim1.useMap(SimulationRunner.createDefaultMap());
//    sim1.displayOn(new SwingSimulationView());
//    sim1.run();


  }

  @Test
  public void test4Simulators() throws InterruptedException {


    Thread t1 = runSimulatorThread(createArgs(20, 20, 120, "Joske"));
    Thread t2 = runSimulatorThread(createArgs(100, 25, 30, "Jefke"));
    Thread t3 = runSimulatorThread(createArgs(40, 20, 60, "Karel"));
    Thread t4 = runSimulatorThread(createArgs(20, 20, 250, "Joris"));
    t1.join();
    t2.join();
    t3.join();
    t4.join();

  }

  @Test
  public void test2Simulators() throws InterruptedException {


    Thread t1 = runSimulatorThread(createArgs(20, 20, 120, "Joske"));
    Thread t2 = runSimulatorThread(createArgs(100, 25, 30, "Jefke"));
    t1.join();
    t2.join();

  }

  private String createArgs(int x, int y, int angle, String name) {
    return "-n penoplatinum.navigators.BehaviourNavigator -p " + x + "," + y + "," + angle + " -m ..\\..\\src\\java\\penoplatinum\\simulator\\map2.track -name " + name;
  }

  private Thread runSimulatorThread(final String args) {
    Thread t = new Thread(new Runnable() {

      @Override
      public void run() {
        SimulationRunner.main(args.split(" "));
      }
    });

    t.start();

    return t;
  }
}
