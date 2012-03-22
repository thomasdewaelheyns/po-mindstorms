
package penoplatinum.simulator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Penoplatinum
 */
public class ColorLink {
  
  static ArrayList<Color> colors = fillColors();
  static HashMap<String, Color> link = new HashMap<String, Color>();
  
  private static ArrayList<Color> fillColors(){
    ArrayList<Color> temp = new ArrayList<Color>();
    temp.add(Color.CYAN);
    temp.add(Color.RED);
    temp.add(Color.PINK);
    temp.add(Color.ORANGE);
    return temp;
  }
  
  public static Color getColorByName(String name){
    return link.get(name);
  }
  
  public static void addName(String name){
    if(colors.isEmpty())
      throw new IllegalArgumentException("Cannot add another robot.");
    link.put(name, colors.get(0));
    colors.remove(0);
  }
  
}
