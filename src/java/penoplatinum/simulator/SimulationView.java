package penoplatinum.simulator;

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
  public void log(String msg);
}
