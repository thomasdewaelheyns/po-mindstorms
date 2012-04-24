package penoplatinum.simulator;

import penoplatinum.simulator.view.ViewRobot;

/**
 * This class is responsible for representing a robot in the simulator
 * @author MHGameWork
 */
public interface RobotEntity {

  void step();

  ViewRobot getViewRobot();

  double getDir();

  double getPosX();

  double getPosY();
}
