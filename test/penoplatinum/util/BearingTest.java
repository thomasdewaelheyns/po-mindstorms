package penoplatinum.util;

/**
 * BearingTest
 * 
 * Tests Bearing enumeration
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 


public class BearingTest extends TestCase {

  public BearingTest(String name) { 
    super(name);
  }
 
  public void testBearingReversal() {
    assertEquals("Reverse of N should be S", Bearing.S, Bearing.N.reverse());
    assertEquals("Reverse of E should be W", Bearing.W, Bearing.E.reverse());
    assertEquals("Reverse of S should be N", Bearing.N, Bearing.S.reverse());
    assertEquals("Reverse of E should be E", Bearing.E, Bearing.W.reverse());

    assertEquals("Reverse of NE should be SW", Bearing.SW, Bearing.NE.reverse());
    assertEquals("Reverse of SE should be NW", Bearing.NW, Bearing.SE.reverse());
    assertEquals("Reverse of SW should be NE", Bearing.NE, Bearing.SW.reverse());
    assertEquals("Reverse of NW should be SE", Bearing.SE, Bearing.NW.reverse());
  }

  public void testLeftFrom() {
    assertEquals("Left from N should W",   Bearing.W,  Bearing.N .leftFrom() );
    assertEquals("Left from NE should NW", Bearing.NW, Bearing.NE.leftFrom() );
    assertEquals("Left from E should N",   Bearing.N,  Bearing.E .leftFrom() );
    assertEquals("Left from SE should NE", Bearing.NE, Bearing.SE.leftFrom() );
    assertEquals("Left from S should E",   Bearing.E,  Bearing.S .leftFrom() );
    assertEquals("Left from SW should SE", Bearing.SE, Bearing.SW.leftFrom() );
    assertEquals("Left from W should S",   Bearing.S,  Bearing.W .leftFrom() );
    assertEquals("Left from NW should SW", Bearing.SW, Bearing.NW.leftFrom() );
  }

  public void testRightFrom() {
    assertEquals("Right from N should E",   Bearing.E,  Bearing.N .rightFrom() );
    assertEquals("Right from NE should SE", Bearing.SE, Bearing.NE.rightFrom() );
    assertEquals("Right from E should S",   Bearing.S,  Bearing.E .rightFrom() );
    assertEquals("Right from SE should SW", Bearing.SW, Bearing.SE.rightFrom() );
    assertEquals("Right from S should W",   Bearing.W,  Bearing.S .rightFrom() );
    assertEquals("Right from SW should NW", Bearing.NW, Bearing.SW.rightFrom() );
    assertEquals("Right from W should N",   Bearing.N,  Bearing.W. rightFrom() );
    assertEquals("Right from NW should NE", Bearing.NE, Bearing.NW.rightFrom() );
  }
  
  public void testLeftNeighbour() {
    assertEquals("Left neighbour of N should be W", Bearing.W, Bearing.N.getLeftNeighbour());
    assertEquals("Left neighbour of E should be N", Bearing.N, Bearing.E.getLeftNeighbour());
    assertEquals("Left neighbour of S should be W", Bearing.W, Bearing.S.getLeftNeighbour());
    assertEquals("Left neighbour of W should be N", Bearing.N, Bearing.W.getLeftNeighbour());
  }

  public void testRightNeighbour() {
    assertEquals("Right neighbour of N should be E", Bearing.E, Bearing.N.getRightNeighbour());
    assertEquals("Right neighbour of E should be S", Bearing.S, Bearing.E.getRightNeighbour());
    assertEquals("Right neighbour of S should be E", Bearing.E, Bearing.S.getRightNeighbour());
    assertEquals("Right neighbour of W should be S", Bearing.S, Bearing.W.getRightNeighbour());
  }
  
  public void testRotation() {
    assertEquals("N + NONE = N", Bearing.N, Bearing.N.rotate(Rotation.NONE) );
    assertEquals("N + L90  = W", Bearing.W, Bearing.N.rotate(Rotation.L90 ) );
    assertEquals("N + L180 = S", Bearing.S, Bearing.N.rotate(Rotation.L180) );
    assertEquals("N + L270 = E", Bearing.E, Bearing.N.rotate(Rotation.L270) );
    assertEquals("N + L360 = N", Bearing.N, Bearing.N.rotate(Rotation.L360) );

    assertEquals("N + R90  = E", Bearing.E, Bearing.N.rotate(Rotation.R90 ) );
    assertEquals("N + R180 = S", Bearing.S, Bearing.N.rotate(Rotation.R180) );
    assertEquals("N + R270 = W", Bearing.W, Bearing.N.rotate(Rotation.R270) );
    assertEquals("N + R360 = N", Bearing.N, Bearing.N.rotate(Rotation.R360) );

    assertEquals("E + NONE = E", Bearing.E, Bearing.E.rotate(Rotation.NONE) );
    assertEquals("E + L90  = N", Bearing.N, Bearing.E.rotate(Rotation.L90 ) );
    assertEquals("E + L180 = W", Bearing.W, Bearing.E.rotate(Rotation.L180) );
    assertEquals("E + L270 = S", Bearing.S, Bearing.E.rotate(Rotation.L270) );
    assertEquals("E + L360 = E", Bearing.E, Bearing.E.rotate(Rotation.L360) );

    assertEquals("E + R90  = S", Bearing.S, Bearing.E.rotate(Rotation.R90 ) );
    assertEquals("E + R180 = W", Bearing.W, Bearing.E.rotate(Rotation.R180) );
    assertEquals("E + R270 = N", Bearing.N, Bearing.E.rotate(Rotation.R270) );
    assertEquals("E + R360 = E", Bearing.E, Bearing.E.rotate(Rotation.R360) );

    assertEquals("S + NONE = S", Bearing.S, Bearing.S.rotate(Rotation.NONE) );
    assertEquals("S + L90  = E", Bearing.E, Bearing.S.rotate(Rotation.L90 ) );
    assertEquals("S + L180 = N", Bearing.N, Bearing.S.rotate(Rotation.L180) );
    assertEquals("S + L270 = W", Bearing.W, Bearing.S.rotate(Rotation.L270) );
    assertEquals("S + L360 = S", Bearing.S, Bearing.S.rotate(Rotation.L360) );

    assertEquals("S + R90  = W", Bearing.W, Bearing.S.rotate(Rotation.R90 ) );
    assertEquals("S + R180 = N", Bearing.N, Bearing.S.rotate(Rotation.R180) );
    assertEquals("S + R270 = E", Bearing.E, Bearing.S.rotate(Rotation.R270) );
    assertEquals("S + R360 = S", Bearing.S, Bearing.S.rotate(Rotation.R360) );

    assertEquals("W + NONE = W", Bearing.W, Bearing.W.rotate(Rotation.NONE) );
    assertEquals("W + L90  = S", Bearing.S, Bearing.W.rotate(Rotation.L90 ) );
    assertEquals("W + L180 = E", Bearing.E, Bearing.W.rotate(Rotation.L180) );
    assertEquals("W + L270 = N", Bearing.N, Bearing.W.rotate(Rotation.L270) );
    assertEquals("W + L360 = W", Bearing.W, Bearing.W.rotate(Rotation.L360) );

    assertEquals("W + R90  = N", Bearing.N, Bearing.W.rotate(Rotation.R90 ) );
    assertEquals("W + R180 = E", Bearing.E, Bearing.W.rotate(Rotation.R180) );
    assertEquals("W + R270 = S", Bearing.S, Bearing.W.rotate(Rotation.R270) );
    assertEquals("W + R360 = W", Bearing.W, Bearing.W.rotate(Rotation.R360) );
  }
}
