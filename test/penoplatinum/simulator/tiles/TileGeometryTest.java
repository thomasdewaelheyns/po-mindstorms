package penoplatinum.simulator.tiles;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class TileGeometryTest {

  /**
   * Test of findHitPoint method, of class TileGeometry.
   */
  @Test
  public void testFindHitPoint() {
    System.out.println("findHitPoint");
    double X = 12.0;
    double Y = 16.0;
    double angle = 0.0;
    int size = 40;
    Point result = TileGeometry.findHitPoint(X, Y, angle, size);
    assertEquals(new Point(40, 16), result);

    X = 0.0; //
    Y = 16.0;
    angle = 53.13;
    result = TileGeometry.findHitPoint(X, Y, angle, size);
    assertEquals(new Point(12, 0), result);

    X = 0.0;
    Y = 40.0;
    angle = 45.0;
    result = TileGeometry.findHitPoint(X, Y, angle, size);
    assertEquals(new Point(40, 0), result);

    X = 24.0;
    Y = 0.0;
    angle = -45.0;
    result = TileGeometry.findHitPoint(X, Y, angle, size);
    assertEquals(new Point(40, 16), result); //15.9999 rounded up
    
    X = 40.0;
    Y = 24.0;
    angle = 225.0;
    result = TileGeometry.findHitPoint(X, Y, angle, size);
    assertEquals(new Point(24, 40), result);
    
    X = 24.0;
    Y = 40.0;
    angle = 135.0;
    result = TileGeometry.findHitPoint(X, Y, angle, size);
    assertEquals(new Point(0, 16), result);
  }

  /**
   * Test of getDistance method, of class TileGeometry.
   */
  @Test
  public void testGetDistance() {
    System.out.println("getDistance");
    double x = 0.0;
    double y = 16.0;
    double angle = 53.13;
    Point hit = TileGeometry.findHitPoint(x, y, angle, 40);
    assertEquals(new Point(12, 0), hit);
    double expResult = 20.0;
    double result = TileGeometry.getDistance(x, y, hit);
    assertEquals(expResult, result, 0.0001);
  }

  /**
   * Test of getHitWall method, of class TileGeometry.
   */
  @Test
  public void testGetHitWall() {
    System.out.println("getHitWall");
    int size = 40;
    assertEquals(Bearing.W, TileGeometry.getHitWall(new Point(0, 10) , size, 0.0  ));
    assertEquals(Bearing.N, TileGeometry.getHitWall(new Point(10, 0) , size, 90.0 ));
    assertEquals(Bearing.E, TileGeometry.getHitWall(new Point(40, 10), size, 180.0));
    assertEquals(Bearing.S, TileGeometry.getHitWall(new Point(10, 40), size, 270.0));
    
    assertEquals(Bearing.NW, TileGeometry.getHitWall(new Point(0, 0)  , size, 135.0));
    assertEquals(Bearing.NE, TileGeometry.getHitWall(new Point(40, 0) , size, 45.0 ));
    assertEquals(Bearing.SE, TileGeometry.getHitWall(new Point(40, 40), size, 315.0));
    assertEquals(Bearing.SW, TileGeometry.getHitWall(new Point(0, 40) , size, 225.0));
    
    assertEquals(Bearing.W, TileGeometry.getHitWall(new Point(0, 0)  , size, 180.0));
    assertEquals(Bearing.E, TileGeometry.getHitWall(new Point(40, 0) , size, 0.0 ));
    assertEquals(Bearing.E, TileGeometry.getHitWall(new Point(40, 40), size, 0.0));
    assertEquals(Bearing.W, TileGeometry.getHitWall(new Point(0, 40) , size, 180.0));
    
    assertEquals(Bearing.N, TileGeometry.getHitWall(new Point(0, 0)  , size, 90.0));
    assertEquals(Bearing.N, TileGeometry.getHitWall(new Point(40, 0) , size, 90.0 ));
    assertEquals(Bearing.S, TileGeometry.getHitWall(new Point(40, 40), size, 270.0));
    assertEquals(Bearing.S, TileGeometry.getHitWall(new Point(0, 40) , size, 270.0));
  }
}
