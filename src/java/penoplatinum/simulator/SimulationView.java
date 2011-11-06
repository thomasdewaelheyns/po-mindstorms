package penoplatinum.simulator;

/**
 * SimulationView interface
 * 
 * Defines the interface for Viewers for the Simulator
 * 
 * Author: Team Platinum
 */
public interface SimulationView {
  public void showMap(Map map);
  public void updateRobot( int x, int y, int direction );
}
