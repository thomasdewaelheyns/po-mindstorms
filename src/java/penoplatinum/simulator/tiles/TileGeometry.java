package penoplatinum.simulator.tiles;

import java.awt.Point;
import penoplatinum.simulator.Baring;

public class TileGeometry {

  /**
   * calculates the point where given an angle and position, the  robot will 
   * "hit" a wall of this/a tile.
   */
  public static Point findHitPoint(double X, double Y, double angle, int size) {
    double x, y, dx, dy;

    if (angle <= 90) {
      dx = size - X;
      dy = T(dx, angle);
      if (dy > Y) {
        dy = Y;
        dx = T(dy, 90 - angle);
      }
      x = X + dx;
      y = Y - dy;
    } else if (angle > 90 && angle <= 180) {
      dx = X;
      dy = T(dx, 180 - angle);
      if (dy > Y) {
        dy = Y;
        dx = T(dy, angle - 90);
      }
      x = X - dx;
      y = Y - dy;
    } else if (angle > 180 && angle <= 270) {
      dx = X;
      dy = T(dx, angle - 180);
      if (dy > (size - Y)) {
        dy = (size - Y);
        dx = T(dy, 270 - angle);
      }
      x = X - dx;
      y = Y + dy;
    } else {
      // angle > 270 && angle < 360
      dx = size - X;
      dy = T(dx, 360 - angle);
      if (dy > (size - Y)) {
        dy = (size - Y);
        dx = T(dy, angle - 270);
      }
      x = X + dx;
      y = Y + dy;
    }
    return new Point((int) x, (int) y);
  }

  private static double T(double x, double d) {
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
    return x * Math.tan(Math.toRadians(d));
  }

  // simple application of a^2 + b^2 = c^2
  public static double getDistance(double x, double y, Point hit) {
    return Math.sqrt(Math.pow(hit.x - x, 2) + Math.pow(hit.y - y, 2));
  }

  /**
   * based on a hit determine the wall that has been hit
   */
  public static int getHitWall(Point hit, int size, double angle) {
    if (hit.y == 0) {                          // North
      if (hit.x == 0) {
        if (90 < angle && angle < 180) {
          return Baring.NW;
        } else if (angle <= 90) {
          return Baring.N;
        } else {
          return Baring.W;
        }
      } else if (hit.x == size) {
        if (0 < angle && angle < 90) {
          return Baring.NE;
        } else if (angle >= 90) {
          return Baring.N;
        } else {
          return Baring.E;
        }
      } else {
        return Baring.N;
      }
    } else if (hit.y == size) {                  // South
      if (hit.x == 0) {
        if (270 < angle) {
          return Baring.SW;
        } else if (angle < 90) {
          return Baring.W;
        } else {
          return Baring.S;
        }
      } else if (hit.x == size) {
        return Baring.SE;
      } else {
        return Baring.S;
      }
    } else {                                    // East or West
      if (hit.x == 0) {
        return Baring.W;
      } else {
        return Baring.E;
      }
    }
  }
}
