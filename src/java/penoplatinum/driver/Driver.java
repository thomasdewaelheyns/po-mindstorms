package penoplatinum.driver;

/**
 * Driver
 * 
 * Counterpart of the Navigator, focussing on just driving optimally. It 
 * accepts a robot to retrieve all information needed to perform its actions
 * and determine what to do in each step.
 * 
 * @author: Team Platinum
 */
 
import penoplatinum.robot.Robot;


public interface Driver {
  // we're driving Miss Robot
  public Driver drive(Robot robot);

  // we can control the Driver using basic movement methods:
  public Driver move(double distance);
  public Driver turn(int angle);

  // I can indicate that I'm still busy performing an Action
  public boolean isBusy();

  // If the Driver is still busy, he can be instructed to continue...
  public void continue();
}
