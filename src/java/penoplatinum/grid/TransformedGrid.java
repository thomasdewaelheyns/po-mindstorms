/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

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
    transformation.transform(position);

    grid.add(s, position);

    transformation.inverseTransform(position);

    return this;
  }

  @Override
  public Sector getSectorAt(Point position) {
    transformation.transform(position);

    Sector s = grid.getSectorAt(position);

    transformation.inverseTransform(position);
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Grid add(Agent agent, Point position, Bearing bearing) {
    transformation.transform(position);
    bearing = bearing.rotate(transformation.getRotation());

    grid.add(agent, position, bearing);

    transformation.inverseTransform(position);

    return this;
  }

  @Override
  public Grid moveTo(Agent agent, Point position, Bearing bearing) {
    transformation.transform(position);
    bearing = bearing.rotate(transformation.getRotation());

    grid.moveTo(agent, position, bearing);

    transformation.inverseTransform(position);

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
    transformation.transform(position);

    Agent a = grid.getAgentAt(position);

    transformation.inverseTransform(position);
    return a;
  }

  @Override
  public Iterable<Agent> getAgents() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private int getBoundAt(Bearing b) {
    b = b.rotate(transformation.getRotation());
    switch (b) {
      case N:
        return grid.getMinTop();
      case E:
        return grid.getMaxLeft();
      case S:
        return grid.getMaxTop();
      case W:
        return grid.getMinLeft();
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
    Bearing b = Bearing.N.rotate(transformation.getRotation());

    if (b == Bearing.N || b == Bearing.S)
      return grid.getWidth();
    return grid.getHeight();
  }

  @Override
  public int getHeight() {
    Bearing b = Bearing.N.rotate(transformation.getRotation());

    if (b == Bearing.N || b == Bearing.S)
      return grid.getHeight();
    return grid.getHeight();
  }

  @Override
  public int getSize() {
    return grid.getSize();
  }
}
