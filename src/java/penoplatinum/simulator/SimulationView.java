package penoplatinum.simulator;

/**
 * SimulationView interface
 * 
 * Defines the interface for Viewers for the Simulator
 * 
 * Author: Team Platinum
 */

import java.util.List;

public interface SimulationView {
  public void showMap(Map map);
  public void updateRobot( int x, int y, int direction,
                           List<Integer> values, List<Integer> angles);
  public void log(String msg);
}
