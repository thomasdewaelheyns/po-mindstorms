/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import penoplatinum.util.Bearing;
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
    atBearing = mapBearing(atBearing);
    return s.hasNeighbour(atBearing);
  }

  @Override
  public Sector getNeighbour(Bearing atBearing) {
    atBearing = mapBearing(atBearing);
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
    atBearing = mapBearing(atBearing);
    s.setWall(atBearing);
    return this;
  }

  @Override
  public Sector setNoWall(Bearing atBearing) {
    atBearing = mapBearing(atBearing);
    s.setNoWall(atBearing);
    return this;
  }

  @Override
  public Sector clearWall(Bearing atBearing) {
    atBearing = mapBearing(atBearing);
    s.clearWall(atBearing);
    return this;
  }

  @Override
  public boolean hasWall(Bearing atBearing) {
    atBearing = mapBearing(atBearing);
    return s.hasWall(atBearing);
  }

  @Override
  public boolean hasNoWall(Bearing atBearing) {
    atBearing = mapBearing(atBearing);
    return s.hasNoWall(atBearing);
  }

  @Override
  public boolean knowsWall(Bearing atBearing) {
    atBearing = mapBearing(atBearing);
    return s.knowsWall(atBearing);
  }

  @Override
  public boolean hasSameWallsAs(Sector s) {
     for (Bearing b : Bearing.NESW) {
      if (knowsWall(b) != s.knowsWall(b))
        return false;
      if (!knowsWall(b))
        continue;
      if (hasWall(b) != s.hasWall(b))
        return false;
    }
    return true;
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
    atBearing = mapBearing(atBearing);
    return s.givesAccessTo(atBearing);
  }

  @Override
  public boolean equals(Object obj) {
    if (! (obj instanceof TransformedSector))
      return super.equals(obj);
    
    return ((TransformedSector)obj).s.equals(s) && ((TransformedSector)obj).transformation.equals(transformation);
  }
  
  private Bearing mapBearing(Bearing atBearing)
  {
    return atBearing.rotate(transformation.getRotation().invert());
  }
  
  
}
