/**
 * Enumeration of Bearings
 * 
 * @author: Team Platinum
 */

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
}
