package penoplatinum.simulator;

/**
 * Enumeration of barings
 * 
 * @author: Team Platinum
 */
public class Baring {

  public static final int NONE = -1;
  public static final int N = 0;
  public static final int E = 1;
  public static final int S = 2;
  public static final int W = 3;
  public static final int NE = 4;
  public static final int SE = 5;
  public static final int SW = 6;
  public static final int NW = 7;

  // returns the move in left/right direction given a baring
  public static int moveLeft(int baring) {
    int move = 0;
    if (baring == Baring.NE || baring == Baring.E || baring == Baring.SE) {
      move = +1;
    }
    if (baring == Baring.NW || baring == Baring.W || baring == Baring.SW) {
      move = -1;
    }
    return move;
  }

  // returns the move in top/down direction given a baring
  public static int moveTop(int baring) {
    int move = 0;
    if (baring == Baring.NE || baring == Baring.N || baring == Baring.NW) {
      move = -1;
    }
    if (baring == Baring.SE || baring == Baring.S || baring == Baring.SW) {
      move = +1;
    }
    return move;
  }

  // returns the neighbour line that influences a position in two ways
  public static int getLeftNeighbour(int baring) {
    return baring == Baring.N || baring == Baring.S ? Baring.W : Baring.N;
  }

  // returns the neighbour line that influences a position in one ways
  public static int getRightNeighbour(int baring) {
    return baring == Baring.N || baring == Baring.S ? Baring.E : Baring.S;
  }

  
  /**
   * WARNING: faulty!!
   * @param angle
   * @return 
   */
  public static int getBaringFromAngle(int angle) {
//    if (angle < 0) {
//      angle += (angle / 360 + 1) * 360;
//    }
//    angle = angle % 360;
//    if (angle == 45 + 0 * 90) {
//      return Baring.NE;
//    }
//    if (angle == 45 + 1 * 90) {
//      return Baring.NW;
//    }
//    if (angle == 45 + 2 * 90) {
//      return Baring.SW;
//    }
//    if (angle == 45 + 3 * 90) {
//      return Baring.SE;
//    }
//
//    switch ((45 + angle) / 90) {
//      case 0:
//        return Baring.E;
//      case 1:
//        return Baring.N;
//      case 2:
//        return Baring.S;
//      case 3:
//        return Baring.W;
//    }
//    
    throw new RuntimeException("Algoritm error!!");

  }
}
