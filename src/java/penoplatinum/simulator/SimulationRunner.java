package penoplatinum.simulator;

import penoplatinum.Utils;

/**
 * SimulationRunner
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation.
 * 
 * Future improvement: Use DI (Spring,...) framework to externalize the wiring.
 *
 * Author: Team Platinum
 */

class SimulationRunner {

  public static void main(String[] args) {
    // setup the robot
    Navigator navigator = new LineFollowerNavigator();
    Robot     robot     = new NavigatorRobot(navigator);

    // construct a course
    Map map = new Map(4)
      .add(Tile.S_E) .add(Tile.W_E) .add(Tile.W_E) .add(Tile.W_S)
      .add(Tile.E_N) .add(Tile.E_W) .add(Tile.S_W) .add(Tile.N_S)
      .add(Tile.S_E) .add(Tile.W_E) .add(Tile.W_N) .add(Tile.N_S)
      .add(Tile.E_N) .add(Tile.E_W) .add(Tile.E_W) .add(Tile.N_W);

    // Future implementation
    // Map map = Map.fromFile("map.txt");  // load a map from a file

    // setup the simulator, (optionally) providing a view and add the course
    // and the robot
    Simulator simulator = new Simulator();
    simulator.displayOn(new SwingSimulationView());
    simulator.useMap   (map);
    // put the robot at position 150 cm from top, 150 cm from left, in an
    // angle of 33 degrees, with 0 degrees pointing north
    simulator.putRobotAt (robot, 20, 60, 0);

    // give robot instructions through the communication layer
    // simulator.send( "start" );

    // start the simulator and enjoy the ride
    simulator.run();
  }

}
