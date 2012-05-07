package penoplatinum.grid.agent;

/**
 * MovingAgent abstract base class
 * 
 * Abstract base class for Agents. It implements all the common logic and
 * provides methods that can be overridden to implement Agent-specific details
 * 
 * @author: Team Platinum
 */


public abstract class MovingAgent implements Agent {

  private String  name;
  private boolean active  = false;

  public MovingAgent(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public int getValue() {
    return -1;
  }

  @Override
  public Agent activate() {
    this.active = true;
    return this;
  }
  
  @Override
  public boolean isActive() {
    return this.active;
  }
}
