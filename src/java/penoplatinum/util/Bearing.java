package penoplatinum.util;

/**
 * Bearing
 * 
 * Enumeration of bearings with a few utility functions.
 * 
 * @author: Team Platinum
 */
import java.util.List;
import java.util.Arrays;

public enum Bearing {

  UNKNOWN(-1),
  N(0),
  NE(1),
  E(2),
  SE(3),
  S(4),
  SW(5),
  W(6),
  NW(7);
  public static final List<Bearing> NESW =
          Arrays.asList(new Bearing[]{Bearing.N, Bearing.E, Bearing.S, Bearing.W});
  private int bearing;

  private Bearing(int bearing) {
    this.bearing = bearing;
  }

  private int getValue() {
    return this.bearing;
  }

  private static Bearing get(int value) {
    switch (value) {
      case -1:
        return UNKNOWN;
      case 0:
        return N;
      case 1:
        return NE;
      case 2:
        return E;
      case 3:
        return SE;
      case 4:
        return S;
      case 5:
        return SW;
      case 6:
        return W;
      case 7:
        return NW;
    }
    throw new RuntimeException("" + value + " isn't a valid Bearing.");
  }

  // returns the reverse Bearing
  public Bearing reverse() {
    return Bearing.get( (this.bearing + 4) % 8);
  }

  // returns a Bearing in a 90 degree corner to the left
  public Bearing leftFrom() {
    return this.get(((this.bearing - 2) + 8) % 8);
  }

  // returns a Bearing in a 90 degree corner to the right
  public Bearing rightFrom() {
    return this.get(((this.bearing + 2) + 8) % 8);
  }

  // TODO: The following methods should be implemented differently or on
  //       a different class. Kept here to not break the code that uses them
  //       in a functional way
  // returns the neighbour line that influences a position in two ways
  public Bearing  getLeftNeighbour() {
    return this == N || this == S ? W : N;
  }

  // returns the neighbour line that influences a position in one ways
  public Bearing getRightNeighbour() {
    return this == N || this == S ? E : S;
  }

  // applies a rotation and returns the new Bearing
  public Bearing rotate(Rotation rotation) {
    switch (rotation.min()) {
      case L90:
        return this.leftFrom();
      case R90:
        return this.rightFrom();
      case L180:
      case R180:
        return this.reverse();
    }
    return this; // matches NONE
  }

  // returns the rotation needed to turn to the targetted bearing
  public Rotation to(Bearing target) {
    int diff = ((target.getValue() - this.getValue() + 8) % 8);
    switch (diff) {
      case 0:
        return Rotation.NONE;
      case 2:
        return Rotation.R90;
      case 4:
        return Rotation.R180;
      case 6:
        return Rotation.L90;
      default:
        throw new RuntimeException("Not implemented...");
    }
  }
}
        

/*
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!! No longer in use in the current codebase (Yeah!)

public static int withOrigin(int bearing, int origin);

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!! Is now implemented on Point

public static Point mapToNorth(int rotation, int x, int y);

   * /**/