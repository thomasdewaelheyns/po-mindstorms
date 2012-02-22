package penoplatinum.simulator;


/**
 * Robot API interface
 * 
 * Defines the abstracted methods to control a robot. It allows for different
 * implementations 
 * 
 * @author: Team Platinum
 */
public interface RobotAPI {

  // moves the robot in a straigth line for a distance expressed in meters
  public void move(double distance);

  // turns the robot on its spot by an angle expressed in degrees
  public void turn(int angle);

  // stop the robot immediately
  public void stop();

  // returns the current values for the sensors
  public int[] getSensorValues();

  // sets the speed for one of the motors
  public void setSpeed(int motor, int speed);

  //beeps once
  public void beep();

  /**
   * Places given reference point at the robot's current position
   * @param reference 
   */
  public void setReferencePoint(ReferencePosition reference);

  /**
   * Returns the position of the robot relative to given reference
   * @param reference 
   */
  public ExtendedVector getRelativePosition(ReferencePosition reference);
}
