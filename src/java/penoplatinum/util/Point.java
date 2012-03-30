package penoplatinum.util;

/**
 * This class represents a point in 2D space.
 * Note: NOT IMMUTABLE
 * 
 * @author Team Platinum
 */


public class Point {

  private int x, y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() { return this.x; }
  public int getY() { return this.y; }

  public Point translate(int dx, int dy) {
    this.x += dx;
    this.y += dy;
    return this;
  }
  
  public Point rotate(Rotation rotation) {
    int oldX = this.x, oldY = this.y;

    switch(rotation.min()) {
      case L90:
        this.x = -1 * oldY;
        this.y =      oldX;
        break;
      case R90:
        this.x =      oldY;
        this.y = -1 * oldX;
        break;
      case R180:
      case L180:
        this.x = -1 * oldX;
        this.y = -1 * oldY;
        break;
    }
    return this;
  }
  
  public String toString() {
    return this.x + "," + this.y;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Point)) {
      return false;
    }
    if (((Point) obj).getX() != getX()) {
      return false;
    }
    if (((Point) obj).getY() != getY()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return x * 43 + y * 113;
  }
}
