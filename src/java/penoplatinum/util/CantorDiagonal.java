package penoplatinum.util;

public class CantorDiagonal {

  public static int transform(int x, int y) {
    int rand = Math.max(Math.abs(y), Math.abs(x));
    int start = (rand - 1) * rand * 4;
    int pos = 0;
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

  public static Point transform(int in) {
    if (in < 0) {
//      throw new RuntimeException("Only positive numbers");
      throw new RuntimeException();
    }
    if (in == 0) {
      return new Point(0, 0);
    }
    int rand = (int) ((1 + Math.sqrt(in)) / 2);
    int over = in - rand * (rand - 1) * 4;
    int zijde = (over - 1) / 2 / rand;
    int zijdeOver = over - zijde * 2 * rand;
    switch (zijde) {
      case 0:
        return new Point(rand, zijdeOver - rand);
      case 1:
        return new Point(rand - zijdeOver, rand);
      case 2:
        return new Point(-rand, rand - zijdeOver);
      case 3:
        return new Point(zijdeOver - rand, -rand);
    }
//    throw new RuntimeException("Cannot happen");
    throw new RuntimeException();

  }

  public static int transform(Point point) {
    int x = point.getX();
    int y = point.getY();
    return transform(x, y);

  }
}
