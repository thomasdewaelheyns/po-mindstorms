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
  public void move( float distance );
  
  // turns the robot on its spot by an angle expressed in degrees
  public void turn( int angle );
  
}