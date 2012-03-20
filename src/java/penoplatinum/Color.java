/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

/**
 *
 * @author MHGameWork
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
    return (rgb >> 16) & 0XFF;
  }
}
