package penoplatinum.util;

/**
 * Color class to store RGB values, without pulling in the entire AWT stack.
 * 
 * @author Team Platinum
 */
 

public class Color {
  private final int rgb;

  public Color(byte r, byte g, byte b) {
    this.rgb = (r << 16) | (g << 8) | b;
  }

  public Color(int r, int g, int b) {
    this.rgb = (r << 16) | (g << 8) | b;
  }

  public int getB() {
    return rgb & 0xFF;
  }

  public int getG() {
    return (rgb >> 8) & 0xFF;
  }

  public int getR() {
    return (rgb >> 16) & 0xFF;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final Color other = (Color) obj;
    if (this.rgb != other.rgb)
      return false;
    return true;
  }

  
}
