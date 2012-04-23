package penoplatinum.util;

public class CantorDiagonal {
  
  public static int transform(int x, int y) {
    int rand = Math.max(Math.abs(y), Math.abs(x));
    int start = (rand - 1) * rand * 4 + 1;
    int pos = -1;
    if (y > 0 && (Math.abs(x) < y || x == -y)) {
      pos += y - x;
      start += 2 * rand;
    } else if (y < 0 && (Math.abs(x) < -y || x == -y)) {
      pos += x - y;
      start += 6 * rand;
    } else if (x > 0 && (Math.abs(y) < x || x == y)) {
      pos += x + y;
      start += 0 * rand;
    } else if (x < 0 && (Math.abs(y) < -x || x == y)) {
      pos += -x - y;
      start += 4 * rand;
    }
    return start + pos;
  }
  public static int transform(Point point){
    int x = point.getX();
    int y = point.getY();
    return transform(x,y);
    
  }

}
