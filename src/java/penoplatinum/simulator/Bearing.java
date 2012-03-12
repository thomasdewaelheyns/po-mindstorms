package penoplatinum.simulator;

import penoplatinum.map.Point;

/**
 * Bearing
 * 
 * Basic enumeration of bearings with a few utility functions.
 * 
 * @author: Team Platinum
 */

public class Bearing {
  public static final int NONE = -1;
  public static final int N    = 0;
  public static final int E    = 1;
  public static final int S    = 2;
  public static final int W    = 3;
  public static final int NE   = 4;
  public static final int SE   = 5;
  public static final int SW   = 6;
  public static final int NW   = 7;


  public static int reverse(int bearing) {
    switch (bearing) {
      case Bearing.N:
        return Bearing.S;
      case Bearing.E:
        return Bearing.W;
      case Bearing.S:
        return Bearing.N;
      case Bearing.W:
        return Bearing.E;
    }
    return Bearing.NONE; // shouldn't happen ;-)
  }

  public static int leftFrom(int bearing) {
    switch (bearing) {
      case Bearing.N:
        return Bearing.W;
      case Bearing.E:
        return Bearing.N;
      case Bearing.S:
        return Bearing.E;
      case Bearing.W:
        return Bearing.S;
    }
    return Bearing.NONE; // shouldn't happen ;-)
  }

  public static int rightFrom(int bearing) {
    switch (bearing) {
      case Bearing.N:
        return Bearing.E;
      case Bearing.E:
        return Bearing.S;
      case Bearing.S:
        return Bearing.W;
      case Bearing.W:
        return Bearing.N;
    }
    return Bearing.NONE; // shouldn't happen ;-)
  }

  // bearings are N-based, this method allows to transform such a bearing
  // to a bearing, based on a different origin
  public static int withOrigin(int bearing, int origin) {
    int newBearing = bearing;
    switch(origin) {
      case(Bearing.E): newBearing += 1; break;
      case(Bearing.S): newBearing += 2; break;
      case(Bearing.W): newBearing -= 1; break;
    }
    return (newBearing + 4) % 4;
  }

  public static Point mapToNorth(int rotation, int x, int y) {
    int newX, newY;
    switch (rotation) {
      case Bearing.N:
        newX = x;
        newY = y;
        break;
      case Bearing.E:
        newX = y;
        newY = -x;
        break;
      case Bearing.S:
        newX = -x;
        newY = -y;
        break;
      case Bearing.W:
        newX = -y;
        newY = x;
        break;
      default:
        throw new RuntimeException("NOO!");
    }
    return new Point(newX, newY);
  }
  
  // returns the move in left/right direction given a bearing
  public static int moveLeft(int bearing) {
    int move = 0;
    if (bearing == Bearing.NE || bearing == Bearing.E || bearing == Bearing.SE) {
      move = +1;
    }
    if (bearing == Bearing.NW || bearing == Bearing.W || bearing == Bearing.SW) {
      move = -1;
    }
    return move;
  }

  // returns the move in top/down direction given a bearing
  public static int moveTop(int bearing) {
    int move = 0;
    if (bearing == Bearing.NE || bearing == Bearing.N || bearing == Bearing.NW) {
      move = -1;
    }
    if (bearing == Bearing.SE || bearing == Bearing.S || bearing == Bearing.SW) {
      move = +1;
    }
    return move;
  }

  // returns the neighbour line that influences a position in two ways
  public static int getLeftNeighbour(int bearing) {
    return bearing == Bearing.N || bearing == Bearing.S ? Bearing.W : Bearing.N;
  }

  // returns the neighbour line that influences a position in one ways
  public static int getRightNeighbour(int bearing) {
    return bearing == Bearing.N || bearing == Bearing.S ? Bearing.E : Bearing.S;
  }

}
