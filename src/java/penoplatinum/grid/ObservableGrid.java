package penoplatinum.grid;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

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
  public Sector getSectorOf(Agent agent) {
    return grid.getSectorOf(agent);
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
  public Agent getAgentAt(Point position) {
    return grid.getAgentAt(position);
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

  public boolean hasAgentOn(Sector sector, Class type) {
    throw new RuntimeException("not implemented");
  }

}
