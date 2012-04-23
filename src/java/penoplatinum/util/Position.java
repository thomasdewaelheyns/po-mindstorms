package penoplatinum.util;

public class Position {
  
  public static int moveLeft(Bearing b, int left) {
    switch (b) {
      case NE:
      case E:
      case SE:
        return left + 1;
      case NW:
      case W:
      case SW:
        return left - 1;
    }
    return left;
  }

  public static int moveTop(Bearing b, int top) {
    switch (b) {
      case NE:
      case N:
      case NW:
        return top - 1;
      case SE:
      case S:
      case SW:
        return top + 1;
    }
    return top;
  }
  
}
