/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.agent;

import java.io.FileNotFoundException;
import org.junit.Test;
import penoplatinum.bluetooth.SimulatedConnection;
import penoplatinum.gateway.Gateway;
import penoplatinum.grid.SwingGridView;
import penoplatinum.pacman.DashboardAgent;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.pacman.LeftFollowingGhostNavigator;
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
    robot.useDashboardAgent(new DashboardAgent(conn));
    robot.useNavigator(new LeftFollowingGhostNavigator());

    SimulatedEntity ent = new SimulatedEntity(robot);
    ent.setPostition(20, 20, 0);
    sim.addSimulatedEntity(ent);



    Thread t = new Thread(new Runnable() {

      @Override
      public void run() {
        Gateway ag = new Gateway();
        ag.connect(remoteConn);
        ag.start();
      }
    });
    t.start();
            
    
    
    
    sim.displayOn(new SwingSimulationView());
    sim.run();
  }
}
