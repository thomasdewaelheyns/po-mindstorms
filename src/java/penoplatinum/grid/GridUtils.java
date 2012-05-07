/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import penoplatinum.grid.agent.Agent;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

/**
 * Utility functions for grid debuggin
 * @author MHGameWork
 */
public class GridUtils {

  public static String createGridSectorsString(Grid grid) {
    String ret = "";

    /*for (int top = grid.getMinTop(); top <= grid.getMaxTop(); top++) {
      for (int left = grid.getMinLeft(); left <= grid.getMaxLeft(); left++) {
        Sector sector = grid.getSectorAt(new Point(left, top));
        if (sector == null)
          continue;
        ret += "(" + left + "," + top + "): " + sector.toString();
        ret += "\n";
      }
    }/**/
    return ret;
  }

  public static String createSectorWallsString(Sector s) {
    return "";
    /*return "N" + (s.knowsWall(Bearing.N) ? (s.hasWall(Bearing.N) ? "Y" : " ") : "?")
            + "E" + (s.knowsWall(Bearing.E) ? (s.hasWall(Bearing.E) ? "Y" : " ") : "?")
            + "S" + (s.knowsWall(Bearing.S) ? (s.hasWall(Bearing.S) ? "Y" : " ") : "?")
            + "W" + (s.knowsWall(Bearing.W) ? (s.hasWall(Bearing.W) ? "Y" : " ") : "?");
    /**/
  }

  public static boolean hasSameWallsAs(Sector s, Sector d) {
    for (Bearing b : Bearing.NESW) {

      if (s.knowsWall(b) != d.knowsWall(b))
        return false;
      if (s.knowsWall(b) && (s.hasWall(b) != d.hasWall(b)))
        return false;
    }

    return true;
  }

  /**
   * True when each wall is known
   */
  public static boolean isFullyKnown(Sector s) {
    for (Bearing b : Bearing.NESW)
      if (!s.knowsWall(b))
        return false;

    return true;
  }

  public static void clearWalls(Sector s) {
    for (Bearing b : Bearing.NESW)
      s.clearWall(b);
  }

  /**
   * Returns true when we can say for sure that it is possible for the robot
   * to access the neighbour at given bearing
   * @param atBearing
   * @return 
   */
  public static boolean givesAccessTo(Sector s, Bearing atBearing) {
    return s.knowsWall(atBearing) && s.hasNoWall(atBearing);
  }
}
