public class MazeRunner {
  public static void main(String[] args) {
    SwingMazeView swing = new SwingMazeView();

    while(true) {
      Maze maze = Maze.load("test.maze");
      maze.displayOn(swing);

      // add agents
      maze.addAgent(new GhostAgent(0,0))
        .addAgent(new GhostAgent(9,0));

      // add random target
      maze.setTarget((int)(Math.random()*maze.getWidth()),
        (int)(Math.random()*maze.getHeight()));

      maze.show();

      CD cd = new CD();

      maze.moveAgents();
      while( ! maze.allAgentsAreHolding() ) {
        try { Thread.sleep(100); } catch(Exception e) {}
        cd.apply(maze);
        cd.apply(maze);
        cd.apply(maze);
        maze.moveAgents();
        maze.show();
      }
      System.out.println("All Agents Are Holding ... WIN!" );
      try { Thread.sleep(1500); } catch(Exception e) {}
    }
  }
}