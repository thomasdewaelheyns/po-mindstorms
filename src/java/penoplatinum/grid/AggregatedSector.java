/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Position;

/**
 *
 * @author MHGameWork
 */
public class AggregatedSector implements Sector {

  private final AggregatedGrid grid;
  private final Point position;

  AggregatedSector(AggregatedGrid grid, Point position) {
    if (position == null)
      throw new IllegalArgumentException();
    this.grid = grid;
    this.position = position;
  }

  public Point getPosition() {
    return position;
  }

  public AggregatedGrid getAggregatedGrid() {
    return grid;
  }

  @Override
  public Sector putOn(Grid grid) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Grid getGrid() {
    return this.grid;
  }

  @Override
  public boolean hasNeighbour(Bearing atBearing) {
    return getNeighbour(atBearing) != null;
  }

  @Override
  public Sector getNeighbour(Bearing atBearing) {
    Point p = this.grid.getPositionOf(this);
    int left = Position.moveLeft(atBearing, p.getX());
    int top = Position.moveTop(atBearing, p.getY());
    return this.grid.getSectorAt(new Point(left, top));
  }

  @Override
  public Sector setValue(int value) {
    for (int i = 0; i < grid.getActiveGrids().size(); i++) {
      Grid g = grid.getActiveGrids().get(i);
      Sector s = g.getSectorAt(position);
      if (s == null)
        continue;
      s.setValue(value);
    }

    return this;
  }

  @Override
  public int getValue() {
    for (int i = 0; i < grid.getActiveGrids().size(); i++) {
      Grid g = grid.getActiveGrids().get(i);
      Sector s = g.getSectorAt(position);
      if (s == null)
        continue;
      return s.getValue();
    }

    return -1;

  }

  @Override
  public Sector setWall(Bearing atBearing) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Sector setNoWall(Bearing atBearing) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Sector clearWall(Bearing atBearing) {
    for (int i = 0; i < grid.getActiveGrids().size(); i++) {
      Grid g = grid.getActiveGrids().get(i);
      Sector s = g.getSectorAt(position);
      if (s == null)
        continue;
      s.clearWall(atBearing);
    }
    return this;
  }

  @Override
  public boolean hasWall(Bearing wall) {
    int val = calculateWallValue(wall);
    if (val == 0)
      throw new UnsupportedOperationException();
    return val > 0;
  }

  @Override
  public boolean hasNoWall(Bearing wall) {
    final int val = calculateWallValue(wall);
    if (val == 0)
      throw new UnsupportedOperationException();
    return val < 0;
  }

  @Override
  public boolean knowsWall(Bearing atBearing) {
    return calculateWallValue(atBearing) != 0;
  }

  private int calculateWallValue(Bearing atBearing) {
    int ret = 0;
    for (int i = 0; i < grid.getActiveGrids().size(); i++) {
      Grid g = grid.getActiveGrids().get(i);
      int s = g.getSectorId(position);
      if (s == -1)
        continue;

      ret += !g.knowsWall(s, atBearing) ? 0 : (g.hasWall(s, atBearing) ? 1 : -1);

    }
    return ret;
  }

  @Override
  public boolean hasSameWallsAs(Sector s) {
    for (Bearing b : Bearing.NESW) {
      if (knowsWall(b) != s.knowsWall(b))
        return false;
      if (!knowsWall(b))
        continue;
      if (hasWall(b) != s.hasWall(b))
        return false;;
    }
    return true;
  }

  @Override
  public boolean isFullyKnown() {
    for (Bearing b : Bearing.NESW)
      if (!knowsWall(b))
        return false;

    return true;
  }

  @Override
  public Sector clearWalls() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean givesAccessTo(Bearing atBearing) {
    return knowsWall(atBearing) && hasNoWall(atBearing);
  }

  @Override
  public String toString() {
    return GridUtils.createSectorWallsString(this);
  }
}
