/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import org.mockito.internal.stubbing.answers.Returns;
import penoplatinum.util.Bearing;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

/**
 *
 * @author MHGameWork
 */
public class TransformedSector implements Sector {

  private final Sector s;
  private final TransformationTRT transformation;

  TransformedSector(Sector s, TransformationTRT transformation) {
    this.s = s;
    this.transformation = transformation;
  }

  public Sector getDecoratedSector() {
    return s;
  }
  
  

  @Override
  public Sector putOn(Grid grid) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Grid getGrid() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Sector addNeighbour(Sector neighbour, Bearing atBearing) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean hasNeighbour(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    return s.hasNeighbour(atBearing);
  }

  @Override
  public Sector getNeighbour(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    Sector n = s.getNeighbour(atBearing);
    return new TransformedSector(n, transformation);
  }

  @Override
  public Sector setValue(int value) {
    s.setValue(value);
    return this;
  }

  @Override
  public int getValue() {
    return s.getValue();
  }

  @Override
  public Sector setWall(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    s.setWall(atBearing);
    return this;
  }

  @Override
  public Sector setNoWall(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    s.setNoWall(atBearing);
    return this;
  }

  @Override
  public Sector clearWall(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    s.clearWall(atBearing);
    return this;
  }

  @Override
  public boolean hasWall(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    return s.hasWall(atBearing);
  }

  @Override
  public boolean hasNoWall(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    return s.hasNoWall(atBearing);
  }

  @Override
  public boolean knowsWall(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    return s.knowsWall(atBearing);
  }

  @Override
  public boolean hasSameWallsAs(Sector s) {
    return s.hasSameWallsAs(this.s);
  }

  @Override
  public boolean isFullyKnown() {
    return s.isFullyKnown();
  }

  @Override
  public Sector clearWalls() {
    s.clearWalls();
    return this;
  }

  @Override
  public boolean givesAccessTo(Bearing atBearing) {
    atBearing = atBearing.rotate(transformation.getRotation());
    return s.givesAccessTo(atBearing);
  }
}
