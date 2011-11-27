package penoplatinum.simulator;

/**
 * TestSimulation
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation. All ready to test.
 * 
 * Author: Team Platinum
 */
 
public class TestSimulation {
  
  private Simulator simulator;
  private Robot     robot;
  private Navigator navigator;
  
  public TestSimulation() {
    this.simulator = new Simulator();
    // handy for debugging (FIXME: needs to be integrated better)
    // this.simulator.displayOn( new SwingSimulationView() );
    this.navigator = new TestNavigator();
    this.robot     = new NavigatorRobot(this.navigator);
  }
  
  public TestSimulation setControler(GoalDecider controler) {
    this.navigator.setControler(controler);
    return this;
  }
  
  public TestSimulation useMap(Map map) {
    this.simulator.useMap(map);
    return this;
  }
  
  public TestSimulation putRobotAt(int x, int y, int direction) {
    this.simulator.putRobotAt(robot, x, y, direction);
    return this;
  }
  
  public TestSimulation run() {
    this.simulator.run();
    return this;
  }

  public Simulator getSimulator() {
    return this.simulator;
  }

  public Robot getRobot() {
    return this.robot;
  }
  
  public Navigator getNavigator() {
    return this.navigator;
  }
}
