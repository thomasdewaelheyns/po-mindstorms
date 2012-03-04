import java.awt.Color;

public class GhostAgent extends MovingAgent {
  private Color color = new Color(255,255,255);

  public GhostAgent(String name) { super(name); }
  public GhostAgent(String name, Color color) {
    super(name);
    this.color = color;
  }
  
  public Color getColor() { return this.color; }
}
