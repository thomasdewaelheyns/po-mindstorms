package penoplatinum.simulator;

/**
 * SilentSimulationView
 * 
 * Implementation of SimulationView that outputs nothing.
 * 
 * Author: Team Platinum
 */

import javax.swing.JFrame;
import java.util.List;

public class SilentSimulationView implements SimulationView {
  
  public void showMap(Map map) {
    // this method is intentionally left blank
  }
  
  public void updateRobot( int x, int y, int direction,
                           List<Integer> values, List<Integer> angles )
  {
    // this method is intentionally left blank
  }
  
  public void log( String msg ) {
    // this method is intentionally left blank
  }
}
