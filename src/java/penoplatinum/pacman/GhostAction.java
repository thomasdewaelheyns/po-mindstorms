package penoplatinum.pacman;

/**
 * GhostAction
 * 
 * Basic enumeration of typical actions performed by a Ghost
 * TODO: make this more general, GridAction
 * 
 * @author: Team Platinum
 */

public class GhostAction {
  public static final int RESET      = -1;
  public static final int NONE       =  0;
  public static final int FORWARD    =  1;
  public static final int TURN_LEFT  =  2;
  public static final int TURN_RIGHT =  3;
}
