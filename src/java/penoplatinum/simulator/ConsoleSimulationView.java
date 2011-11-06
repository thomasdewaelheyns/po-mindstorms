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
    System.out.println( "Robot is at " + x + "," + y + " / " + direction );
  }

    @Override
    public void showMap(Map map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
