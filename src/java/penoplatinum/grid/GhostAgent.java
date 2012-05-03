package penoplatinum.grid;

/**
 * GhostAgent
 * 
 * Implementation of an Agent, extending a MovingAgent into a Ghost
 * 
 * @author: Team Platinum
 */

import penoplatinum.util.Color;
import penoplatinum.util.Colors;


public class GhostAgent extends MovingAgent {

  private Color color = Colors.WHITE;

  public GhostAgent(String name) { super(name); }

  public GhostAgent(String name, Color color) {
    super(name);
    this.color = color;
  }
  
  public Color getColor() { return this.color; }

}
