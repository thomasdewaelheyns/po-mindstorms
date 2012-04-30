package penoplatinum.util;

/**
 * TransformationTRTTest
 * 
 * Tests TransformationTRT class
 * 
 * @author: Team Platinum
 *
 * TODO: this test only tests 1 case with random values. more cases should
 *       be added, especially when a bug is found ;-)
 */
import junit.framework.*;

public class TransformationTRTTest extends TestCase {

  public TransformationTRTTest(String name) {
    super(name);
  }

  public void testTransform() {
    TransformationTRT transformation = this.createTransformation();
    Point point = this.createPoint();
    Point expectedPoint = this.createTransformedPoint();

    transformation.transform(point);

    assertEquals(expectedPoint, point);


  }

  public void testInverseTransform() {
    TransformationTRT transformation = createTransformation();
    Point point = createPoint();
    Point expectedPoint = this.createTransformedPoint();
    transformation.inverseTransform(expectedPoint);

    assertEquals(point, expectedPoint);
  }

  public void testInvert() {
    TransformationTRT transformation = createTransformation();
    transformation.invert();
    Point point = createPoint();
    Point expectedPoint = this.createTransformedPoint();
    transformation.transform(expectedPoint);

    assertEquals(point, expectedPoint);
  }

  public void testEquals() {
    TransformationTRT transformation = createTransformation();
    TransformationTRT transformation2 = createTransformation();
    assertEquals(transformation, transformation2);
  }

  private TransformationTRT createTransformation() {
    return new TransformationTRT().setTransformation(-1, -1,
            Rotation.L90,
            1, 2);
  }

  // we mock all other objects that are not the main scope of this test
  private Point createPoint() {
    return new Point(0, 0);
  }

  private Point createTransformedPoint() {
    return new Point(0, 3);
  }
}