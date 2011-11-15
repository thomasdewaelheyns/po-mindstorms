package penoplatinum.simulator;

/**
 * SilentSimulationView
 * 
 * Implementation of SimulationView that outputs nothing.
 * 
 * Author: Team Platinum
 */

import javax.swing.JFrame;

public class SilentSimulationView implements SimulationView {
  
  public void showMap(Map map) {
    // this method is intentionally blank
  }
  
  public void updateRobot( int x, int y, int direction ) {
    // this method is intentionally blank
  }
  
  public void log( String msg ) {
    // this method is intentionally blank
  }
}
