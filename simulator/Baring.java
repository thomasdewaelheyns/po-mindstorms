/**
 * Enumeration of barings
 * 
 * Author: Team Platinum
 */

public class Baring {
  public static final int N  = 0;
  public static final int E  = 1;
  public static final int S  = 2;
  public static final int W  = 3;
  public static final int NE = 4;
  public static final int SE = 5;
  public static final int SW = 6;
  public static final int NW = 7;

  // returns the move in left/right direction given a baring
  public static int moveLeft(int baring) {
    int move = 0;
    if( baring == Baring.NE || baring == Baring.E || baring == Baring.SE ) { 
      move = +1;
    }
    if( baring == Baring.NW || baring == Baring.W || baring == Baring.SW ) {
      move = -1;
    }
    return move;
  }

  // returns the move in top/down direction given a baring
  public static int moveTop(int baring) {
    int move = 0;
    if( baring == Baring.NE || baring == Baring.N || baring == Baring.NW ) { 
      move = -1;
    }
    if( baring == Baring.SE || baring == Baring.S || baring == Baring.SW ) {
      move = +1;
    }
    return move;
  }
}
