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
}
