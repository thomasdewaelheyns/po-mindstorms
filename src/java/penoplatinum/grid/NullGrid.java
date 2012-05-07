package penoplatinum.grid;

/**
 * NullGrid is an implementation of Grid that does nothing, both good or bad
 * 
 * @author Team Platinum
 */
import penoplatinum.grid.view.GridView;
import penoplatinum.grid.agent.Agent;
import java.util.List;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class NullGrid implements Grid {

  private static Grid instance = null;

  private NullGrid() {
  }

  public static Grid getInstance() {
    if (instance == null) {
      synchronized (NullGrid.class) {
        instance = new NullGrid();
      }
    }
    return instance;
  }

  public Grid addSector(Sector sector) {
    return this;
  }

  public Sector getSector(int left, int top) {
    return null;
  }

  public List<Sector> getSectors() {
    return null;
  }

  public Grid displayOn(GridView view) {
    return this;
  }

  public Grid refresh() {
    return this;
  }

  public Grid dump() {
    return this;
  }

  public int getMinLeft() {
    return 0;
  }

  public int getMaxLeft() {
    return 0;
  }

  public int getMinTop() {
    return 0;
  }

  public int getMaxTop() {
    return 0;
  }

  public int getWidth() {
    return 0;
  }

  public int getHeight() {
    return 0;
  }

  public Grid addAgent(Agent agent) {
    return this;
  }

  public Grid add(Agent agent, Point point) {
    return this;
  }

  public Agent getAgent(String name) {
    return null;
  }

  public List<Agent> getAgents() {
    return null;
  }

  public Grid clearAgents() {
    return this;
  }

  public Grid sectorsNeedRefresh() {
    return this;
  }

  public Grid wallsNeedRefresh() {
    return this;
  }

  public Grid valuesNeedRefresh() {
    return this;
  }

  public Grid agentsNeedRefresh() {
    return this;
  }

  public void addTaggedSector(Sector s) {
  }

  public List<Sector> getTaggedSectors() {
    return null;
  }

  public void removeAgent(Agent agent) {
  }

  public Sector getOrCreateSector(int x, int y) {
    return null;
  }

  public void barcodesNeedRefresh() {
  }

  public boolean areSectorsEqual(Grid other) {
    return false;
  }

  public int getSize() {
    return 0;
  }

  public void disengage() {
  }

  public Agent getAgentAt(Sector sector) {
    return null;
  }

  public Point getPositionOf(Sector sector) {
    return null;
  }

  public Point getPosition(Agent sector) {
    return null;
  }

  public Grid remove(Agent agent) {
    return this;
  }

  public Grid add(Agent agent) {
    return this;
  }

  public Sector getSectorAt(Point position) {
    return null;
  }

  public Bearing getBearingOf(Agent agent) {
    return Bearing.UNKNOWN;
  }

  public Sector getSectorOf(Agent agent) {
    return null;
  }

  public Grid addRoot(Sector sector) {
    return this;
  }

  public Grid moveForward(Agent agent) {
    return this;
  }

  @Override
  public Grid add(Sector s, Point position) {
    return null;
  }

  @Override
  public Grid add(Agent agent, Point position, Bearing bearing) {
    return this;
  }

  @Override
  public Grid moveTo(Agent agent, Point position, Bearing bearing) {
    return this;
  }

  @Override
  public Point getPositionOf(Agent agent) {
    return null;
  }

  @Override
  public Agent getAgentAt(Point position, Class cls) {
    return null;
  }

  @Override
  public int getSectorId(Point position) {
    return 0;
  }

  @Override
  public Sector getSector(int id) {
    return null;
  }

  @Override
  public boolean hasNeighbour(int sectorId, Bearing atBearing) {
    return false;
  }

  @Override
  public int getNeighbourId(int sectorId, Bearing atBearing) {
    return -1;
  }

  @Override
  public Grid setValue(int sectorId, int value) {
    return this;
  }

  @Override
  public int getValue(int sectorId) {
    return 0;
  }

  @Override
  public Grid setWall(int sectorId, Bearing atBearing) {
    return this;
  }

  @Override
  public Grid setNoWall(int sectorId, Bearing atBearing) {
    return this;
  }

  @Override
  public Grid clearWall(int sectorId, Bearing atBearing) {
    return this;
  }

  @Override
  public boolean hasWall(int sectorId, Bearing atBearing) {
    return false;
  }

  @Override
  public boolean hasNoWall(int sectorId, Bearing atBearing) {
    return false;
  }

  @Override
  public boolean knowsWall(int sectorId, Bearing atBearing) {
    return false;
  }

}