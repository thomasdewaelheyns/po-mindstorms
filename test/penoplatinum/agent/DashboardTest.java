/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.agent;

import java.io.FileNotFoundException;
import org.junit.Test;
import penoplatinum.bluetooth.SimulatedConnection;
import penoplatinum.grid.SwingGridView;
import penoplatinum.pacman.DashboardReporter;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.pacman.LeftFollowingGhostNavigator;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.SimulatorTest;
import penoplatinum.simulator.view.SwingSimulationView;

/**
 *
 * @author MHGameWork
 */
public class DashboardTest {

  @Test
  public void testSimulatedDashboard() throws FileNotFoundException {
    Simulator sim = new Simulator();

    sim.useMap(SimulatorTest.createSectorMap());

    SimulatedConnection conn = new SimulatedConnection();
    final SimulatedConnection remoteConn = new SimulatedConnection();
    remoteConn.setEndPoint(conn);
    conn.setEndPoint(remoteConn);

    GhostRobot robot = new GhostRobot("Ikke!", new SwingGridView());
    robot.useDashboardReporter(new DashboardReporter(conn));
    robot.useNavigator(new LeftFollowingGhostNavigator());

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulatedGatewayClient(), robot);
    ent.setPostition(20, 20, 0);
    sim.addSimulatedEntity(ent);



    Thread t = new Thread(new Runnable() {

      @Override
      public void run() {
        Agent ag = new Agent();
        ag.connect(remoteConn);
        ag.start();
      }
    });
    t.start();
            
    
    
    
    sim.displayOn(new SwingSimulationView());
    sim.run();
  }
}
