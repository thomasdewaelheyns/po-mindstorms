package penoplatinum.simulator;

/**
 * Tickable interface
 * 
 * Defines an interface for components that are influenced by time. The 
 * Simulator will provide the elapsed time in seconds.
 * 
 * @author: Team Platinum
 */

public interface Tickable {

  // updates itself based on the elapsed time since the last tick
  public void tick(double elapsed);

}
