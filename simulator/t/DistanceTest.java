import junit.framework.*; 

import java.awt.Point;

public class DistanceTest extends TestCase { 
  private Tile tile;
  private int positionX;
  private int positionY;
  private int direction;

  public DistanceTest(String name) { 
    super(name);
  }
  
  public void testHitPoint() {
    assertEquals( "java.awt.Point[x=80,y=50]", 
                  Tile.findHitPoint(50, 50,   0, 80).toString() );
    assertEquals( "java.awt.Point[x=80,y=39]", 
                  Tile.findHitPoint(50, 50,  20, 80).toString() );
    assertEquals( "java.awt.Point[x=68,y=0]",
                  Tile.findHitPoint(50, 50,  70, 80).toString() );
    assertEquals( "java.awt.Point[x=50,y=0]",
                  Tile.findHitPoint(50, 50,  90, 80).toString() );
    assertEquals( "java.awt.Point[x=31,y=0]",
                  Tile.findHitPoint(50, 50, 110, 80).toString() );
    assertEquals( "java.awt.Point[x=0,y=31]",
                  Tile.findHitPoint(50, 50, 160, 80).toString() );
    assertEquals( "java.awt.Point[x=0,y=50]",
                  Tile.findHitPoint(50, 50, 180, 80).toString() );
    assertEquals( "java.awt.Point[x=0,y=68]",
                  Tile.findHitPoint(50, 50, 200, 80).toString() );
    assertEquals( "java.awt.Point[x=39,y=80]",
                  Tile.findHitPoint(50, 50, 250, 80).toString() );
    assertEquals( "java.awt.Point[x=50,y=80]",
                  Tile.findHitPoint(50, 50, 270, 80).toString() );
    assertEquals( "java.awt.Point[x=60,y=80]",
                  Tile.findHitPoint(50, 50, 290, 80).toString() );
    assertEquals( "java.awt.Point[x=80,y=60]",
                  Tile.findHitPoint(50, 50, 340, 80).toString() );
    assertEquals( "java.awt.Point[x=80,y=50]",
                  Tile.findHitPoint(50, 50, 360, 80).toString() );
    assertEquals( "java.awt.Point[x=80,y=39]", 
                  Tile.findHitPoint(50, 50, 380, 80).toString() );
  }

  public void testFacingWall() {
    assertEquals( Baring.E, this.findFacingWall(50, 50,   0 ) );
    assertEquals( Baring.E, this.findFacingWall(50, 50,  20 ) );
    assertEquals( Baring.N, this.findFacingWall(50, 50,  70 ) );
    assertEquals( Baring.N, this.findFacingWall(50, 50,  90 ) );
    assertEquals( Baring.N, this.findFacingWall(50, 50, 110 ) );
    assertEquals( Baring.W, this.findFacingWall(50, 50, 160 ) );
    assertEquals( Baring.W, this.findFacingWall(50, 50, 180 ) );
    assertEquals( Baring.W, this.findFacingWall(50, 50, 200 ) );
    assertEquals( Baring.S, this.findFacingWall(50, 50, 250 ) );
    assertEquals( Baring.S, this.findFacingWall(50, 50, 270 ) );
    assertEquals( Baring.S, this.findFacingWall(50, 50, 290 ) );
    assertEquals( Baring.E, this.findFacingWall(50, 50, 340 ) );
    assertEquals( Baring.E, this.findFacingWall(50, 50, 360 ) );
    assertEquals( Baring.E, this.findFacingWall(50, 50, 380 ) );
  }
  
  private int findFacingWall( int x, int y, int angle ) {
    Point hit = Tile.findHitPoint( x, y, angle, 80 );
    return Tile.getHitWall( hit, 80 );
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
    Point hit = Tile.findHitPoint( x, y, angle, 80 );
    return Tile.getDistance( x, y, hit );
  }

}
