/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator.tiles;

import java.awt.Point;
import penoplatinum.simulator.Baring;

/**
 *
 * @author Thomas
 */
public class TileGeometry {
  
  
  /**
   * calculates the point where given an angle and position, the  robot will 
   * "hit" a wall of this/a tile.
   */
  public static Point findHitPoint( int X, int Y, double angle, int size ) {
    double x, y, dx, dy;

    if( angle <= 90 ) {
      dx = size - X;
      dy = T( dx, angle );
      if( dy > Y ) {
        dy = Y;
        dx = T( dy, 90 - angle );
      }
      x = X + dx;
      y = Y - dy;
    } else if( angle > 90 && angle <= 180 ) {
      dx = X;
      dy = T( dx, 180-angle );
      if( dy > Y ) {
        dy = Y;
        dx = T( dy, angle - 90 );
      }
      x = X - dx;
      y = Y - dy;
    } else if( angle > 180 && angle <= 270 ) {
      dx = X;
      dy = T( dx, angle - 180 );
      if( dy > ( size - Y ) ) {
        dy = ( size - Y );
        dx = T( dy, 270 - angle );
      }
      x = X - dx;
      y = Y + dy;
    } else { 
      // angle > 270 && angle < 360
      dx = size - X;
      dy = T( dx, 360 - angle );
      if( dy > ( size - Y ) ) {
        dy = ( size - Y );
        dx = T( dy, angle - 270 );
      }
      x = X + dx;
      y = Y + dy;
    }
    return new Point((int)x,(int)y);
  }
  
  
  
  private static double T( double x, double d ) {
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
  public static double getDistance( int x, int y, Point hit ) {
    return Math.sqrt( Math.pow(hit.x - x, 2 ) + Math.pow(hit.y - y, 2 ) );    
  }
  
  
  
  
 
  /**
   * based on a hit determine the wall that has been hit
   */
  public static int getHitWall(Point hit, int size) {
    int wall;
    if( hit.y == 0 ) {                          // North
      wall = hit.x == 0 ? Baring.NW : ( hit.x == size ? Baring.NE : Baring.N );
    } else if( hit.y == size ) {                  // South
      wall = hit.x == 0 ? Baring.SW : ( hit.x == size ? Baring.SE : Baring.S );
    } else {                                    // East or West
      wall = hit.x == 0 ? Baring.W : Baring.E;
    }
    return wall;
  }
}


