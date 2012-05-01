package penoplatinum.simulator.tiles;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class TileGeometry {

  /**
   * calculates the point where given an angle and position, the  robot will 
   * "hit" a wall of this/a tile.
   */
  public static Point findHitPoint(double X, double Y, double angle, int size) {
    double x, y, dx, dy;

    if (angle <= 90) {
      dx = size - X;
      dy = height(dx, angle);
      if (dy > Y) {
        dy = Y;
        dx = height(dy, 90 - angle);
      }
      x = X + dx;
      y = Y - dy;
    } else if (angle > 90 && angle <= 180) {
      dx = X;
      dy = height(dx, 180 - angle);
      if (dy > Y) {
        dy = Y;
        dx = height(dy, angle - 90);
      }
      x = X - dx;
      y = Y - dy;
    } else if (angle > 180 && angle <= 270) {
      dx = X;
      dy = height(dx, angle - 180);
      if (dy > (size - Y)||dy==0 ) {
        dy = (size - Y);
        dx = height(dy, 270 - angle);
      }
      x = X - dx;
      y = Y + dy;
    } else {
      // angle > 270 && angle < 360
      dx = size - X;
      dy = height(dx, 360 - angle);
      if (dy > (size - Y)) {
        dy = (size - Y);
        dx = height(dy, angle - 270);
      }
      x = X + dx;
      y = Y + dy;
    }
    return new Point((int) Math.round(x), (int) Math.round(y));
  }

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

  /**
   * based on a hit determine the wall that has been hit
   */
  public static Bearing getHitWall(Point hit, int size, double angle) {
    if (hit.getY() == 0) {                          // North
      if (hit.getX() == 0) {
        if (90 < angle && angle < 180) {
          return Bearing.NW;
        } else if (angle <= 90) {
          return Bearing.N;
        } else {
          return Bearing.W;
        }
      } else if (hit.getX() == size) {
        if (0 < angle && angle < 90) {
          return Bearing.NE;
        } else if (angle >= 90) {
          return Bearing.N;
        } else {
          return Bearing.E;
        }
      } else {
        return Bearing.N;
      }
    } else if (hit.getY() == size) {                  // South
      if (hit.getX() == 0) {
        if (180 < angle && angle < 270) {
          return Bearing.SW;
        } else if (angle <= 180) {
          return Bearing.W;
        } else {
          return Bearing.S;
        }
      } else if (hit.getX() == size) {
        if (270 < angle) {
          return Bearing.SE;
        } else if (angle <= 180) {
          return Bearing.E;
        } else {
          return Bearing.S;
        }
      } else {
        return Bearing.S;
      }
    } else {                                    // East or West
      if (hit.getX() == 0) {
        return Bearing.W;
      } else {
        return Bearing.E;
      }
    }
  }


}