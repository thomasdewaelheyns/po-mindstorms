package penoplatinum.simulator;

import penoplatinum.navigators.TestNavigator;

/**
 * TestSimulation
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation. All ready to test.
 * 
 * @author: Team Platinum
 */
public class TestSimulation {

  private Simulator simulator;
  private Navigator navigator;
  private Robot robot;
  private SimulatedEntity entity;

  public TestSimulation() {
    this.simulator = new Simulator();
    // handy for debugging (FIXME: needs to be integrated better)
    // this.simulator.displayOn( new SwingSimulationView() );
    this.navigator = new TestNavigator();
    this.robot = new NavigatorRobot(this.navigator);
    SimulatedEntity entity = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent("PlatinumSimulated"), this.robot);
    simulator.addSimulatedEntity(entity);
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
    entity.setPostition(x, y, direction);
    return this;
  }

  public TestSimulation run() {
    this.simulator.run();
    return this;
  }

  public Simulator getSimulator() {
    return this.simulator;
  }
  
  public SimulatedEntity getSimulatedEntity(){
    return entity;
  }
  
  public Robot getRobot(){
    return this.robot;
  }
}
