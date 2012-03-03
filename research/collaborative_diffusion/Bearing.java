/**
 * Enumeration of Bearings
 * 
 * @author: Team Platinum
 */

// TODO: turn in into a class with instances and methods

public class Bearing {
  public static final int NONE = -1;
  public static final int N    =  0;
  public static final int E    =  1;
  public static final int S    =  2;
  public static final int W    =  3;

  public static int reverse(int bearing) {
    switch(bearing) {
      case Bearing.N: return Bearing.S;
      case Bearing.E: return Bearing.W;
      case Bearing.S: return Bearing.N;
      case Bearing.W: return Bearing.E;
    }
    return Bearing.NONE; // shouldn't happen ;-)
  }
  
  public static int leftFrom(int bearing) {
    switch(bearing) {
      case Bearing.N: return Bearing.W;
      case Bearing.E: return Bearing.N;
      case Bearing.S: return Bearing.E;
      case Bearing.W: return Bearing.S;
    }
    return Bearing.NONE; // shouldn't happen ;-)
  }

  public static int rightFrom(int bearing) {
    switch(bearing) {
      case Bearing.N: return Bearing.E;
      case Bearing.E: return Bearing.S;
      case Bearing.S: return Bearing.W;
      case Bearing.W: return Bearing.N;
    }
    return Bearing.NONE; // shouldn't happen ;-)
  }
  
  // bearings are N-based, this method allows to transform such a bearing
  // to a bearing, based on a different origin
  public static int withOrigin(int bearing, int origin) {
    if( origin == Bearing.N ) { return bearing; }
    throw new RuntimeException("Bearing::withOrigin not fully implemented.");
  }
}
