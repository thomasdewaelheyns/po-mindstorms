package penoplatinum.simulator.mini;

/**
 * ShowMap
 * 
 * Shows a Map, applying the Values Diffusion.
 * 
 */

import penoplatinum.grid.Grid;
import penoplatinum.grid.SimpleGrid;
import penoplatinum.grid.view.GridView;
import penoplatinum.grid.view.SwingGridView;
import penoplatinum.grid.DiffusionGridProcessor;
import penoplatinum.grid.view.GridFactory;

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
    if( args.length < 2 || ! args[1].equals("--no-cd-update") ) {
      grid.refresh(); grid.refresh(); grid.refresh(); grid.refresh(); grid.refresh();
      grid.refresh(); grid.refresh(); grid.refresh(); grid.refresh(); grid.refresh();
    }
    
    grid.dump();

    System.out.println("*** press return to exit...");
    try { System.in.read(); } catch(Exception e) {}
    System.exit(0);
  }
}
