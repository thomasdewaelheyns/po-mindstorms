/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import penoplatinum.grid.agent.Agent;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

/**
 *
 * @author MHGameWork
 */
public class ObservableGrid implements Grid {

  private final Grid grid;
  private GridObserver observer;

  ObservableGrid(Grid grid) {
    this.grid = grid;

    observer = new GridObserver() {

      @Override
      public void agentChanged(Grid g, Agent a) {
      }
    };
  }

  public ObservableGrid useObserver(GridObserver observer) {
    this.observer = observer;
    return this;
  }

  @Override
  public Grid add(Sector s, Point position) {
    grid.add(s, position);
    return this;
  }

  @Override
  public Sector getSectorAt(Point position) {
    return grid.getSectorAt(position);
  }

  @Override
  public Point getPositionOf(Sector sector) {
    return grid.getPositionOf(sector);
  }

  @Override
  public Iterable<Sector> getSectors() {
    return grid.getSectors();
  }

  @Override
  public Grid add(Agent agent, Point position, Bearing bearing) {
    grid.add(agent, position, bearing);
    observer.agentChanged(this, agent);
    return this;
  }

  @Override
  public Grid moveTo(Agent agent, Point position, Bearing bearing) {
    grid.moveTo(agent, position, bearing);
    observer.agentChanged(this, agent);
    return this;
  }

  @Override
  public Bearing getBearingOf(Agent agent) {
    return grid.getBearingOf(agent);
  }

  @Override
  public Agent getAgent(String name) {
    return grid.getAgent(name);
  }

  @Override
  public Iterable<Agent> getAgents() {
    return grid.getAgents();
  }

  @Override
  public int getMinLeft() {
    return grid.getMinLeft();
  }

  @Override
  public int getMaxLeft() {
    return grid.getMaxLeft();
  }

  @Override
  public int getMinTop() {
    return grid.getMinTop();
  }

  @Override
  public int getMaxTop() {
    return grid.getMaxTop();
  }

  @Override
  public int getWidth() {
    return grid.getWidth();
  }

  @Override
  public int getHeight() {
    return grid.getHeight();
  }

  @Override
  public int getSize() {
    return grid.getSize();
  }

  @Override
  public Point getPositionOf(Agent agent) {
    return grid.getPositionOf(agent);
  }

  @Override
  public Agent getAgentAt(Point position, Class cls) {
    return grid.getAgentAt(position, cls);
  }

  @Override
  public int getSectorId(Point position) {
    return grid.getSectorId(position);
  }

  @Override
  public Sector getSector(int id) {
    return grid.getSector(id);
  }

  @Override
  public boolean hasNeighbour(int sectorId, Bearing atBearing) {
    return grid.hasNeighbour(sectorId, atBearing);
  }

  @Override
  public int getNeighbourId(int sectorId, Bearing atBearing) {
    return grid.getNeighbourId(sectorId, atBearing);
  }

  @Override
  public Grid setValue(int sectorId, int value) {
    grid.setValue(sectorId, value);
    return this;
  }

  @Override
  public int getValue(int sectorId) {
    return grid.getValue(sectorId);
  }

  @Override
  public Grid setWall(int sectorId, Bearing atBearing) {
    return grid.setWall(sectorId, atBearing);
  }

  @Override
  public Grid setNoWall(int sectorId, Bearing atBearing) {
    return grid.setNoWall(sectorId, atBearing);
  }

  @Override
  public Grid clearWall(int sectorId, Bearing atBearing) {
    grid.clearWall(sectorId, atBearing);
    return this;
  }

  @Override
  public boolean hasWall(int sectorId, Bearing atBearing) {
    return grid.hasWall(sectorId, atBearing);
  }

  @Override
  public boolean hasNoWall(int sectorId, Bearing atBearing) {
    return grid.hasNoWall(sectorId, atBearing);
  }

  @Override
  public boolean knowsWall(int sectorId, Bearing atBearing) {
    return grid.knowsWall(sectorId, atBearing);
  }

  @Override
  public boolean isFullyKnown(int sectorId) {
    return grid.isFullyKnown(sectorId);
  }

  @Override
  public Grid clearWalls(int sectorId) {
    grid.clearWalls(sectorId);
    return this;
  }

  @Override
  public boolean givesAccessTo(int sectorId, Bearing atBearing) {
    return grid.givesAccessTo(sectorId, atBearing);
  }
}
