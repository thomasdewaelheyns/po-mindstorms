package penoplatinum.simulator.tiles;

import junit.framework.*; 

import java.awt.Point;
import penoplatinum.simulator.tiles.TileGeometry;
import penoplatinum.util.Bearing;

public class DistanceTest extends TestCase { 
  private int positionX;
  private int positionY;
  private int direction;

  public DistanceTest(String name) { 
    super(name);
  }
  
  public void testHitPoint() {
    assertEquals( "java.awt.Point[x=80,y=50]", 
                  TileGeometry.findHitPoint(50, 50,   0, 80).toString() );
    assertEquals( "java.awt.Point[x=80,y=39]", 
                  TileGeometry.findHitPoint(50, 50,  20, 80).toString() );
    assertEquals( "java.awt.Point[x=68,y=0]",
                  TileGeometry.findHitPoint(50, 50,  70, 80).toString() );
    assertEquals( "java.awt.Point[x=50,y=0]",
                  TileGeometry.findHitPoint(50, 50,  90, 80).toString() );
    assertEquals( "java.awt.Point[x=31,y=0]",
                  TileGeometry.findHitPoint(50, 50, 110, 80).toString() );
    assertEquals( "java.awt.Point[x=0,y=31]",
                  TileGeometry.findHitPoint(50, 50, 160, 80).toString() );
    assertEquals( "java.awt.Point[x=0,y=50]",
                  TileGeometry.findHitPoint(50, 50, 180, 80).toString() );
    assertEquals( "java.awt.Point[x=0,y=68]",
                  TileGeometry.findHitPoint(50, 50, 200, 80).toString() );
    assertEquals( "java.awt.Point[x=39,y=80]",
                  TileGeometry.findHitPoint(50, 50, 250, 80).toString() );
    assertEquals( "java.awt.Point[x=50,y=80]",
                  TileGeometry.findHitPoint(50, 50, 270, 80).toString() );
    assertEquals( "java.awt.Point[x=60,y=80]",
                  TileGeometry.findHitPoint(50, 50, 290, 80).toString() );
    assertEquals( "java.awt.Point[x=80,y=60]",
                  TileGeometry.findHitPoint(50, 50, 340, 80).toString() );
    assertEquals( "java.awt.Point[x=80,y=50]",
                  TileGeometry.findHitPoint(50, 50, 360, 80).toString() );
    assertEquals( "java.awt.Point[x=80,y=39]", 
                  TileGeometry.findHitPoint(50, 50, 380, 80).toString() );
  }

  public void testFacingWall() {
    assertEquals( Panel.mapBearingToInt(Bearing.E), this.findFacingWall(50, 50,   0 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.E), this.findFacingWall(50, 50,  20 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.N), this.findFacingWall(50, 50,  70 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.N), this.findFacingWall(50, 50,  90 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.N), this.findFacingWall(50, 50, 110 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.W), this.findFacingWall(50, 50, 160 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.W), this.findFacingWall(50, 50, 180 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.W), this.findFacingWall(50, 50, 200 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.S), this.findFacingWall(50, 50, 250 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.S), this.findFacingWall(50, 50, 270 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.S), this.findFacingWall(50, 50, 290 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.E), this.findFacingWall(50, 50, 340 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.E), this.findFacingWall(50, 50, 360 ) );
    assertEquals( Panel.mapBearingToInt(Bearing.E), this.findFacingWall(50, 50, 380 ) );
  }
  
  private int findFacingWall( int x, int y, int angle ) {
    Point hit = TileGeometry.findHitPoint( x, y, angle, 80 );
    return TileGeometry.getHitWall( hit, 80, angle );
  }
  
  public void testDistance() {
    assertEquals( 30, (int)this.findFrontFacingDistance(50, 50,   0) );
    assertEquals( 31, (int)this.findFrontFacingDistance(50, 50,  20) );
    assertEquals( 53, (int)this.findFrontFacingDistance(50, 50,  70) );
    assertEquals( 50, (int)this.findFrontFacingDistance(50, 50,  90) );
    assertEquals( 53, (int)this.findFrontFacingDistance(50, 50, 110) );
    assertEquals( 53, (int)this.findFrontFacingDistance(50, 50, 160) );
    assertEquals( 50, (int)this.findFrontFacingDistance(50, 50, 180) );
    assertEquals( 53, (int)this.findFrontFacingDistance(50, 50, 200) );
    assertEquals( 31, (int)this.findFrontFacingDistance(50, 50, 250) );
    assertEquals( 30, (int)this.findFrontFacingDistance(50, 50, 270) );
    assertEquals( 31, (int)this.findFrontFacingDistance(50, 50, 290) );
    assertEquals( 31, (int)this.findFrontFacingDistance(50, 50, 340) );
    assertEquals( 30, (int)this.findFrontFacingDistance(50, 50, 360) );
  }
  
  private double findFrontFacingDistance( int x, int y, int angle ) {
    Point hit = TileGeometry.findHitPoint( x, y, angle, 80 );
    return TileGeometry.getDistance( x, y, hit );
  }

}
