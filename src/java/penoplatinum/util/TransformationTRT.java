/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

import penoplatinum.simulator.Bearing;

/**
 * This class represents a translation + rotation + translation transformation
 * @author MHGameWork
 */
public class TransformationTRT {
  public static TransformationTRT Identity;
  
  static
  {
    Identity = new TransformationTRT();
    Identity.setTransformation(0, 0, 0, 0, 0);
  }

  private int translationAX;
  private int translationAY;
  private int translationBX;
  private int translationBY;
  private int rotation;

  public int getRotation() {
    return rotation;
  }

  public int getTranslationAX() {
    return translationAX;
  }

  public int getTranslationAY() {
    return translationAY;
  }

  public int getTranslationBX() {
    return translationBX;
  }

  public int getTranslationBY() {
    return translationBY;
  }

  public TransformationTRT setTransformation(int tAX, int tAY, int rotation, int tBX, int tBY) {
    translationAX = tAX;
    translationAY = tAY;
    this.rotation = rotation;
    translationBX = tBX;
    translationBY = tBY;

    return this;

  }

  public Point transform(int x, int y) {
    return transformCoordinate(x, y, translationAX, translationAY, rotation, translationBX, translationBY);
  }

  public static Point transformCoordinate(int x, int y, int localX, int localY, int rotation, int otherX, int otherY) {
    // Relative current other sector to otherx and y
    x = x - otherX;
    y = y - otherY;
    // Now rotate this vector
    Point p = Bearing.mapToNorth(rotation, x, y);
    // Now apply this vector to our coordinates
    p = new Point(p.getX() + localX, p.getY() + localY);
    return p;
  }
  
  public String toString(){
    return "Transformation: "+translationAX+","+translationAY+":"+rotation+":"+translationBX+","+translationBY;
  }
  
  
}
