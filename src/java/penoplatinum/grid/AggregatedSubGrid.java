/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import java.util.List;
import penoplatinum.pacman.GhostAgent;
import penoplatinum.pacman.PacmanAgent;
import penoplatinum.simulator.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.TransformationTRT;
import penoplatinum.util.Utils;

/**
 * Represents a subgrid of the AggregatedGrid. 
 * This grid has 2 modes of operation. 
 * In the first mode this grid locally stores information. 
 * In the second mode, it relays its commands to the parent AggregatedGrid
 * 
 * 
 * @author MHGameWork
 */
public class AggregatedSubGrid {

  private Grid storageGrid;
  private TransformationTRT transformation;
  private final AggregatedGrid baseGrid;

  public AggregatedSubGrid(AggregatedGrid baseGrid) {
    this.baseGrid = baseGrid;

  }

  public Grid getStorageGrid() {
    return storageGrid;
  }

  public void setStorageGrid(Grid decoratedGrid) {
    this.storageGrid = decoratedGrid;
  }

  public TransformationTRT getTransformation() {
    return transformation;
  }

  public void setTransformation(TransformationTRT transformation) {
    this.transformation = transformation;
  }

  public void setSector(int x, int y, Boolean[] walls) {
    if (storageGrid != null) {
      Sector s = storageGrid.getOrCreateSector(x, y);
      for (int i = 0; i < walls.length; i++) {
        s.setWall(i, walls[i]);
      }
    } else {
      Point p = transformation.transform(x, y);
      x = p.getX();
      y = p.getY();

      Sector baseSector = baseGrid.getOrCreateSector(x, y);
      for (int i = 0; i < walls.length; i++) {
        baseSector.setWall(i, walls[(i + transformation.getRotation()) % 4]);
      }
    }

  }

  public void setBarcodeAt(int left, int top, int code, int bearing) {
    if (storageGrid != null) {


      Sector barcodeSector = storageGrid.getOrCreateSector(left, top);
      
      
      barcodeSector.setTagCode(code);
      barcodeSector.setTagBearing(bearing);

      // tag the current sector of the agent with the given barcode
      // check if we have it too
      // if so, import the agents map in our grid,
      //        create translators to continously import its information
      //        in our own grid
      List<Sector> bs = baseGrid.getTaggedSectors();
      for (int i = 0; i < bs.size(); i++) {
        baseGrid.attemptMapBarcode(bs.get(i), barcodeSector, baseGrid.getGhostNameForGrid(this));
      }
    } else {
      Sector barcodeSector = storageGrid.getOrCreateSector(left, top);
      barcodeSector.setTagCode(code);
      barcodeSector.setTagBearing((bearing + transformation.getRotation()) % 4);

      //TODO: decide what to do know


    }

  }

  public void setActorPosition(String agentName, int x, int y) {
    // Duplicate code is here for a reason! (reason is convert to state pattern)
    if (storageGrid != null) {
      Agent agent = storageGrid.getAgent(agentName);

      if (agent == null) {
        agent = new GhostAgent(agentName);
        storageGrid.addAgent(agent);
      }
      storageGrid.getOrCreateSector(x, y).put(agent, Bearing.N);

    } else {
      Point p = transformation.transform(x, y);
      x = p.getX();
      y = p.getY();

      Agent agent = baseGrid.getAgent(agentName);

      if (agent == null) {
        agent = new GhostAgent(agentName);
        baseGrid.addAgent(agent);
      }
      baseGrid.getOrCreateSector(x, y).put(agent, Bearing.N);


    }
  }

  public void setPacmanPosition(int x, int y) {

    if (storageGrid != null) {
      Agent ag = storageGrid.getAgent("pacman");
      if (ag == null) {
        ag = new PacmanAgent();
        storageGrid.addAgent(ag);
      }

      storageGrid.getOrCreateSector(x, y).put(ag, Bearing.N);

    } else {

      Point p = transformation.transform(x, y);
      x = p.getX();
      y = p.getY();

      Agent ag = baseGrid.getAgent("pacman");
      if (ag == null) {
        ag = new PacmanAgent();
        baseGrid.addAgent(ag);
      }

      baseGrid.getOrCreateSector(x, y).put(ag, Bearing.N);

    }

  }
}