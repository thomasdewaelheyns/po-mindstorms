package penoplatinum.actions;

import penoplatinum.simulator.Navigator;

/**
 * This method drives forward until stopped by the driver itself.
 * @author: Team Platinum
 */
public class DriveForwardAction extends ActionSkeleton {

  public static final DriveForwardAction singleton = new DriveForwardAction();

  public DriveForwardAction() {
    setDistance(1);
    setAngle(0);
  }

  @Override
  public int getNextAction() {
    return Navigator.MOVE;
  }

  @Override
  public boolean isComplete() {
    return false; // Never complete!
  }

  @Override
  public String getKind() {
    return "Drive forward";
  }

  @Override
  public String getArgument() {
    return "";
  }
}