public class MazeRunner {
  public static void main(String[] args) {
    SwingMazeView swing = new SwingMazeView();

    while(true) {
      Maze maze = Maze.load(args[0]).displayOn(swing).show();

      CD cd = new CD();

      while( ! maze.targetIsBlocked() ) {
        cd.apply(maze);
        cd.apply(maze);
        cd.apply(maze);

        maze.moveAgents();

        maze.show();
        try { Thread.sleep(200); } catch(Exception e) {}
      }

      System.out.println( "Target is blocked ... Ghosts WIN!" );

      try { Thread.sleep(1500); } catch(Exception e) {}
    }
  }
}