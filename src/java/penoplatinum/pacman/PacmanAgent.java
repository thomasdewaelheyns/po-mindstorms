package penoplatinum.pacman;

/**
 * PacmanAgent
 * 
 * Implementation of an Agent, extending a MovingAgent into a Pacman
 * 
 * @author: Team Platinum
 */

import penoplatinum.Color;
import penoplatinum.grid.MovingAgent;


public class PacmanAgent extends MovingAgent {
  public static int VALUE = 10000;
  private Color color = new Color(255,255,0);
  
  public PacmanAgent() { super("pacman"); }

  public int   getValue() { return PacmanAgent.VALUE; }
  public Color getColor() { return this.color;   }
}
