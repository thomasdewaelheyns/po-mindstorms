public class ShowMap {
  public static void main(String[] args) {
    if( args.length < 1 ) {
      System.out.println("please provide a map filename...");
      System.exit(0);
    }

    GridView view = new SwingGridView().changeTitle(args[0]);
    Grid     grid = new Grid().load(args[0])
                              .displayOn(view);
    
    // 10x results in a stable CD
    CD.apply(grid); CD.apply(grid); CD.apply(grid); CD.apply(grid); CD.apply(grid);
    CD.apply(grid); CD.apply(grid); CD.apply(grid); CD.apply(grid); CD.apply(grid);
    grid.show();
    
    grid.dump();

    System.out.println("*** press return to exit...");
    try { System.in.read(); } catch(Exception e) {}
    System.exit(0);
  }
}
