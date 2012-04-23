package penoplatinum.simulator.tiles;

import junit.framework.*;

import penoplatinum.simulator.tiles.TileGeometry;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class DistanceTest extends TestCase {

  private int positionX;

  private int positionY;

  private int direction;

  public DistanceTest(String name) {
    super(name);
  }

  public void testHitPoint() {
    assertEquals( new Point(80,50).toString(),
            TileGeometry.findHitPoint(50, 50, 0, 80).toString());
    assertEquals(new Point(80,39).toString(),
            TileGeometry.findHitPoint(50, 50, 20, 80).toString());
    assertEquals(new Point(68,0).toString(),
            TileGeometry.findHitPoint(50, 50, 70, 80).toString());
    assertEquals(new Point(50,0).toString(),
            TileGeometry.findHitPoint(50, 50, 90, 80).toString());
    assertEquals(new Point(32,0).toString(),
            TileGeometry.findHitPoint(50, 50, 110, 80).toString());
    assertEquals(new Point(0,32).toString(),
            TileGeometry.findHitPoint(50, 50, 160, 80).toString());
    assertEquals(new Point(0,50).toString(),
            TileGeometry.findHitPoint(50, 50, 180, 80).toString());
    assertEquals(new Point(0,68).toString(),
            TileGeometry.findHitPoint(50, 50, 200, 80).toString());
    assertEquals(new Point(39,80).toString(),
            TileGeometry.findHitPoint(50, 50, 250, 80).toString());
    assertEquals(new Point(50,80).toString(),
            TileGeometry.findHitPoint(50, 50, 270, 80).toString());
    assertEquals(new Point(61,80).toString(),
            TileGeometry.findHitPoint(50, 50, 290, 80).toString());
    assertEquals(new Point(80,61).toString(),
            TileGeometry.findHitPoint(50, 50, 340, 80).toString());
    assertEquals(new Point(80,50).toString(),
            TileGeometry.findHitPoint(50, 50, 360, 80).toString());
    assertEquals(new Point(80,39).toString(),
            TileGeometry.findHitPoint(50, 50, 380, 80).toString());
  }

  public void testFacingWall() {
    assertEquals(Bearing.E, this.findFacingWall(50, 50, 0));
    assertEquals(Bearing.E, this.findFacingWall(50, 50, 20));
    assertEquals(Bearing.N, this.findFacingWall(50, 50, 70));
    assertEquals(Bearing.N, this.findFacingWall(50, 50, 90));
    assertEquals(Bearing.N, this.findFacingWall(50, 50, 110));
    assertEquals(Bearing.W, this.findFacingWall(50, 50, 160));
    assertEquals(Bearing.W, this.findFacingWall(50, 50, 180));
    assertEquals(Bearing.W, this.findFacingWall(50, 50, 200));
    assertEquals(Bearing.S, this.findFacingWall(50, 50, 250));
    assertEquals(Bearing.S, this.findFacingWall(50, 50, 270));
    assertEquals(Bearing.S, this.findFacingWall(50, 50, 290));
    assertEquals(Bearing.E, this.findFacingWall(50, 50, 340));
    assertEquals(Bearing.E, this.findFacingWall(50, 50, 360));
    assertEquals(Bearing.E, this.findFacingWall(50, 50, 380));
  }

  private Bearing findFacingWall(int x, int y, int angle) {
    Point hit = TileGeometry.findHitPoint(x, y, angle, 80);
    return TileGeometry.getHitWall(hit, 80, angle);
  }

  public void testDistance() {
    assertEquals(30, (int) this.findFrontFacingDistance(50, 50, 0));
    assertEquals(31, (int) this.findFrontFacingDistance(50, 50, 20));
    assertEquals(53, (int) this.findFrontFacingDistance(50, 50, 70));
    assertEquals(50, (int) this.findFrontFacingDistance(50, 50, 90));
    assertEquals(53, (int) this.findFrontFacingDistance(50, 50, 110));
    assertEquals(53, (int) this.findFrontFacingDistance(50, 50, 160));
    assertEquals(50, (int) this.findFrontFacingDistance(50, 50, 180));
    assertEquals(53, (int) this.findFrontFacingDistance(50, 50, 200));
    assertEquals(31, (int) this.findFrontFacingDistance(50, 50, 250));
    assertEquals(30, (int) this.findFrontFacingDistance(50, 50, 270));
    assertEquals(31, (int) this.findFrontFacingDistance(50, 50, 290));
    assertEquals(31, (int) this.findFrontFacingDistance(50, 50, 340));
    assertEquals(30, (int) this.findFrontFacingDistance(50, 50, 360));
  }

  private double findFrontFacingDistance(int x, int y, int angle) {
    Point hit = TileGeometry.findHitPoint(x, y, angle, 80);
    return TileGeometry.getDistance(x, y, hit);
  }
}
