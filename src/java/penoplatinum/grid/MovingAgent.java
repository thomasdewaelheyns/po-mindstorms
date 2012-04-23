package penoplatinum.grid;

/**
 * MovingAgent abstract base class
 * 
 * Abstract base class for Agents. It implements all the common logic and
 * provides methods that can be overridden to implement Agent-specific details
 * 
 * @author: Team Platinum
 */

import penoplatinum.util.Bearing;


public abstract class MovingAgent implements Agent {

  private String  name;
  private Grid    grid;
  private Bearing bearing = Bearing.N;
  private boolean active  = false;

  public MovingAgent(String name) {
    this.name = name;
  }

  public Agent assignGrid(Grid grid) {
    this.grid = grid;
    return this;
  }
  
  public Grid getGrid() {
    return this.grid;
  }

  public String getName() {
    return this.name;
  }

  public int getValue() {
    return -1;
  }

  public Bearing getBearing() {
    return this.bearing;
  }

  public Agent turnLeft() {
    this.bearing = this.bearing.leftFrom();
    return this;
  }

  public Agent turnRight() {
    this.bearing = this.bearing.rightFrom();
    return this;
  }

  public Agent moveForward() {
//    this.grid.moveForward(this);
    throw new UnsupportedOperationException();
  }
  
  public Agent activate() {
    this.active = true;
    return this;
  }
  
  public boolean isActive() {
    return this.active;
  }
  
  public Agent createCopy()
  {
    throw new UnsupportedOperationException();
  }
}
