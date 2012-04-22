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

  @Override
  public LinkedSector putOn(Grid grid) {
    this.grid = grid;
    return this;
  }

  @Override
  public Grid getGrid() {
    return this.grid;
  }

  @Override
  public Sector addNeighbour(Sector neighbour, Bearing atBearing) {

    Sector left = null;
    Sector right = null;


    if (neighbour.getNeighbour(atBearing.leftFrom()) != null) {
      left = neighbour.getNeighbour(atBearing.leftFrom()).getNeighbour(atBearing.reverse());
    }
    if (neighbour.getNeighbour(atBearing.rightFrom()) != null) {
      right = neighbour.getNeighbour(atBearing.rightFrom()).getNeighbour(atBearing.reverse());
    }

    linkNeighbour(neighbour, atBearing);
    linkNeighbour(left, atBearing.leftFrom());
    linkNeighbour(right, atBearing.rightFrom());






    return this;
  }

  private void linkNeighbour(Sector neighbour, Bearing atBearing) {
    neighbours[mapBearing(atBearing)] = (LinkedSector) neighbour;
    exchangeWallInfo(atBearing);

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
        System.err.println("Conflicting Wall information: ");//+ this.getLeft() + "," + this.getTop() + " / " + atBearing);
        this.clearWall(atBearing);
        neighbour.clearWall(locationAtNeighbour);
      }
    }
    return this;
  }

  @Override
  public boolean hasNeighbour(Bearing atBearing) {
    return this.getNeighbour(atBearing) != null;
  }

  // returns the neighbour at a given location
  @Override
  public LinkedSector getNeighbour(Bearing atBearing) {
    if (atBearing == Bearing.UNKNOWN) {
      return this;
    }
    return this.neighbours[this.mapBearing(atBearing)];
  }

  // sets the value of the sector
  @Override
  public LinkedSector setValue(int value) {
    //TODO: if there is an agent on this sector, the grid is affected
    this.value = value;
    return this;
  }
  // Gets the value if the sector

  @Override
  public int getValue() {
    return this.value;
  }

  @Override
  public Sector setWall(Bearing atBearing) {
    this.inheritWall(atBearing);
    // also set the wall at our neighbour's
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).inheritWall(atBearing.reverse());
    }
    // this.grid.wallsNeedRefresh();
    return this;
  }

  @Override
  public Sector setNoWall(Bearing atBearing) {
    this.inheritNoWall(atBearing);
    // also remove the wall at our neighbour's
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).inheritNoWall(atBearing.reverse());
    }
    // this.grid.wallsNeedRefresh();
    return this;
  }

  @Override
  public Sector clearWall(Bearing atBearing) {
    // clears all knowledge about a wal
  }

  @Override
  public boolean hasWall(Bearing wall) {
    if (!knowsWall(wall)) {
      throw new IllegalArgumentException();
    }
    int bit = this.mapBearing(this.applyRotation(atBearing));
    return BitwiseOperations.hasBit(this.walls, bit);
  }

  @Override
  public boolean hasNoWall(Bearing wall) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean knowsWall(Bearing atBearing) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public char getWalls() {
    return this.walls;
  }

  @Override
  public boolean isFullyKnown() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public LinkedSector clearWalls() {
    this.walls = 0;
    this.certainty = 0;
    return this;
  }

  @Override
  public boolean givesAccessTo(Bearing atBearing) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
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

  public void inheritWall(Bearing atBearing) {
    this.withWall(atBearing);
    // this.grid.wallsNeedRefresh();
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

  // clears the certainty information of the Sector
  public LinkedSector clearCertainty() {
    this.certainty = 0;
    return this;
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
