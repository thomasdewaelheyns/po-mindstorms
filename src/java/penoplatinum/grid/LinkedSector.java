package penoplatinum.grid;

/**
 * LinkedSector
 * 
 * A Sector is a single location in a Grid. It has links and movement methods
 * to its neighbours. It can hold an Agent and be assigned a Tag. It stores
 * information abouts its walls and how certain it is about that information.
 * 
 * @author: Team Platinum
 */
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Rotation;
import penoplatinum.util.BitwiseOperations;

public class LinkedSector implements Sector {
  // back-link to the Grid we live in

  private Grid grid;
  // links to the adjacent Sectors
  private LinkedSector[] neighbours = new LinkedSector[4];
  // walls and the certainty about them
  private char walls = 0;
  private char certainty = 0;
  // the value associated with this sector
  private int value = 0;

  public LinkedSector() {
    this.putOn(NullGrid.getInstance());
  }

  // copy constructor
  // TODO: copy ALL information
  public LinkedSector(Sector original) {
    this();
    this.addWalls(original.getWalls());
  }

//  public LinkedSector rotate(Rotation rotation) {
//    this.rotation = this.rotation.add(rotation);
//    return this;
//  }

  public String toString() {
    return //"(" + getLeft() + "," + getTop() + ")" +
             "N" + (this.isKnown(Bearing.N) ? (this.hasWall(Bearing.N) ? "Y" : " ") : "?")
            + "E" + (this.isKnown(Bearing.E) ? (this.hasWall(Bearing.E) ? "Y" : " ") : "?")
            + "S" + (this.isKnown(Bearing.S) ? (this.hasWall(Bearing.S) ? "Y" : " ") : "?")
            + "W" + (this.isKnown(Bearing.W) ? (this.hasWall(Bearing.W) ? "Y" : " ") : "?");
  }

  public LinkedSector putOn(Grid grid) {
    this.grid = grid;
    return this;
  }

  public Grid getGrid() {
    return this.grid;
  }

  // adds a neighbour at a given location
  public LinkedSector addNeighbour(Sector neighbour, Bearing atBearing) {
    this.neighbours[this.mapBearing(atBearing)] = (LinkedSector) neighbour;
    this.exchangeWallInfo(atBearing);
    return this;
  }

  private int mapBearing(Bearing bearing) {
    // map bearing to our index (don't reuse internal Bearing representation)
    int index = 0; // default N = 0
    switch (bearing) {
      case E:
        index = 1;
        break;
      case S:
        index = 2;
        break;
      case W:
        index = 3;
        break;
    }
    return index;
  }

  // WATCH OUT: this method returns the new neighbour, not itself
  public LinkedSector createNeighbour(Bearing atBearing) {
    // if we already have a neighbour at this location, return it
    if (this.hasNeighbour(atBearing)) {
      return this.getNeighbour(atBearing);
    }
    // create empty sector and assign it coordinates relative to ours
//    int left = this.getLeft(), top = this.getTop();
//    switch (atBearing) {
//      case N:
//        top--;
//        break;
//      case E:
//        left++;
//        break;
//      case S:
//        top++;
//        break;
//      case W:
//        left--;
//        break;
//    }
    LinkedSector neighbour = new LinkedSector();//.setCoordinates(left, top);
    // add it to the grid, it will be connected to us and all other relevant
    // sectors
    // this.grid.addSector(neighbour);
    return neighbour;
  }

  private LinkedSector exchangeWallInfo(Bearing atBearing) {
    Sector neighbour = this.getNeighbour(atBearing);
    if (neighbour == null || neighbour == this) {
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
        System.err.println("Conflicting Wall information: " );//+ this.getLeft() + "," + this.getTop() + " / " + atBearing);
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
  public LinkedSector getNeighbour(Bearing atBearing) {
    if (atBearing == Bearing.UNKNOWN) {
      return this;
    }
    return this.neighbours[this.mapBearing(atBearing)];
  }

  // sets the value of the sector
  public LinkedSector setValue(int value) {
    //TODO: if there is an agent on this sector, the grid is affected
    this.value = value;
    return this;
  }

  // Gets the value if the sector
  public int getValue() {
    return this.value;
  }

  // adds a wall on this sector at given location
  public LinkedSector addWall(Bearing atBearing) {
    this.inheritWall(atBearing);
    // also set the wall at our neighbour's
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).inheritWall(atBearing.reverse());
    }
    // this.grid.wallsNeedRefresh();
    return this;
  }

  public void inheritWall(Bearing atBearing) {
    this.withWall(atBearing);
    // this.grid.wallsNeedRefresh();
  }

  // removes a wall from this sector at given location
  public LinkedSector removeWall(Bearing atBearing) {
    this.inheritNoWall(atBearing);
    // also remove the wall at our neighbour's
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).inheritNoWall(atBearing.reverse());
    }
    // this.grid.wallsNeedRefresh();
    return this;
  }

  public void inheritNoWall(Bearing atBearing) {
    this.withoutWall(atBearing);
    // this.grid.wallsNeedRefresh();
  }

  // adds all walls at once
  public LinkedSector addWalls(char walls) {
    this.withWalls(walls);
    // also update neighbours
    this.updateNeighboursWalls();

    // this.grid.wallsNeedRefresh();
    return this;
  }

  protected void updateNeighboursWalls() {
    for (Bearing atBearing : Bearing.values()) {
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
  public LinkedSector clearCertainty() {
    this.certainty = 0;
    return this;
  }

  // clears all knowledge about a wall
  public LinkedSector clearWall(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls = (char) BitwiseOperations.unsetBit(this.walls, bit);
    this.certainty = (char) BitwiseOperations.unsetBit(this.certainty, bit);
    return this;
  }

  // clears all knowledge about a wall
  public LinkedSector clearWalls() {
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

  public Sector withWall(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls = (char) BitwiseOperations.setBit(this.walls, bit);
    this.certainty = (char) BitwiseOperations.setBit(this.certainty, bit);
    return this;
  }

  public void withWalls(char walls) {
    this.walls = walls;
    this.certainty = 15;
  }

  public void withoutWall(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls = (char) BitwiseOperations.unsetBit(this.walls, bit);
    this.certainty = (char) BitwiseOperations.setBit(this.certainty, bit);
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
    for (int i = 0; i < 4; i++) {
      this.neighbours[i] = null;
    }
    grid = null;
  }

  private Bearing applyRotation(Bearing bearing) {
    return bearing.rotate(this.grid.getTransformation().getRotation().invert());
  }
}
