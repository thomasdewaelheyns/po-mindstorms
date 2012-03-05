package penoplatinum.grid;

/**
 * ProxyAgent
 * 
 * Implements an Agent that is used in the MiniSimulator, representing the
 * Agents in the "real" world.
 * 
 * @author: Team Platinum
 */

import java.awt.Color;

// TODO: give the proxy a link to the actual agent
//       copy color, name, etc.
public class ProxyAgent extends MovingAgent {
  private Color color = new Color(200,200,200);

  public ProxyAgent(String name) { super(name); }
  public Color getColor() { return this.color; }
}
