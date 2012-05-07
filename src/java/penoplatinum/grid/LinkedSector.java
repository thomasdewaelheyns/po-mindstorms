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
  private char walls = 0;
  private char certainty = 0;
  // the value associated with this sector
  private int value = 0;
  private int id = -2;

  public LinkedSector() {
    this.putOn(NullGrid.getInstance());
  }

  public LinkedSector(Sector original) {
    this();
    addWalls(((LinkedSector) original).walls);
  }

  public int getId() {
    return this.id;
  }

  public Sector setId(int value) {
    this.id = value;
    return this;
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

  public Sector addNeighbour(Sector neighbour, Bearing atBearing) {
    if (getNeighbour(atBearing) == neighbour)
      return this;

    LinkedSector left = null;
    LinkedSector right = null;

    linkNeighbour(neighbour, atBearing);

    if (getNeighbour(atBearing.leftFrom()) != null)
      left = (LinkedSector) getNeighbour(atBearing.leftFrom()).getNeighbour(atBearing);

    if (getNeighbour(atBearing.rightFrom()) != null)
      right = (LinkedSector) getNeighbour(atBearing.rightFrom()).getNeighbour(atBearing);

    if (right != null)
      right.addNeighbour(neighbour, atBearing.leftFrom());
    if (left != null)
      left.addNeighbour(neighbour, atBearing.rightFrom());
    return this;
  }

  private void linkNeighbour(Sector neighbour, Bearing atBearing) {

//    neighbours[mapBearing(atBearing)] = (LinkedSector) neighbour;
    if (neighbour == null)
      return;
//    ((LinkedSector) neighbour).neighbours[mapBearing(atBearing.reverse())] = this;
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
        //System.err.println("Conflicting Wall information: ");//+ this.getLeft() + "," + this.getTop() + " / " + atBearing);
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
    return (LinkedSector) grid.getSector(grid.getNeighbourId(this.id, atBearing));
//    if (atBearing == Bearing.UNKNOWN) {
//      return this;
//    }
//    return this.neighbours[this.mapBearing(atBearing)];
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
    LinkedSector n = getNeighbour(atBearing);
    if (n != null)
      n.setWallInternal(atBearing.reverse());
    return this;
  }

  @Override
  public Sector setNoWall(Bearing atBearing) {
    setNoWallInternal(atBearing);
    LinkedSector n = getNeighbour(atBearing);
    if (n != null)
      n.setNoWallInternal(atBearing.reverse());
    return this;
  }

  @Override
  public Sector clearWall(Bearing atBearing) {
    clearWallInternal(atBearing);
    LinkedSector n = getNeighbour(atBearing);
    if (n != null)
      n.clearWallInternal(atBearing.reverse());
    return this;
  }

  @Override
  public boolean hasWall(Bearing wall) {
    if (!this.knowsWall(wall)) {
      //throw new IllegalArgumentException("Don't know wall: " + wall);
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

  public String toString() {
    return GridUtils.createSectorWallsString(this);
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

  public void setWallInternal(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls = (char) BitwiseOperations.setBit(this.walls, bit);
    this.certainty = (char) BitwiseOperations.setBit(this.certainty, bit);
  }

  public void setNoWallInternal(Bearing atBearing) {
    int bit = this.mapBearing(atBearing);
    this.walls = (char) BitwiseOperations.unsetBit(this.walls, bit);
    this.certainty = (char) BitwiseOperations.setBit(this.certainty, bit);
  }

  public void clearWallInternal(Bearing atBearing) {
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

}
