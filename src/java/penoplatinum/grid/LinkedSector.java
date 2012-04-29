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
import penoplatinum.util.BitwiseOperations;

/**
 * This sector implementation relies on the fact that the entire grid composing
 * this sector does not contain holes!!
 * 
 * Note: this class should be able to work with generic 'Grid's, not only with
 * LinkedGrid
 * 
 * @author MHGameWork
 */
public class LinkedSector implements Sector {

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

  public LinkedSector(Sector original) {
    this();
    addWalls(((LinkedSector) original).walls);
  }
  
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

    if (getNeighbour(atBearing) == neighbour)
      return this;

    Sector left = null;
    Sector right = null;

    linkNeighbour(neighbour, atBearing);


    if (getNeighbour(atBearing.leftFrom()) != null)
      left = getNeighbour(atBearing.leftFrom()).getNeighbour(atBearing);

    if (getNeighbour(atBearing.rightFrom()) != null)
      right = getNeighbour(atBearing.rightFrom()).getNeighbour(atBearing);


    if (right != null)
      right.addNeighbour(neighbour, atBearing.leftFrom());
    if (left != null)
      left.addNeighbour(neighbour, atBearing.rightFrom());



    return this;
  }

  private void linkNeighbour(Sector neighbour, Bearing atBearing) {
    neighbours[mapBearing(atBearing)] = (LinkedSector) neighbour;
    if (neighbour == null)
      return;
    ((LinkedSector) neighbour).neighbours[mapBearing(atBearing.reverse())] = this;
    exchangeWallInfo(atBearing);

  }

  private LinkedSector exchangeWallInfo(Bearing atBearing) {
    LinkedSector neighbour = (LinkedSector) this.getNeighbour(atBearing);
    if (neighbour == null || neighbour == this) {
      return this;
    }

    Bearing locationAtNeighbour = atBearing.reverse();
    Boolean iHaveWall = !this.knowsWall(atBearing) ? null : this.hasWall(atBearing);
    Boolean neighbourHasWall = !neighbour.knowsWall(locationAtNeighbour) ? null : neighbour.hasWall(locationAtNeighbour);

    // if we have different information, we need to update it
    if (neighbourHasWall != iHaveWall) {
      if (neighbourHasWall == null) { // T/F != null
        if (iHaveWall) {
          neighbour.setWallInternal(locationAtNeighbour);
        } else {
          neighbour.setNoWallInternal(locationAtNeighbour);
        }
      } else if (iHaveWall == null) {   // null != T/F
        if (neighbourHasWall) {
          this.setWall(atBearing);
        } else {
          this.setNoWallInternal(atBearing);
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
    setWallInternal(atBearing);
    // also set the wall at our neighbour's
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).setWallInternal(atBearing.reverse());
    }
    // this.grid.wallsNeedRefresh();
    return this;
  }

  @Override
  public Sector setNoWall(Bearing atBearing) {
    setNoWallInternal(atBearing);
    // also remove the wall at our neighbour's
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).setNoWallInternal(atBearing.reverse());
    }
    // this.grid.wallsNeedRefresh();
    return this;
  }

  @Override
  public Sector clearWall(Bearing atBearing) {
    // clears all knowledge about a wal
    clearWallInternal(atBearing);
    // also update neighbour
    if (this.hasNeighbour(atBearing)) {
      this.getNeighbour(atBearing).clearWallInternal(atBearing.reverse());
    }
    return this;
  }

  @Override
  public boolean hasWall(Bearing wall) {
    if (!knowsWall(wall)) {
      throw new IllegalArgumentException();
    }
    int bit = this.mapBearing(wall);
    return BitwiseOperations.hasBit(this.walls, bit);
  }

  @Override
  public boolean hasNoWall(Bearing wall) {
    return !hasWall(wall);
  }

  @Override
  public boolean knowsWall(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    return BitwiseOperations.hasBit(this.certainty, bit);
  }

  public char getWalls() {
    return this.walls;
  }

  @Override
  public boolean isFullyKnown() {
    return this.certainty == 15;
  }

  public LinkedSector clearWalls() {
    this.walls = 0;
    this.certainty = 0;
    return this;
  }

  @Override
  public boolean givesAccessTo(Bearing atBearing) {
    if (!knowsWall(atBearing))
      return false;
    return !hasWall(atBearing);
  }

  public String toString() {
    return //"(" + getLeft() + "," + getTop() + ")" +
            "N" + (this.knowsWall(Bearing.N) ? (this.hasWall(Bearing.N) ? "Y" : " ") : "?")
            + "E" + (this.knowsWall(Bearing.E) ? (this.hasWall(Bearing.E) ? "Y" : " ") : "?")
            + "S" + (this.knowsWall(Bearing.S) ? (this.hasWall(Bearing.S) ? "Y" : " ") : "?")
            + "W" + (this.knowsWall(Bearing.W) ? (this.hasWall(Bearing.W) ? "Y" : " ") : "?");
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

  private void setWallInternal(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls = (char) BitwiseOperations.setBit(this.walls, bit);
    this.certainty = (char) BitwiseOperations.setBit(this.certainty, bit);
  }

  private void setNoWallInternal(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls = (char) BitwiseOperations.unsetBit(this.walls, bit);
    this.certainty = (char) BitwiseOperations.setBit(this.certainty, bit);
  }

  private void clearWallInternal(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.certainty = (char) BitwiseOperations.unsetBit(this.certainty, bit);
  }

  // adds all walls at once
  private LinkedSector addWalls(char walls) {
    this.walls = walls;
    certainty = 15;
    // also update neighbours (this is used in clone, 
    //      how are neighbours relevant?)
    this.updateNeighboursWalls();

    // this.grid.wallsNeedRefresh();
    return this;
  }

  protected void updateNeighboursWalls() {
    for (Bearing atBearing : Bearing.NESW) {
      LinkedSector neighbour = (LinkedSector) this.getNeighbour(atBearing);
      Boolean haveWall = !knowsWall(atBearing) ? null : this.hasWall(atBearing);
      if (neighbour != null && haveWall != null) {
        if (haveWall) {
          neighbour.setWallInternal(atBearing.reverse());
        } else {
          neighbour.setNoWallInternal(atBearing.reverse());
        }
      }
    }
  }

  private void disengage() {
    for (int i = 0; i < 4; i++) {
      this.neighbours[i] = null;
    }
    grid = null;
  }

  @Override
  public boolean hasSameWallsAs(Sector s) {
    if (s instanceof LinkedSector)
      return ((LinkedSector) s).walls == walls;

    for (Bearing b : Bearing.NESW) {

      if (s.knowsWall(b) != knowsWall(b))
        return false;
      if (s.knowsWall(b) && (s.hasWall(b) != hasWall(b)))
        return false;
    }
    
    return true;

  }
}
