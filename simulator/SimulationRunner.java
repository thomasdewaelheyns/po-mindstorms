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
    Robot  robot  = new VeelhoekRobot(); // simple robot that drives a polygon

    // construct a course
    // Track track = new PlainTrack(); // this robot doesn't track a course
    // Future implementation
    // Track course = new FileTrack("map.txt");  // load a course from a map

    // setup the simulator, (optionally) providing a view and add the course
    // and the robot
    Simulator simulator = new Simulator();
    simulator.displayOn(new ConsoleSimulationView());
    // simulator.useTrack   (track)
    simulator.putRobotAt (robot, 10, 10, 0);  // robot @ 10,10, 0 angle (=N)

    // give robot instructions through the communication layer
    simulator.send( "5;0.20" ); // run polygon with 5 vertexes of length 20cm

    // start the simulator and enjoy the ride
    simulator.run();
  }

}
