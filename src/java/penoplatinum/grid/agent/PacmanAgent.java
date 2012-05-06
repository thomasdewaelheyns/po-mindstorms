package penoplatinum.grid.agent;

/**
 * PacmanAgent
 * 
 * Implementation of an Agent, extending a MovingAgent into a Pacman
 * 
 * @author: Team Platinum
 */

import penoplatinum.util.Color;
import penoplatinum.util.Colors;


public class PacmanAgent extends MovingAgent {
  
  public static int VALUE =-1;// 10000;
  private Color color = Colors.YELLOW;
  
  public PacmanAgent() { super("pacman"); }

  public int   getValue() { return PacmanAgent.VALUE; }
  public Color getColor() { return this.color;   }

}
