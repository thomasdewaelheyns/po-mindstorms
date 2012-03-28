package penoplatinum.grid;

/**
 * Sector
 * 
 * A Sector is a single location in a Grid. It has links and movement methods
 * to its neighbours. It can hold an Agent and be assigned a Tag. It stores
 * information abouts its walls and how certain it is about that information.
 * 
 * @author: Team Platinum
 */

import penoplatinum.BitwiseOperations;

import penoplatinum.util.Bearing;
import penoplatinum.util.Rotation;


public class Sector {
  // back-link to the Grid we live in
  private Grid grid;

  // links to the adjacent Sectors
  private Sector[] neighbours = new Sector[4];

  // position within Grid
  private int left, top;

  // walls and the certainty about them
  private char walls = 0;
  private char certainty = 0;

  // the value associated with this sector
  private int     value = 0;
  private int     tagCode = -1;
  private Bearing tagBearing;

  // our own rotation
  private Rotation rotation = Rotation.NONE;

  public Sector() {
    this.putOn(NullGrid.getInstance());
  }

  public Sector(Grid grid) {
    this.putOn(grid);
  }
  
  // copy constructor
  // TODO: copy ALL information
  public Sector(Sector original) {
    this();
    this.addWalls(original.getWalls());
  }
  
  public Sector rotate(Rotation rotation) {
    this.rotation = this.rotation.add(rotation);
    return this;
  }

  public String toString() {
    return "(" + getLeft() + "," + getTop() + ")"
            + "N" + (this.isKnown(Bearing.N) ? (this.hasWall(Bearing.N) ? "Y" : " ") : "?")
            + "E" + (this.isKnown(Bearing.E) ? (this.hasWall(Bearing.E) ? "Y" : " ") : "?")
            + "S" + (this.isKnown(Bearing.S) ? (this.hasWall(Bearing.S) ? "Y" : " ") : "?")
            + "W" + (this.isKnown(Bearing.W) ? (this.hasWall(Bearing.W) ? "Y" : " ") : "?");
  }

  // sets the absolute coordinates in the Grid this Sector is placed in
  public Sector setCoordinates(int left, int top) {
    this.left = left;
    this.top = top;
    return this;
  }

  public int getLeft() {
    return this.left;
  }

  public int getTop() {
    return this.top;
  }

  public Sector putOn(Grid grid) {
    this.grid = grid;
    return this;
  }

  public Grid getGrid() {
    return this.grid;
  }

  // adds a neighbour at a given location
  public Sector addNeighbour(Sector neighbour, Bearing atBearing) {
    this.neighbours[this.mapBearing(atBearing)] = neighbour;
    this.exchangeWallInfo(atBearing);
    return this;
  }
  
  private int mapBearing(Bearing bearing) {
    // map bearing to our index (don't reuse internal Bearing representation)
    int index = 0; // default N = 0
    switch(bearing) {
      case E: index = 1; break;
      case S: index = 2; break;
      case W: index = 3; break;
    }
    return index;
  }

  // WATCH OUT: this method returns the new neighbour, not itself
  public Sector createNeighbour(Bearing atBearing) {
    // if we already have a neighbour at this location, return it
    if (this.hasNeighbour(atBearing)) {
      return this.getNeighbour(atBearing);
    }
    // create empty sector and assign it coordinates relative to ours
    int left = this.getLeft(), top = this.getTop();
    switch (atBearing) {
      case N: top--;  break;
      case E: left++; break;
      case S: top++;  break;
      case W: left--; break;
    }
    Sector neighbour = new Sector().setCoordinates(left, top);
    // add it to the grid, it will be connected to us and all other relevant
    // sectors
    this.grid.addSector(neighbour);
    return neighbour;
  }

  private Sector exchangeWallInfo(Bearing atBearing) {
    Sector neighbour = this.getNeighbour(atBearing);
    if(neighbour == null || neighbour == this) {
      return this;
    }

    Bearing locationAtNeighbour = atBearing.reverse();
    Boolean iHaveWall = this.hasWall(atBearing);
    Boolean neighbourHasWall = neighbour.hasWall(locationAtNeighbour);

    // if we have different information, we need to update it
    if (neighbourHasWall != iHaveWall) {
      if (neighbourHasWall == null) { // T/F != null
        if (iHaveWall) {
          neighbour.inheritWall(locationAtNeighbour);
        } else {
          neighbour.inheritNoWall(locationAtNeighbour);
        }
      } else if (iHaveWall == null) {   // null != T/F
        if (neighbourHasWall) {
          this.inheritWall(atBearing);
        } else {
          this.inheritNoWall(atBearing);
        }
      } else {                           // T/F != F/T
        // conflicting information => clear both, go back to unknown state
        System.err.println("Conflicting Wall information: " + this.getLeft() + "," + this.getTop() + " / " + atBearing);
        this.clearWall(atBearing);
        neighbour.clearWall(locationAtNeighbour);
      }
    }
    return this;
  }

  public boolean hasNeighbour(Bearing atBearing) {
    return this.getNeighbour(atBearing) != null;
  }

  // returns the neighbour at a given location
  public Sector getNeighbour(Bearing atBearing) {
    if(atBearing == Bearing.UNKNOWN) {
      return this;
    }
    return this.neighbours[this.mapBearing(atBearing)];
  }

  // keeps track of an agent occupying this sector
  public Sector put(Agent agent, Bearing bearing) {
    // reset the value to zero, because an agent has its own value
    this.setValue(0);
    // now add the agent
    throw new RuntimeException( "Commented out code, needs to be fixed." );
    // agent.assignSector(this, bearing);
    // this.grid.addAgent(agent);
    // return this;
  }

