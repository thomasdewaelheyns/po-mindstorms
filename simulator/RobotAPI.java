/**
 * Robot API interface
 * 
 * Defines the abstracted methods to control a robot. It allows for different
 * implementations 
 * 
 * Author: Team Platinum
 */

public interface RobotAPI {

  // moves the robot in a straigth line for a distance expressed in meters
  public void move( double distance );
  
  // turns the robot on its spot by an angle expressed in degrees
  public void turn( double angle );
  
  // stop the robot immediately
  public void stop();
  
  // returns the current values for the sensors
  public int[] getSensorValues();
}
