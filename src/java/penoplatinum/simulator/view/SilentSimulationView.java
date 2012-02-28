package penoplatinum.simulator.view;

/**
 * SilentSimulationView
 * 
 * Implementation of SimulationView that outputs nothing.
 * 
 * @author: Team Platinum
 */

import java.util.List;
import penoplatinum.map.Map;

public class SilentSimulationView implements SimulationView {
  
  public void showMap(Map map) {
    // this method is intentionally left blank
  }
  
  public void log( String msg ) {
    // this method is intentionally left blank
  }

  @Override
  public void addRobot(ViewRobot r) {
    // this method is intentionally left blank
  }

  @Override
  public void updateRobots() {
    // this method is intentionally left blank
  }
}
