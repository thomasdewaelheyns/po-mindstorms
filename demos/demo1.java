/**
 * Demo 1
 * Robot runs a polygon. Number of corners and length of vertex is entered
 * through the robot's LCD and button interface.
 * 
 * Author: Team Platinum
 */
package penoplatinum.demo;

import penoplatinum.veelhoek.Veelhoek;
import penoplatinum.movement.RotationMovement;

public class demo1 {
  public static void main(String[] args) {
    Veelhoek v = new Veelhoek(new RotationMovement());
    v.run();
  }
}
