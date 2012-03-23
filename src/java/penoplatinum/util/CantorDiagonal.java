package penoplatinum.util;

public class CantorDiagonal {
  
  public static void main(String[] args){
    test();
  }

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

  public static void test() {
    boolean win = true;
    win &= 0 == transform(0, 0);
    win &= 1 == transform(1, 0);
    win &= 2 == transform(1, 1);
    win &= 3 == transform(0, 1);
    win &= 4 == transform(-1, 1);
    win &= 5 == transform(-1, 0);
    win &= 6 == transform(-1, -1);
    win &= 7 == transform(0, -1);
    win &= 8 == transform(1, -1);
    win &= 9 == transform(2, -1);
    
    win &= 67 == transform(-4, 1);
    win &= 90 == transform(5,5);
    win &= 120 == transform(5,-5);
    
    System.out.println(win);
    
    
  }
}
