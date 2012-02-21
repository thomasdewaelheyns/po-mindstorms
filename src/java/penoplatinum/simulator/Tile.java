/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator;

import java.awt.Point;

/**
 *
 * @author Thomas
 */
public interface Tile {

  int getBarcode();

  int getColorAt(int x, int y);

  Boolean hasWall(int location);

  int toInteger();

  String toString();
  
  int getSize();
  
    
}
