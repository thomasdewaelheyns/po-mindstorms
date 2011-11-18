package penoplatinum.simulator;

/**
 * ConsoleSimulationView
 * 
 * Implementation of the SimulationView that displays the status of the 
 * Simulation on the console.
 * 
 * Author: Team Platinum
 */
public class ConsoleSimulationView implements SimulationView {
  public void updateRobot( int x, int y, int direction ) {
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
