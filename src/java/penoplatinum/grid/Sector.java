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

import java.util.List;
import java.util.ArrayList;

import penoplatinum.simulator.mini.Bearing;

public class Sector {
  // back-link to the Grid we live in
  private Grid grid;
  
  // links to the adjacent Sectors
  private Sector[] neighbours = new Sector[4];

  // position within Grid
  private int left, top;

  // walls and the certainty about them
  private Tile walls     = new Tile();
  private Tile certainty = new Tile();

  // the agent currently on this Sector
  private Agent agent;

  // the value associated with this sector
  private int value = 0;
  
  private List<String> tags = new ArrayList<String>();


  public Sector() {
    this.putOn(new SimpleGrid()); // a dummy one, untill we get one assigned
  }
  
  public Sector(Grid grid) {
    this.putOn(grid);
  }
  
  public String toString() {
    return 
    " N : " + (this.isKnown(Bearing.N) ? (this.hasWall(Bearing.N) ? "Y" : " " ) : "?" ) + "\n" +
    " E : " + (this.isKnown(Bearing.E) ? (this.hasWall(Bearing.E) ? "Y" : " " ) : "?" ) + "\n" +
    " S : " + (this.isKnown(Bearing.S) ? (this.hasWall(Bearing.S) ? "Y" : " " ) : "?" ) + "\n" +
    " W : " + (this.isKnown(Bearing.W) ? (this.hasWall(Bearing.W) ? "Y" : " " ) : "?" ) + "\n";
  }

  // sets the absolute coordinates in the Grid this Sector is placed in
  public Sector setCoordinates(int left, int top) {
    this.left = left;
    this.top  = top;
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
    // if we have an agent, we notify this to our (new) grid
    if( this.hasAgent() ) { this.grid.addAgent(this.getAgent()); }
    return this;
  }
  
  public Grid getGrid() {
    return this.grid;
  }
  
  // adds a neighbour at a given location
  public Sector addNeighbour(Sector neighbour, int atLocation) {
    this.neighbours[atLocation] = neighbour;
    this.exchangeWallInfo(atLocation);
    return this;
  }
  
  // WATCH OUT: this method returns the new neighbour, not itself
  public Sector createNeighbour(int atLocation) {
    // if we already have a neighbour at this location, return it
    if(this.hasNeighbour(atLocation)) { return this.getNeighbour(atLocation);}
    // create empty sector and assign it coordinates relative to ours
    int left = this.getLeft(), top = this.getTop();
    switch(atLocation) {
      case Bearing.N: top--;  break;
      case Bearing.E: left++; break;
      case Bearing.S: top++;  break;
      case Bearing.W: left--; break;
    }
    Sector neighbour = new Sector().setCoordinates(left, top);
    // add it to the grid, it will be connected to us and all other relevant
    // sectors
    this.grid.addSector(neighbour);
    return neighbour;
  }
  
  private Sector exchangeWallInfo(int atLocation) {
    Sector  neighbour = this.neighbours[atLocation];
    if( neighbour == null ) { return this; }
    
    int     locationAtNeighbour = Bearing.reverse(atLocation);
    Boolean iHaveWall           = this.hasWall(atLocation);
    Boolean neighbourHasWall    = neighbour.hasWall(locationAtNeighbour);

    // if we have different information, we need to update it
    if( neighbourHasWall != iHaveWall ) {
      if( neighbourHasWall == null ) { // T/F != null
        if(iHaveWall) { neighbour.inheritWall(locationAtNeighbour);   }
                 else { neighbour.inheritNoWall(locationAtNeighbour); }
      } else if( iHaveWall == null ) {   // null != T/F
        if(neighbourHasWall) { this.inheritWall(atLocation);   }
                        else { this.inheritNoWall(atLocation); }
      } else {                           // T/F != F/T
        // conflicting information => clear both, go back to unknown state
        System.err.println( "Conflicting Wall information: " + this.getLeft() + "," + this.getTop() + " / " + atLocation );
        this.clearWall(atLocation);
        neighbour.clearWall(locationAtNeighbour);
      }
    }
    return this;
  }
  
  public boolean hasNeighbour(int atLocation) {
    return this.getNeighbour(atLocation) != null;
  }

  // returns the neighbour at a given location
  public Sector getNeighbour(int atLocation) {
    if( atLocation == Bearing.NONE ) { return this; }
    return this.neighbours[atLocation];
  }

