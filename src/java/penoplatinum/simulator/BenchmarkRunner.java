package penoplatinum.simulator;

import penoplatinum.Utils;

/**
 * BenchmarkRunner
 * 
 * Constructs a robot and course, sets up the Simulator runs the simulations
 * and outputs the Fitness of this Robot on this course.
 * 
 * Author: Team Platinum
 */

class BenchmarkRunner {

  public static void main(String[] args) {
    // setup the robot
    Navigator navigator = new SonarNavigator();
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
    simulator.useMap   (map);
    // put the robot at position 150 cm from top, 150 cm from left, in an
    // angle of 33 degrees, with 0 degrees pointing north
    simulator.putRobotAt (robot, 150, 150, 33);

    // give robot instructions through the communication layer
    // simulator.send( "start" );

    System.out.print("Starting benchmark of Navigator..." );

    // start the simulator and enjoy the ride
    simulator.run();
    
    System.out.println(" completed.");
    System.out.println("Fitness = " + simulator.getFitness());
  }

}
