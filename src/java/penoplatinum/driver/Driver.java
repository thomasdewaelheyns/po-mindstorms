package penoplatinum.driver;

/**
 * Driver
 * 
 * Counterpart of the Navigator, focussing on just driving optimally. It 
 * accepts a robot to retrieve all information needed to perform its actions
 * and determine what to do in each step.
 *
 * The Driver can be instructed to perform some movement actions, typically
 * by the Navigator. These can be expreseed in (forward) movement and turn
 * actions through the interface. Different Drivers can implement these 
 * methods in different ways.
 *
 * When the robot is no longer busy, it can be asked if the instructions have
 * been performed successfully. A Navigator can then updates its information.
 * Sometimes these instructions are not successfully performed, e.g. 
 * a collision with another robot or an other static obstacle (wall) might
 * prevent the Driver from completing its action.
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

  // I can indicate that I'm still busy performing an Action, this might be an
  // instruction, but can also be some internal action, like re-alignment or
  // evasive maneuvers.
  public boolean isBusy();

  // if the Driver is still busy, he can be instructed to proceed...
  public Driver proceed();
  
  // when the Driver is ready, we can request feedback about the action that
  // was previously requested
  public boolean completedLastInstruction();
}
