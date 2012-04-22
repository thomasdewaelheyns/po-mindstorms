package penoplatinum.simulator;

import penoplatinum.simulator.entities.SimulatedEntity;

/**
 * Sensor interface
 * 
 * Defines an interface for (querying) sensors.
 * 
 * @author: Team Platinum
 */

public interface Sensor {

  // returns the current value of the sensor
  public int getValue();
  
  // sets the simulator to measure in
  public void useSimulator(Simulator sim);
  
  // sets the simulated entity the sensor is mounted on
  public void useSimulatedEntity(SimulatedEntity simEntity);

}
