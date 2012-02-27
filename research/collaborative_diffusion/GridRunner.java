public class GridRunner {
  public static void main(String[] args) {
    SwingGridView swing = new SwingGridView();

    while(true) {
      Grid grid = new Grid().load(args[0]).displayOn(swing).show();

      while( ! grid.targetIsBlocked() ) {
        CD.apply(grid);
        grid.moveAgents();
        grid.show();
        try { Thread.sleep(200); } catch(Exception e) {}
      }

      System.out.println( "Target is blocked ... Ghosts WIN!" );

      try { Thread.sleep(1500); } catch(Exception e) {}
    }
  }
}