  public boolean hasAgent() {
    return getAgent() != null;
  }

  public Agent getAgent() {
    return grid.getAgentAt(this);
  }

  // sets the value of the sector
  public Sector setValue(int value) {
    // if there is an agent on the sector, we don't change the value
    if (this.hasAgent()) {
      return this;
    }
    if (value != this.value) {
      this.value = value;
      this.grid.valuesNeedRefresh();
    }
    return this;
  }

  // if an agent is occupying us, return the agent's value else our own
  public int getValue() {
    return hasAgent() ? getAgent().getValue() : this.value;
  }

  public Bearing getTagBearing() {
    return this.tagBearing;
  }

  public void setTagBearing(Bearing tagBearing) {
    this.tagBearing = tagBearing;
    grid.barcodesNeedRefresh();
  }

  public int getTagCode() {
    return tagCode;
  }

  public void setTagCode(int tagCode) {
    this.tagCode = tagCode;
    grid.addTaggedSector(this);
    grid.barcodesNeedRefresh();
  }

  // adds a wall on this sector at given location
  public Sector addWall(Bearing atBearing) {
    this.inheritWall(atBearing);
    // also set the wall at our neighbour's
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).inheritWall(atBearing.reverse());
    }
    this.grid.wallsNeedRefresh();
    return this;
  }

  protected void inheritWall(Bearing atBearing) {
    this.withWall(atBearing);
    this.grid.wallsNeedRefresh();
  }

  // removes a wall from this sector at given location
  public Sector removeWall(Bearing atBearing) {
    this.inheritNoWall(atBearing);
    // also remove the wall at our neighbour's
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).inheritNoWall(atBearing.reverse());
    }
    this.grid.wallsNeedRefresh();
    return this;
  }

  protected void inheritNoWall(Bearing atBearing) {
    this.withoutWall(atBearing);
    this.grid.wallsNeedRefresh();
  }

  // adds all walls at once
  public Sector addWalls(char walls) {
    this.withWalls(walls);
    // also update neighbours
    this.updateNeighboursWalls();

    this.grid.wallsNeedRefresh();
    return this;
  }

  protected void updateNeighboursWalls() {
    for( Bearing atBearing : Bearing.values() ) {
      Sector neighbour = this.getNeighbour(atBearing);
      Boolean haveWall = this.hasWall(atBearing);
      if (neighbour != null && haveWall != null) {
        if (haveWall) {
          neighbour.inheritWall(atBearing.reverse());
        } else {
          neighbour.inheritNoWall(atBearing.reverse());
        }
      }
    }
  }

  // we use the Boolean class here to be able to return "null" when we don't
  // know anything about the wall.
  public Boolean hasWall(Bearing wall) {
    return this.knowsWall(wall) ? this.hasRawWall(wall) : null;
  }
  
  public void setWall(Bearing wall, Boolean value) {
    if (value == null) {
      this.clearWall(wall);
    } else if (value) {
      this.addWall(wall);
    } else {
      this.removeWall(wall);
    }
  }

  // returns true if there is NO wall and we absolutely can move forward
  // that way
  public boolean givesAccessTo(Bearing atBearing) {
    return this.knowsWall(atBearing) && !this.hasRawWall(atBearing);
  }

  // returns all wall configuration
  public char getWalls() {
    return this.walls;
  }

  // clears the certainty information of the Sector
  public Sector clearCertainty() {
    this.certainty = 0;
    return this;
  }

  // clears all knowledge about a wall
  public Sector clearWall(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls     = (char) BitwiseOperations.unsetBit(this.walls,     bit);
    this.certainty = (char) BitwiseOperations.unsetBit(this.certainty, bit);
    return this;
  }

  // clears all knowledge about a wall
  public Sector clearWalls() {
    this.walls = 0;
    this.certainty = 0;
    return this;
  }

  // returns the certainty information about the Sector
  public int getCertainty() {
    return this.certainty;
  }

  // determines if the wall at given location is known (to be there or not)
  public boolean isKnown(Bearing atBearing) {
    return this.knowsWall(atBearing);
  }

  public boolean isFullyKnown() {
    return this.getCertainty() == 15;
  }

  private void withWall(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls     = (char) BitwiseOperations.setBit(this.walls,     bit);
    this.certainty = (char) BitwiseOperations.setBit(this.certainty, bit);
  }

  public void withWalls(char walls) {
    this.walls     = walls;
    this.certainty = 15;
  }

  public void withoutWall(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls     = (char) BitwiseOperations.unsetBit(this.walls,     bit);
    this.certainty = (char) BitwiseOperations.setBit  (this.certainty, bit);
  }

  public void dontKnow(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.certainty = (char) BitwiseOperations.unsetBit(this.certainty, bit);
  }

  public boolean hasRawWall(Bearing atBearing) {
    int bit = this.mapBearing(this.applyRotation(atBearing));
    return BitwiseOperations.hasBit(this.walls, bit);
  }

  public boolean knowsWall(Bearing atBearing) {
    int bit = this.mapBearing(this.applyRotation(atBearing));
    return BitwiseOperations.hasBit(this.certainty, bit);
  }
  
  private void disengage() {
    for(int i = 0; i < 4; i++) {
      this.neighbours[i] = null;
    }
    grid = null;
  }
  
  private Bearing applyRotation(Bearing bearing) {
    return  bearing.rotate(this.rotation.invert());
  }
}
