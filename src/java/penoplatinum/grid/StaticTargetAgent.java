package penoplatinum.grid;

/**
 * StaticTargetAgent
 * 
 * Agent used in the MiniSimulator to represent a not-moving target.
 * 
 * @author: Team Platinum
 */


import penoplatinum.util.Color;
import penoplatinum.util.Colors;

public class StaticTargetAgent implements Agent {
  
  // our color
  private Color color = Colors.YELLOW;

  public String getName()   { return "target"; }
  public int    getValue()  { return 10000; }
  public Color  getColor()  { return this.color; }


  @Override
  public Agent activate() {
    return this;
  }

  @Override
  public boolean isActive() {
    return false;
  }
}
