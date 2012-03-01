/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.ghost;

import java.util.List;

/**
 *
 * @author MHGameWork
 */
public interface GhostNavigator {

  public static final int TURNLEFT = 1;
  public static final int TURNRIGHT = 2;
  public static final int TURNAROUND = 3;
  public static final int MOVE = 4;

  List<Integer> nextActions();
}
