/**
 * SimulationRunner
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation.
 * 
 * Future improvment: Use DI (Spring,...) framework to externalize the wiring.
 *
 * Author: Team Platinum
 */

class SimulationRunner {

  public static void main(String[] args) {
    // setup the robot
    Robot robot = new BumperNavigatorRobot();

    // construct a course
    Map map = new Map(4);
    map.add(new Tile(9)) .add(new Tile(5)).add(new Tile(5)).add(new Tile(3))
       .add(new Tile(12)).add(new Tile(5)).add(new Tile(3)).add(new Tile(10))
       .add(new Tile(9)) .add(new Tile(5)).add(new Tile(6)).add(new Tile(10))
       .add(new Tile(12)).add(new Tile(5)).add(new Tile(5)).add(new Tile(6));

    // Future implementation
    // Map map = Map.fromFile("map.txt");  // load a map from a file

    // setup the simulator, (optionally) providing a view and add the course
    // and the robot
    Simulator simulator = new Simulator();
    simulator.displayOn(new SwingSimulationView());
    simulator.useMap   (map);
    // put the robot at position 50 cm from top, 50 cm from left, in an
    // angle of 33 degrees, with 0 degrees pointing north
    simulator.putRobotAt (robot, 150, 150, 33);

    // give robot instructions through the communication layer
    // simulator.send( "22;0.05" ); // run polygon with 22 vertexes of 5cm

    // start the simulator and enjoy the ride
    simulator.run();
  }

}
