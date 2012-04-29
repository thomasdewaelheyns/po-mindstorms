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
  
  public void testInverseTransform()
  {
    fail();
  }
  
  public void testInvert()
  {
    fail();
  }
  
  private TransformationTRT createTransformation() {
    return new TransformationTRT().setTransformation( 123, -456,
                                                      Rotation.L270,
                                                      -656, 423 );
  }

  // we mock all other objects that are not the main scope of this test
  private Point createPoint() {
    return new Point(55, -757);
  }
  
  private Point createTransformedPoint() {
    return new Point(-1869, 245);
  }

}