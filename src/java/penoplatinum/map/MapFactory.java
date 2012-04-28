package penoplatinum.map;

import java.util.Scanner;

public interface MapFactory {

  /**
   * Returns the map interpreted from the given Scanner
   * @param sc The scanner to build the map from
   * @return The map
   */
  Map getMap(Scanner sc);
  
}
