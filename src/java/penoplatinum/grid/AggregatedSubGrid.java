/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import java.util.List;
import penoplatinum.util.TransformationTRT;

/**
 * Represents a subgrid of the AggregatedGrid. 
 * This grid has 2 modes of operation. 
 * In the first mode this grid locally stores information. 
 * In the second mode, it relays its commands to the parent AggregatedGrid
 * 
 * 
 * TODO: add transformations EVERYWHERE
 * TODO: define this as a relaygrid instead
 * 
 * @author MHGameWork
 */
public class AggregatedSubGrid implements Grid {

  private Grid decoratedGrid;
  private TransformationTRT transformation;


  public Grid getDecoratedGrid() {
    return decoratedGrid;
  }

  public void setDecoratedGrid(Grid decoratedGrid) {
    this.decoratedGrid = decoratedGrid;
  }

  public TransformationTRT getTransformation() {
    return transformation;
  }

  public void setTransformation(TransformationTRT transformation) {
    this.transformation = transformation;
  }

  @Override
  public Grid setProcessor(GridProcessor processor) {
    return decoratedGrid.setProcessor(processor);
  }

  @Override
  public Grid addSector(Sector sector) {
    return decoratedGrid.addSector(sector);
  }

  @Override
  public Sector getSector(int left, int top) {
//     grid = model.getGridPart().getGrid();
//      // transform the x and y coord
//      Point p = ghost.getTransformationTRT().transform(x, y);
//      x = p.getX();
//      y = p.getY();
    
    
    return decoratedGrid.getSector(left, top);
  }

  @Override
  public List<Sector> getSectors() {
    return decoratedGrid.getSectors();
  }

  @Override
  public Grid displayOn(GridView view) {
    return decoratedGrid.displayOn(view);
  }

  @Override
  public Grid refresh() {
    return decoratedGrid.refresh();
  }

  @Override
  public Grid dump() {
    return decoratedGrid.dump();
  }

  @Override
  public int getMinLeft() {
    return decoratedGrid.getMinLeft();
  }

  @Override
  public int getMaxLeft() {
    return decoratedGrid.getMaxLeft();
  }

  @Override
  public int getMinTop() {
    return decoratedGrid.getMinTop();
  }

  @Override
  public int getMaxTop() {
    return decoratedGrid.getMaxTop();
  }

  @Override
  public int getWidth() {
    return decoratedGrid.getWidth();
  }

  @Override
  public int getHeight() {
    return decoratedGrid.getHeight();
  }

  @Override
  public Grid addAgent(Agent agent) {
    return decoratedGrid.addAgent(agent);
  }

  @Override
  public Agent getAgent(String name) {
    return decoratedGrid.getAgent(name);
  }

  @Override
  public void removeAgent(Agent agent) {
    decoratedGrid.removeAgent(agent);
  }

  @Override
  public List<Agent> getAgents() {
    return decoratedGrid.getAgents();
  }

  @Override
  public Grid clearAgents() {
    return decoratedGrid.clearAgents();
  }

  @Override
  public Grid sectorsNeedRefresh() {
    return decoratedGrid.sectorsNeedRefresh();
  }

  @Override
  public Grid wallsNeedRefresh() {
    return decoratedGrid.wallsNeedRefresh();
  }

  @Override
  public Grid valuesNeedRefresh() {
    return decoratedGrid.valuesNeedRefresh();
  }

  @Override
  public Grid agentsNeedRefresh() {
    return decoratedGrid.agentsNeedRefresh();
  }

  @Override
  public void addTaggedSector(Sector s) {
    decoratedGrid.addTaggedSector(s);
  }

  @Override
  public List<Sector> getTaggedSectors() {
    return decoratedGrid.getTaggedSectors();
  }

  @Override
  public Sector getOrCreateSector(int x, int y) {
    return decoratedGrid.getOrCreateSector(x, y);
  }

  @Override
  public void barcodesNeedRefresh() {
    decoratedGrid.barcodesNeedRefresh();
  }

  @Override
  public boolean areSectorsEqual(Grid other) {
    return decoratedGrid.areSectorsEqual(other);
  }

  @Override
  public int getSize() {
    return decoratedGrid.getSize();
  }

  @Override
  public void disengage() {
    decoratedGrid.disengage();
  }
}