package penoplatinum.simulator;

import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;

import penoplatinum.Config;

import penoplatinum.simulator.entities.SimulatedViewRobot;

public class ColorLink {

  private static ArrayList<Color> colors;
  private static HashMap<String, Color> link = new HashMap<String, Color>();
  private static HashMap<Color, Image> files;

  static {
    colors = fillColors();
    files = fillFiles();
  }

  private static void addFile(HashMap<Color, Image> temp, Color color, String ghostFile) {
    URL resource = ColorLink.class.getResource(ghostFile);
    ImageIcon ii = new ImageIcon(resource);
    SimulatedViewRobot.robot = ii.getImage();
    temp.put(color, ii.getImage());
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
    addFile(temp, Color.CYAN,   "../../" + Config.IMAGE_DIR + "/ghost_cyan.png"  );
    addFile(temp, Color.RED,    "../../" + Config.IMAGE_DIR + "/ghost_red.png"   );
    addFile(temp, Color.PINK,   "../../" + Config.IMAGE_DIR + "/ghost_pink.png"  );
    addFile(temp, Color.ORANGE, "../../" + Config.IMAGE_DIR + "/ghost_orange.png");
    return temp;
  }

  public synchronized static Color getColorByName(String name) {
    addName(name);
    return link.get(name);
  }

  private synchronized static void addName(String name) {
    if (link.get(name) != null) {
      return;
    }
    if (colors.isEmpty()) {
      link.put(name, Color.GRAY);
    } else {
      link.put(name, colors.get(0));
      colors.remove(0);
    }
  }

  public static Image getFileByColor(Color c) {
    return files.get(c);
  }
}
