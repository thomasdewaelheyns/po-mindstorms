/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import java.io.File;
import java.util.Scanner;
import penoplatinum.pacman.GhostAgent;
import penoplatinum.pacman.PacmanAgent;
import penoplatinum.simulator.mini.Bearing;

/**
 *
 * @author MHGameWork
 */
public class GridFactory {

  static Grid g;

  public static Grid load(String fileName) {

    g = new SimpleGrid();

    try {
      File file = new File(fileName);
      Scanner scanner = new Scanner(file);
      loadWalls(scanner);
      loadAgentsAndTags(scanner);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return g;
  }

  // given a scanner-based file, load the walls
  private static void loadWalls(Scanner scanner) {
    // load walls
    int width = scanner.nextInt();
    int height = scanner.nextInt();
    System.out.println("loading grid: " + width + "x" + height);

    Sector currentSector;
    for (int top = 0; top < height; top++) {
      for (int left = 0; left < width; left++) {
        int v = scanner.nextInt();
        Sector sector = new Sector(g).setCoordinates(left, top).addWalls((char) v);
        g.addSector(sector); // add it to the grid, this will connect it
        // to its neighbours
      }
    }
  }

  // given a scanner-based file, load the agents/tags
  // at least one target agent should be available, else add one randomly
  // placed in the Grid
  private static void loadAgentsAndTags(Scanner scanner) {
    boolean haveTarget = false;

    while (scanner.hasNext()) {
      String type, name = "";
      int left, top, orientation;

      type = scanner.next();
      left = scanner.nextInt();
      top = scanner.nextInt();
      orientation = scanner.nextInt();
      Sector sector = g.getSector(left, top);
      if (sector != null) {
        Agent agent;
        if (type.equals("tag")) {
          String tag = scanner.next();
          sector.addTag(tag);
          
          //TODO: g.tags.put(tag, sector);
        } else {
          if (type.equals("ghost")) {
            name = scanner.next();
            agent = new GhostAgent(name);
          } else {
            agent = new PacmanAgent();
            haveTarget = true;
          }
          sector.put(agent, orientation);
        }
      }
    }

    // add at least 1 randomly placed target
    if (!haveTarget) {
      Sector sector = null;
      while (null == sector) {
        int left = (int) (Math.random() * g.getWidth());
        int top = (int) (Math.random() * g.getHeight());
        sector = g.getSector(left, top);
      }
      sector.put(new PacmanAgent(), Bearing.N);
    }
  }
}
