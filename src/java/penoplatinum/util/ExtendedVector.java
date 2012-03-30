package penoplatinum.util;

/**
 * Vector that combines a position and an angular offset
 * WARNING: This class is not immutable!!!!
 * 
 * @author Team Platinum
 * 
 * TODO: THIS CLASS SHOULD BE REMOVED
 */

public class ExtendedVector {

  private float x;
  private float y;
  private float angle;


  public float getAngle() {
    return angle;
  }

  public void setAngle(float angle) {
    this.angle = angle;
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public void set(ExtendedVector other) {
    x = other.x;
    y = other.y;
    angle = other.angle;
  }

  public void negate() {
    x = -x;
    y = -y;
    angle = -angle;
  }

  public void add(ExtendedVector other) {
    x += other.x;
    y += other.y;
    angle += other.angle;
  }
}
