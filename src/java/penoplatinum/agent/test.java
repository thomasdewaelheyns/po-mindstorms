package penoplatinum.agent;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import penoplatinum.bluetooth.SimulatedConnection;

import penoplatinum.grid.SwingGridView;

import penoplatinum.pacman.DashboardAgent;
import penoplatinum.pacman.GhostRobot;
import penoplatinum.pacman.LeftFollowingGhostNavigator;
import penoplatinum.pacman.GhostNavigator;

import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulationRobotAgent;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.view.SwingSimulationView;

import penoplatinum.map.Map;
import penoplatinum.map.MapFactory;
import penoplatinum.map.MapFactorySector;
import penoplatinum.map.mazeprotocolinterpreter.ProtocolMapFactory;

public class test {
  public static void main(String[] args) {
    Simulator sim = new Simulator();

    try {
      sim.useMap(createSectorMap());
    } catch(Exception e) {
      System.err.println(e);
    }

    SimulatedConnection conn = new SimulatedConnection();
    final SimulatedConnection remoteConn = new SimulatedConnection();
    remoteConn.setEndPoint(conn);
    conn.setEndPoint(remoteConn);

    GhostRobot robot = new GhostRobot("Ikke!", new SwingGridView());
    robot.useDashboardAgent(new DashboardAgent(conn));
    robot.useNavigator(new GhostNavigator());

    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(20, 20, 0);
    sim.addSimulatedEntity(ent);

    GhostRobot robot2 = new GhostRobot("robot2", new SwingGridView());
    //robot2.useDashboardAgent(new DashboardAgent(conn));
    robot2.useNavigator(new GhostNavigator());

    SimulatedEntity ent2 = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot2);
    ent2.setPostition(140, 60, 0);
    sim.addSimulatedEntity(ent2);

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
  
  public static Map createSectorMap() throws FileNotFoundException {
    Map m;
    File f = new File("../simulator/map2.track");
    Scanner sc = new Scanner(f);
    MapFactorySector fact = new MapFactorySector();
    m = fact.getMap(sc);
    return m;
  }
}
