package penoplatinum.map;

public class Point {
  int x;
  int y;

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof Point)){
      return false;
    }
    if(((Point) obj).getX() != getX()){
      return false;
    }
    if(((Point) obj).getY() != getY()){
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return x*43+y*113;
  }
  
  
  
}
