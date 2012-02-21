package penoplatinum.simulator;

import penoplatinum.simulator.tiles.Panels;
import penoplatinum.navigators.SonarNavigator;

/**
 * BenchmarkRunner
 * 
 * Constructs a robot and course, sets up the Simulator runs the simulations
 * and outputs the Fitness of this Robot on this course.
 * 
 * @author: Team Platinum
 */

class BenchmarkRunner {

  public static void main(String[] args) {
    // setup the robot
    Navigator navigator = new SonarNavigator();
    Robot     robot     = new NavigatorRobot(navigator);

    // construct a course
    Map map = new Map(4)
      .add(Panels.S_E) .add(Panels.W_E) .add(Panels.W_E) .add(Panels.W_S)
      .add(Panels.E_N) .add(Panels.E_W) .add(Panels.S_W) .add(Panels.N_S)
      .add(Panels.S_E) .add(Panels.W_E) .add(Panels.W_N) .add(Panels.N_S)
      .add(Panels.E_N) .add(Panels.E_W) .add(Panels.E_W) .add(Panels.N_W);

    // Future implementation
    // Map map = Map.fromFile("map.txt");  // load a map from a file

    // setup the simulator, (optionally) providing a view and add the course
    // and the robot
    Simulator simulator = new Simulator();
    simulator.useMap   (map);
    // put the robot at position 150 cm from top, 150 cm from left, in an
    // angle of 33 degrees, with 0 degrees pointing north
    SimulatedEntity s = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    s.setPostition(150, 150, 33);
    simulator.addSimulatedEntity(s);

    // give robot instructions through the communication layer
    // simulator.send( "start" );

    System.out.print("Starting benchmark of Navigator..." );

    // start the simulator and enjoy the ride
    simulator.run();
    
    System.out.println(" completed.");
    //System.out.println("Fitness = " + simulator.getFitness());
  }

}
