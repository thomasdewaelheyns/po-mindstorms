package penoplatinum.grid;

import penoplatinum.util.Color;
import penoplatinum.util.Colors;

/**
 * ProxyAgent
 * 
 * Implements an Agent that is used in the MiniSimulator, representing the
 * Agents in the "real" world.
 * 
 * @author: Team Platinum
 */
// TODO: give the proxy a link to the actual agent
//       copy color, name, etc.
public class ProxyAgent extends MovingAgent {

  private Color color = Colors.GREY;

  public ProxyAgent(String name) {
    super(name);
  }

  public Color getColor() {
    return this.color;
  }

  @Override
  public Agent createCopy() {
    return new ProxyAgent(this.getName());
  }
}
