/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import java.util.ArrayList;
import java.util.List;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.TransformationTRT;

/**
 * This grid implementation decorates another grid. It provides a 'view'
 * for the decorated grid given a transformation
 * 
 * @author MHGameWork
 */
public class TransformedGrid implements Grid {

  private TransformationTRT transformation = TransformationTRT.Identity;
  private Grid grid;

  public TransformedGrid(Grid grid) {
    this.grid = grid;
  }

  public TransformationTRT getTransformation() {
    return transformation;
  }

  public void setTransformation(TransformationTRT transformation) {
    this.transformation = transformation;
  }

  public Grid getGrid() {
    return grid;
  }

  @Override
  public Grid add(Sector s, Point position) {
    transformation.inverseTransform(position);
    //inverse transform the input sector!
    boolean[] knows = new boolean[4];
    boolean[] wall = new boolean[4];
    int i = 0;
    for (Bearing b : Bearing.NESW) {
      knows[i] = s.knowsWall(b);
      if (knows[i])
        wall[i] = s.hasWall(b);
      i++;
    }
    i = 0;
    for (Bearing b : Bearing.NESW) {
      Bearing oriBearing = b.rotate(transformation.getRotation().invert());


      if (!knows[i])
        s.clearWall(oriBearing);
      else {
        if (wall[i])
          s.setWall(oriBearing);
        else
          s.setNoWall(oriBearing);
      }
      i++;
    }

    grid.add(s, position);

    transformation.transform(position);

    return this;
  }

  private void copyWallFrom(Sector source, Sector target, Bearing sourceBearing, Bearing targetBearing) {
    if (!source.knowsWall(sourceBearing))
      target.clearWall(targetBearing);
    else {
      if (source.hasWall(sourceBearing))
        target.setWall(targetBearing);
      else
        target.setNoWall(targetBearing);
    }
  }

  @Override
  public Sector getSectorAt(Point position) {
    transformation.inverseTransform(position);

    Sector s = grid.getSectorAt(position);
    if (s == null)
      return null;

    transformation.transform(position);
    return new TransformedSector(s, transformation);
  }

  @Override
  public Point getPositionOf(Sector sector) {
    while (sector instanceof TransformedSector)
      sector = ((TransformedSector) sector).getDecoratedSector();

    Point p = grid.getPositionOf(sector);
    transformation.transform(p);
    return p;
  }

  @Override
  public Iterable<Sector> getSectors() {
    //Only use on pc!!!
    List<Sector> ret = new ArrayList<Sector>();
    for (Sector s : grid.getSectors())
      ret.add(new TransformedSector(s, transformation));
    return ret;
  }

  @Override
  public Grid add(Agent agent, Point position, Bearing bearing) {
    transformation.inverseTransform(position);
    bearing = bearing.rotate(transformation.getRotation().invert());

    grid.add(agent, position, bearing);

    transformation.transform(position);

    return this;
  }

  @Override
  public Grid moveTo(Agent agent, Point position, Bearing bearing) {
    transformation.inverseTransform(position);
    bearing = bearing.rotate(transformation.getRotation().invert());

    grid.moveTo(agent, position, bearing);

    transformation.transform(position);

    return this;
  }

  @Override
  public Sector getSectorOf(Agent agent) {
    Sector s = grid.getSectorOf(agent);
    return new TransformedSector(s, transformation);
  }

  @Override
  public Bearing getBearingOf(Agent agent) {
    Bearing b = grid.getBearingOf(agent);
    return b.rotate(transformation.getRotation());
  }

  @Override
  public Agent getAgent(String name) {
    return grid.getAgent(name);
  }

  @Override
  public Agent getAgentAt(Point position) {
    transformation.inverseTransform(position);

    Agent a = grid.getAgentAt(position);

    transformation.transform(position);
    return a;
  }

  @Override
  public Iterable<Agent> getAgents() {
    return grid.getAgents();
  }

  private int getBoundAt(Bearing b) {

    Point min = new Point(grid.getMinLeft(), grid.getMinTop());
    Point max = new Point(grid.getMaxLeft(), grid.getMaxTop());

    transformation.transform(min);
    transformation.transform(max);

    switch (b) {
      case N:
        return (int) Math.min(min.getY(), max.getY());
//        return grid.getMinTop();
      case E:
        return (int) Math.max(min.getX(), max.getX());
//        return grid.getMaxLeft();
      case S:
        return (int) Math.max(min.getY(), max.getY());
//        return grid.getMaxTop();
      case W:
        return (int) Math.min(min.getX(), max.getX());
//        return grid.getMinLeft();
      default:
        throw new UnsupportedOperationException();
    }
  }

  @Override
  public int getMinLeft() {
    return getBoundAt(Bearing.W);
  }

  @Override
  public int getMaxLeft() {
    return getBoundAt(Bearing.E);
  }

  @Override
  public int getMinTop() {
    return getBoundAt(Bearing.N);
  }

  @Override
  public int getMaxTop() {
    return getBoundAt(Bearing.S);
  }

  @Override
  public int getWidth() {
    Bearing b = Bearing.N.rotate(transformation.getRotation().invert());

    if (b == Bearing.N || b == Bearing.S)
      return grid.getWidth();
    return grid.getHeight();
  }

  @Override
  public int getHeight() {
    Bearing b = Bearing.N.rotate(transformation.getRotation().invert());

    if (b == Bearing.N || b == Bearing.S)
      return grid.getHeight();
    return grid.getWidth();
  }

  @Override
  public int getSize() {
    return grid.getSize();
  }

  @Override
  public String toString() {
    return GridUtils.createGridSectorsString(this);
  }
}
