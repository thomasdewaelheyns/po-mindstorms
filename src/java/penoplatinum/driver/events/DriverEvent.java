package penoplatinum.driverevents;

import penoplatinum.actions.ActionQueue;
import penoplatinum.simulator.Model;

/**
 * The is an interface that checks different events and changes the driverqueue accordingly.
 * @author Rupsbant
 */
public interface DriverEvent {
  
  DriverEvent nextEvent();
  
  void checkEvent(Model m, ActionQueue queue);
  
}
