

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
import penoplatinum.navigators.BehaviourNavigator;
import penoplatinum.simulator.NavigatorRobot;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulationRobotAgent;
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

      Simulator sim1 = new Simulator();
      SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent("Angie"), new NavigatorRobot(new BehaviourNavigator()));
      ent.setPostition(200,200,10);
      sim1.addSimulatedEntity(ent);
      sim1.useMap(SimulationRunner.createDefaultMap());
      sim1.displayOn(new SwingSimulationView());
      sim1.run();


  }
}
