package penoplatinum.simulator;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author Penoplatinum
 */
public class ColorLink {

  static ArrayList<Color> colors;
  static HashMap<String, Color> link = new HashMap<String, Color>();
  static HashMap<Color, Image> files;

  static {
    colors = fillColors();
    files = fillFiles();
  }

  private static ArrayList<Color> fillColors() {
    ArrayList<Color> temp = new ArrayList<Color>();
    temp.add(Color.CYAN);
    temp.add(Color.RED);
    temp.add(Color.PINK);
    temp.add(Color.ORANGE);
    return temp;
  }

  private static HashMap<Color, Image> fillFiles() {
    HashMap<Color, Image> temp = new HashMap<Color, Image>();
    URL resource = ColorLink.class.getResource("images/ghost_cyan.png");
    System.out.println(resource.toString());
    ImageIcon ii = new ImageIcon(resource);
    SimulatedViewRobot.robot = ii.getImage();
    temp.put(Color.CYAN, ii.getImage());
    resource = ColorLink.class.getResource("images/ghost_red.png");
    ii = new ImageIcon(resource);
    temp.put(Color.RED, ii.getImage());
    resource = ColorLink.class.getResource("images/ghost_pink.png");
    ii = new ImageIcon(resource);
    temp.put(Color.PINK, ii.getImage());
    resource = ColorLink.class.getResource("images/ghost_orange.png");
    ii = new ImageIcon(resource);
    temp.put(Color.ORANGE, ii.getImage());
    return temp;
  }

  public static Color getColorByName(String name) {
    if (link.get(name) == null) {
      addName(name);
    }
    return link.get(name);
  }

  public static void addName(String name) {
    if (colors.isEmpty()) {
      throw new IllegalArgumentException("Cannot add another robot.");
    }
    link.put(name, colors.get(0));
    colors.remove(0);
  }

  public static Image getFileByColor(Color c) {
    return files.get(c);
  }
}
