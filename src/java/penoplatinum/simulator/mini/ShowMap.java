package penoplatinum.simulator.mini;

/**
 * ShowMap
 * 
 * Shows a Map, applying the Values Diffusion.
 * 
 */

import penoplatinum.grid.Grid;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.grid.GridView;
import penoplatinum.grid.SwingGridView;
import penoplatinum.grid.DiffusionGridProcessor;
import penoplatinum.grid.GridFactory;

public class ShowMap {
  public static void main(String[] args) {
    if( args.length < 1 ) {
      System.out.println("please provide a map filename...");
      System.exit(0);
    }

    GridView view = new SwingGridView().changeTitle(args[0]);
    Grid     grid = GridFactory.load(args[0])
                                    .setProcessor(new DiffusionGridProcessor())
                                    .displayOn(view);
    
    // 10x results in a stable CD
    grid.refresh(); grid.refresh(); grid.refresh(); grid.refresh(); grid.refresh();
    grid.refresh(); grid.refresh(); grid.refresh(); grid.refresh(); grid.refresh();
    
    grid.dump();

    System.out.println("*** press return to exit...");
    try { System.in.read(); } catch(Exception e) {}
    System.exit(0);
  }
}
