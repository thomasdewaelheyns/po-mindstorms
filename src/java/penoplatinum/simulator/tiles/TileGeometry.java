package penoplatinum.simulator.tiles;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class TileGeometry {

  public static double height(double distance, double angle) {
    /**
     * Geonometry used:
     *
     *            +
     *          / |
     *        /   |  Y
     *      / a   |
     *    +-------+
     *        X
     *
     *    tan(a) = Y/X    =>   Y = tan(a) * X     ||   X = Y / tan(a)
     */
    return distance * Math.tan(Math.toRadians(angle));
  }

  // simple application of a^2 + b^2 = c^2
  public static double getDistance(double x, double y, Point hit) {
    return Math.sqrt(Math.pow(hit.getX() - x, 2) + Math.pow(hit.getY() - y, 2));
  }
}