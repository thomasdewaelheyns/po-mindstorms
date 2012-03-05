package penoplatinum.pacman;

/**
 * PacmanAgent
 * 
 * Implementation of an Agent, extending a MovingAgent into a Pacman
 * 
 * @author: Team Platinum
 */

import penoplatinum.grid.MovingAgent;

import java.awt.Color;

public class PacmanAgent extends MovingAgent {
  private Color color = new Color(255,255,0);
  
  public PacmanAgent() { super("pacman"); }

  public int   getValue() { return 10000; }
  public Color getColor() { return this.color;   }
}
