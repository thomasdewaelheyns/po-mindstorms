package penoplatinum.grid;

import java.util.List;
import penoplatinum.util.TransformationTRT;

/**
 *
 * @author MHGameWork
 */
public class NullGrid implements Grid {
  private static Grid instance = null;

  private NullGrid() {}
  
  public static Grid getInstance() {
    if(instance == null) {
      synchronized(NullGrid.class) { 
        instance = new NullGrid();
      }
    }
    return instance;
  }

  @Override
  public Grid setProcessor(GridProcessor processor) {
    return this;
  }

  @Override
  public Grid addSector(Sector sector) {
    return this;
  }

  @Override
  public Sector getSector(int left, int top) {
    return null;
  }

  @Override
  public List<Sector> getSectors() {
    return null;
  }

  @Override
  public Grid displayOn(GridView view) {
    return this;
  }

  @Override
  public Grid refresh() {
    return this;
  }

  @Override
  public Grid dump() {
    return this;
  }

  @Override
  public int getMinLeft() {
    return 0;
  }

  @Override
  public int getMaxLeft() {
    return 0;
  }

  @Override
  public int getMinTop() {
    return 0;
  }

  @Override
  public int getMaxTop() {
    return 0;
  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public Grid addAgent(Agent agent) {
    return this;
  }

  @Override
  public Agent getAgent(String name) {
    return null;
  }

  @Override
  public List<Agent> getAgents() {
    return null;
  }

  @Override
  public Grid clearAgents() {
    return this;
  }

  @Override
  public Grid sectorsNeedRefresh() {
    return this;
  }

  @Override
  public Grid wallsNeedRefresh() {
    return this;
  }

  @Override
  public Grid valuesNeedRefresh() {
    return this;
  }

  @Override
  public Grid agentsNeedRefresh() {
    return this;
  }


  @Override
  public void importGrid(Grid g, TransformationTRT transformation) {
  }

  @Override
  public void addTaggedSector(Sector s) {
  }

  @Override
  public List<Sector> getTaggedSectors() {
    return null;
  }

  @Override
  public void removeAgent(Agent agent) {
  }

  @Override
  public Sector getOrCreateSector(int x, int y) {
    return null;
  }

  @Override
  public void barcodesNeedRefresh() {
  }

  @Override
  public boolean areSectorsEqual(Grid other) {
    return false;
  }

  @Override
  public int getSize() {
    return 0;
  }
  
}