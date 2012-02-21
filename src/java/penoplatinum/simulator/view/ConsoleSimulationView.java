package penoplatinum.simulator.view;

/**
 * ConsoleSimulationView
 * 
 * Implementation of the SimulationView that displays the status of the 
 * Simulation on the console.
 * 
 * @author: Team Platinum
 */

import java.util.List;
import penoplatinum.simulator.Map;

public class ConsoleSimulationView implements SimulationView {
  private List<ViewRobot> robots;
  
  public void updateRobot( int x, int y, int direction,
                           List<Integer> distances, List<Integer> angles )
  {
    this.log( "Robot is at " + x + "," + y + " / " + direction );
  }

  @Override
  public void showMap(Map map) {
    // this method is intentionally left blank
    // no map required on console ;-)
  }

  @Override
  public void log(String msg) {
    System.out.println( msg );
  }

  @Override
  public void addRobot(ViewRobot r) {
    robots.add(r);
    this.log("Robot added!");
  }

  @Override
  public void updateRobots() {
    for(ViewRobot robot:robots){
      this.log("Robot at " + robot.getX() + ", " + robot.getY() + " / " + robot.getDirection());
    }
  }
}