  // keeps track of an agent occupying this sector
  public Sector put(Agent agent, int bearing) {
    // reset the value to zero, because an agent has its own value
    this.setValue(0);
    // now add the agent
    this.agent = agent;
    this.agent.assignSector(this, bearing);
    this.grid.addAgent(this.agent);
    return this;
  }

  public boolean hasAgent() {
    return this.agent != null;
  }
  
  public Agent getAgent() {
    return this.agent;
  }
  
  // removes an agent
  public Sector removeAgent() {
    this.agent = null;
    return this;
  }
  
  // sets the value of the sector
  public Sector setValue(int value) {
    // if there is an agent on the sector, we don't change the value
    if( this.hasAgent() ) { return this; }
    if( value != this.value ) {
      this.value = value;
      this.grid.valuesNeedRefresh();
    }
    return this;
  }

  // if an agent is occupying us, return the agent's value else our own
  public int getValue() {
    return this.agent != null ? this.agent.getValue() : this.value;
  }
  
  public Sector addTag(String tag) {
    this.tags.add(tag);
    return this;
  }
  
  // adds a wall on this sector at given location
  public Sector addWall(int atLocation) {
    this.inheritWall(atLocation);
    // also set the wall at our neighbour's
    if( this.hasNeighbour(atLocation) ) {
      this.getNeighbour(atLocation).inheritWall(Bearing.reverse(atLocation));
    }
    this.grid.wallsNeedRefresh();
    return this;
  }
  
  protected void inheritWall(int atLocation) {
    this.walls.withWall(atLocation);
    this.certainty.withWall(atLocation);
    this.grid.wallsNeedRefresh();
  }

  // removes a wall from this sector at given location
  public Sector removeWall(int atLocation) {
    this.inheritNoWall(atLocation);
    // also remove the wall at our neighbour's
    if( this.hasNeighbour(atLocation) ) {
      this.getNeighbour(atLocation).inheritNoWall(Bearing.reverse(atLocation));
    }
    this.grid.wallsNeedRefresh();
    return this;
  }
  
  protected void inheritNoWall(int atLocation) {
    this.walls.withoutWall(atLocation);
    this.certainty.withWall(atLocation);
    this.grid.wallsNeedRefresh();
  }

  // adds all walls at once
  public Sector addWalls(char walls) {
    this.walls.withWalls(walls);
    this.certainty.withWalls((char)15); // mark all walls as certain
    // also update neighbours
    this.updateNeighboursWalls();

    this.grid.wallsNeedRefresh();
    return this;
  }
  
  protected void updateNeighboursWalls() {
    for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
      Sector neighbour = this.getNeighbour(atLocation);
      Boolean haveWall = this.hasWall(atLocation);
      if( neighbour != null && haveWall != null ) {
        if( haveWall ) {
          neighbour.inheritWall(Bearing.reverse(atLocation));
        } else {
          neighbour.inheritNoWall(Bearing.reverse(atLocation));
        }
      }
    }
  }
  
  // we use the Boolean class here to be able to return "null" when we don't
  // know anything about the wall.
  public Boolean hasWall(int wall) {
    return this.certainty.hasWall(wall) ? this.walls.hasWall(wall) : null;
  }

  // returns true if there is NO wall and we absolutely can move forward
  // that way
  public boolean givesAccessTo(int location) {
    return this.certainty.hasWall(location) && 
          ! this.walls.hasWall(location);
  }
  
  // returns all wall configuration
  public char getWalls() {
    return this.walls.getWalls();
  }

  // clears the certainty information of the Sector
  public Sector clearCertainty() {
    this.certainty.clear();
    return this;
  }
  
  // clears all knowledge about a wall
  public Sector clearWall(int atLocation) {
    this.walls.withoutWall(atLocation);
    this.certainty.withoutWall(atLocation);
    return this;
  }

  // clears all knowledge about a wall
  public Sector clearWalls() {
    this.walls.clear();
    this.certainty.clear();
    return this;
  }
  
  // returns the certainty information about the Sector
  public int getCertainty() {
    return this.certainty.getWalls();
  }

  // determines if the wall at given location is known (to be there or not)
  public boolean isKnown(int atLocation) {
    return this.certainty.hasWall(atLocation);
  }
  
  public boolean isFullyKnown() {
    return this.getCertainty() == 15;
  }
}
