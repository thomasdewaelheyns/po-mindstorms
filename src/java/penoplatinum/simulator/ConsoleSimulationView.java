package penoplatinum.simulator;

/**
 * ConsoleSimulationView
 * 
 * Implementation of the SimulationView that displays the status of the 
 * Simulation on the console.
 * 
 * Author: Team Platinum
 */

import java.util.List;

public class ConsoleSimulationView implements SimulationView {
  public void updateRobot( int x, int y, int direction,
                           List<Integer> distances, List<Integer> angles )
  {
    this.log( "Robot is at " + x + "," + y + " / " + direction );
  }

  public void showMap(Map map) {
    // this method is intentionally left blank
    // no map required on console ;-)
  }

  public void log(String msg) {
    System.out.println( msg );
  }
}
