package penoplatinum.simulator;

/**
 * Navigator Interface
 * 
 * A Navigator implements how the robot tries to reach its goal. As with a 
 * true Navigator, the robot will constantly ask it what to do, until the
 * navigator indicates it has reached its goal.
 * 
 * Author: Team Platinum
 */

public interface Navigator {
  public static final int NONE = 0;
  public static final int MOVE = 1;
  public static final int TURN = 2;
  public static final int STOP = 4;

  public Boolean reachedGoal();
  public int     nextAction();
  public double  getDistance();
  public double  getAngle();
}
