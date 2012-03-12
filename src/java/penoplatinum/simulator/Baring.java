package penoplatinum.simulator;

/**
 * Enumeration of bearings
 * 
 * @author: Team Platinum
 */
public class Bearing {

  public static final int NONE = -1;
  public static final int N = 0;
  public static final int E = 1;
  public static final int S = 2;
  public static final int W = 3;
  public static final int NE = 4;
  public static final int SE = 5;
  public static final int SW = 6;
  public static final int NW = 7;

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

  
  /**
   * WARNING: faulty!!
   * @param angle
   * @return 
   */
  public static int getBearingFromAngle(int angle) {
//    if (angle < 0) {
//      angle += (angle / 360 + 1) * 360;
//    }
//    angle = angle % 360;
//    if (angle == 45 + 0 * 90) {
//      return Bearing.NE;
//    }
//    if (angle == 45 + 1 * 90) {
//      return Bearing.NW;
//    }
//    if (angle == 45 + 2 * 90) {
//      return Bearing.SW;
//    }
//    if (angle == 45 + 3 * 90) {
//      return Bearing.SE;
//    }
//
//    switch ((45 + angle) / 90) {
//      case 0:
//        return Bearing.E;
//      case 1:
//        return Bearing.N;
//      case 2:
//        return Bearing.S;
//      case 3:
//        return Bearing.W;
//    }
//    
    throw new RuntimeException("Algoritm error!!");

  }
}
