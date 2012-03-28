package penoplatinum.util;

/**
 * Bearing
 * 
 * Enumeration of bearings with a few utility functions.
 * 
 * @author: Team Platinum
 */

public enum Bearing {
  UNKNOWN (-1),
  N       ( 0),
  NE      ( 1),
  E       ( 2),
  SE      ( 3),
  S       ( 4),
  SW      ( 5),
  W       ( 6),
  NW      ( 7);

  private int bearing;

  private Bearing(int bearing) {
      this.bearing = bearing;
  }

  private int getValue() {
    return this.bearing;
  }
  
  private Bearing get(int value) {
    switch(value) {
      case -1: return UNKNOWN;
      case  0: return N;
      case  1: return NE;
      case  2: return E;
      case  3: return SE;
      case  4: return S;
      case  5: return SW;
      case  6: return W;
      case  7: return NW;
    }
    throw new RuntimeException("" + value + " isn't a valid Bearing." );
  }

  // returns the reverse Bearing
  public Bearing reverse() {
    return this.get( (this.bearing + 4) % 8);
  }

  // returns a Bearing in a 90 degree corner to the left
  public Bearing leftFrom() {
    return this.get( ((this.bearing - 2) + 8) % 8 );
  }

  // returns a Bearing in a 90 degree corner to the right
  public Bearing rightFrom() {
    return this.get( ((this.bearing + 2) + 8) % 8 );
  }
  
  // TODO: The following methods should be implemented differently or on
  //       a different class. Kept here to not break the code that uses them
  //       in a functional way

  // returns the neighbour line that influences a position in two ways
  public Bearing getLeftNeighbour() {
    return this == N || this == S ? W : N;
  }

  // returns the neighbour line that influences a position in one ways
  public Bearing getRightNeighbour() {
    return this == N || this == S ? E : S;
  }

}

/*
  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  !! No longer in use in the current codebase (Yeah!)

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

  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  !! Should be implemented on Point -> Should maybe be a Position class ?!!

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
  
  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  !! Should be implemented on a Position class as Position.moveTo(Bearing)
  
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

}
*/
