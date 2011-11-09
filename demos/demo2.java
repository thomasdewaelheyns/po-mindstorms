/**
 * demo 2
 *
 * Robot is able to
 * 1) follow a line
 * 2) follow a wall
 * 3) read and interprete barcodes
 * It is controled through the Console.
 * 
 * Author: Team Platinum
 */
package penoplatinum.demo;

import penoplatinum.demo.SensorDemo;

public class demo2 {
  public static void main(String[] args) {
    try {
      SensorDemo.main(null);
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
