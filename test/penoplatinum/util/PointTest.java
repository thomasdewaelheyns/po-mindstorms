package penoplatinum.util;

/**
 * PointTest
 * 
 * Tests Point class
 * 
 * @author: Team Platinum
 */
import junit.framework.*;

public class PointTest extends TestCase {
  
  private int X = 123,
          Y = 456;
  
  public PointTest(String name) {    
    super(name);
  }
  
  public void testGetters() {
    Point point = this.createPoint();
    
    assertEquals(X, point.getX());
    assertEquals(Y, point.getY());
  }
  
  public void testToString() {
    assertEquals(X + "," + Y, this.createPoint().toString());
  }
  
  public void testTranslation() {
    Point point = this.createPoint().translate(-999, -999);
    
    assertEquals(-876, point.getX());
    assertEquals(-543, point.getY());
  }
  
  public void testRotation() {
    Point pointNONE = this.createPoint().rotate(Rotation.NONE);
    Point pointL90 = this.createPoint().rotate(Rotation.L90);
    Point pointR90 = this.createPoint().rotate(Rotation.R90);
    Point pointL180 = this.createPoint().rotate(Rotation.L180);
    Point pointR180 = this.createPoint().rotate(Rotation.R180);
    Point pointL270 = this.createPoint().rotate(Rotation.L270);
    Point pointR270 = this.createPoint().rotate(Rotation.R270);
    Point pointL360 = this.createPoint().rotate(Rotation.L360);
    Point pointR360 = this.createPoint().rotate(Rotation.R360);
    
    assertEquals(X, pointNONE.getX());
    assertEquals(Y, pointNONE.getY());
    
    assertEquals(-1 * Y, pointL90.getX());
    assertEquals(X, pointL90.getY());
    
    assertEquals(-1 * X, pointL180.getX());
    assertEquals(-1 * Y, pointL180.getY());
    
    assertEquals(Y, pointL270.getX());
    assertEquals(-1 * X, pointL270.getY());
    
    assertEquals(X, pointL360.getX());
    assertEquals(Y, pointL360.getY());
    
    assertEquals(Y, pointR90.getX());
    assertEquals(-1 * X, pointR90.getY());
    
    assertEquals(-1 * X, pointR180.getX());
    assertEquals(-1 * Y, pointR180.getY());
    
    assertEquals(-1 * Y, pointR270.getX());
    assertEquals(X, pointR270.getY());
    
    assertEquals(X, pointR360.getX());
    assertEquals(Y, pointR360.getY());
  }
  
  public void testEquals() {
    Point point1 = this.createPoint(),
            point2 = this.createPoint(),
            point3 = this.createOtherPoint();
    
    assertTrue("identical points aren't equal.", point1.equals(point2));
    assertTrue("identical points aren't equal.", point2.equals(point1));
    
    assertFalse("different points are equal.", point1.equals(point3));
    assertFalse("different points are equal.", point2.equals(point3));
  }
  
  public void testHashCode() {
    assertEquals("hascode is wrong.", 56817, this.createPoint().hashCode());
  }
  
  public void testCopyConstructor() {
    Point p1 = new Point(4, 3);
    Point p2 = new Point(p1);
    assertEquals(p1, p2);
  }
  
  private Point createPoint() {
    return new Point(X, Y);
  }
  
  private Point createOtherPoint() {
    return new Point(789, 101112);
  }
}
