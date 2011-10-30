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
    this.setupPosition();
    this.direction = 0;
    assertEquals("java.awt.Point[x=80,y=50]", this.findHitPoint().toString());
    this.direction = 20;
    assertEquals("java.awt.Point[x=80,y=39]", this.findHitPoint().toString());
    this.direction = 70;
    assertEquals("java.awt.Point[x=68,y=0]",  this.findHitPoint().toString());
    this.direction = 90;
    assertEquals("java.awt.Point[x=50,y=0]", this.findHitPoint().toString());
    this.direction = 110;
    assertEquals("java.awt.Point[x=31,y=0]",  this.findHitPoint().toString());
    this.direction = 160;
    assertEquals("java.awt.Point[x=0,y=31]",  this.findHitPoint().toString());
    this.direction = 180;
    assertEquals("java.awt.Point[x=0,y=50]", this.findHitPoint().toString());
    this.direction = 200;
    assertEquals("java.awt.Point[x=0,y=68]",  this.findHitPoint().toString());
    this.direction = 250;
    assertEquals("java.awt.Point[x=39,y=80]", this.findHitPoint().toString());
    this.direction = 270;
    assertEquals("java.awt.Point[x=50,y=80]", this.findHitPoint().toString());
    this.direction = 290;
    assertEquals("java.awt.Point[x=60,y=80]", this.findHitPoint().toString());
    this.direction = 340;
    assertEquals("java.awt.Point[x=80,y=60]", this.findHitPoint().toString());
    this.direction = 360;
    assertEquals("java.awt.Point[x=80,y=50]", this.findHitPoint().toString());
    this.direction = 380;
    assertEquals("java.awt.Point[x=80,y=39]", this.findHitPoint().toString());
  }

  public void testFacingWall() {
    this.setupPosition();
    this.direction = 0;
    assertEquals( "east",  this.findFacingWall() );
    this.direction = 20;
    assertEquals( "east",  this.findFacingWall() );
    this.direction = 70;
    assertEquals( "north", this.findFacingWall() );
    this.direction = 90;
    assertEquals( "north", this.findFacingWall() );
    this.direction = 110;
    assertEquals( "north", this.findFacingWall() );
    this.direction = 160;
    assertEquals( "west",  this.findFacingWall() );
    this.direction = 180;
    assertEquals( "west",  this.findFacingWall() );
    this.direction = 200;
    assertEquals( "west",  this.findFacingWall() );
    this.direction = 250;
    assertEquals( "south", this.findFacingWall() );
    this.direction = 270;
    assertEquals( "south", this.findFacingWall() );
    this.direction = 290;
    assertEquals( "south", this.findFacingWall() );
    this.direction = 340;
    assertEquals( "east",  this.findFacingWall() );
    this.direction = 360;
    assertEquals( "east",  this.findFacingWall() );
    this.direction = 380;
    assertEquals( "east",  this.findFacingWall() );
  }
  
  public void testDistance() {
    this.setupPosition();
    this.direction = 0;
    assertEquals( 30, this.findFrontFacingDistance() );
    this.direction = 20;
    assertEquals( 32, this.findFrontFacingDistance() );
    this.direction = 70;
    assertEquals( 53, this.findFrontFacingDistance() );
    this.direction = 90;
    assertEquals( 50, this.findFrontFacingDistance() );
    this.direction = 110;
    assertEquals( 53, this.findFrontFacingDistance() );
    this.direction = 160;
    assertEquals( 53, this.findFrontFacingDistance() );
    this.direction = 180;
    assertEquals( 50, this.findFrontFacingDistance() );
    this.direction = 200;
    assertEquals( 53, this.findFrontFacingDistance() );
    this.direction = 250;
    assertEquals( 32, this.findFrontFacingDistance() );
    this.direction = 270;
    assertEquals( 30, this.findFrontFacingDistance() );
    this.direction = 290;
    assertEquals( 32, this.findFrontFacingDistance() );
    this.direction = 340;
    assertEquals( 32, this.findFrontFacingDistance() );
    this.direction = 360;
    assertEquals( 30, this.findFrontFacingDistance() );
  }

  private void setupPosition() {
    this.positionX = 50;
    this.positionY = 50;
  }

  private double T( double x, double d ) {
    /**
     * Geonometry used:
     *
     *            +
     *          / |
     *        /   |  Y
     *      / a   |
     *    +-------+
     *        X
     *
     *    tan(a) = Y/X    =>   Y = tan(a) * X     ||   X = Y / tan(a)
     */
    return x * Math.tan(Math.toRadians(d));
  }
  
  private Point findHitPoint() {
    return this.findHitPoint( this.positionX, this.positionY );
  }

  private Point findHitPoint( int X, int Y ) {
    double angle   = ( this.direction % 360 );
    double x, y;
    double dx, dy;

    if( angle <= 90 ) {
      dx = 80 - X;
      dy = this.T( dx, angle );
      if( dy > Y ) {
        dy = Y;
        dx = this.T( dy, 90 - angle );
      }
      x = X + dx;
      y = Y - dy;
    } else if( angle > 90 && angle <= 180 ) {
      dx = X;
      dy = this.T( dx, 180-angle );
      if( dy > Y ) {
        dy = Y;
        dx = this.T( dy, angle - 90 );
      }
      x = X - dx;
      y = Y - dy;
    } else if( angle > 180 && angle <= 270 ) {
      dx = X;
      dy = this.T( dx, angle - 180 );
      if( dy > ( 80 - Y ) ) {
        dy = ( 80 - Y );
        dx = this.T( dy, 270 - angle );
      }
      x = X - dx;
      y = Y + dy;
    } else { 
      // angle > 270 && angle < 360
      dx = 80 - X;
      dy = this.T( dx, 360 - angle );
      if( dy > ( 80 - Y ) ) {
        dy = ( 80 - Y );
        dx = this.T( dy, angle - 270 );
      }
      x = X + dx;
      y = Y + dy;
    }
    
    return new Point((int)x,(int)y);
  }

  private String findFacingWall() {
    Point hit = this.findHitPoint();
    String result = "unknown";
    
    if( hit.x == 0 ) {
      result = "west";
    } else if( hit.x == 80 ) {
      result = "east";
    } else if( hit.y == 0 ) {
      result = "north";
    } else if( hit.y == 80 ) {
      result = "south";
    } else {
      result = "impossible";
    }

    return result;
  }
  
  private int findFrontFacingDistance() {
    Point hit = this.findHitPoint();
    return (int)Math.round( Math.sqrt( Math.pow(hit.x - this.positionX, 2 ) + 
                                       Math.pow(hit.y - this.positionY, 2 )));
  }

}
