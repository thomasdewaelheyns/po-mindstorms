package penoplatinum.simulator.view;

import penoplatinum.map.Map;

/**
 * SimulationView interface
 * 
 * Defines the interface for Viewers for the Simulator
 * 
 * @author: Team Platinum
 */

public interface SimulationView {
  public void showMap(Map map);
  public void addRobot(ViewRobot r);
  public void updateRobots();
}
