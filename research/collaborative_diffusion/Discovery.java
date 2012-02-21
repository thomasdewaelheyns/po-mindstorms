public class Discovery {
  public static void main(String[] args) {
    MazeView goalView    = new SwingMazeView()
                            .changeTitle("Goal")
                            .changeLocation(100,100);
    MazeView currentView = new SwingMazeView()
                            .changeTitle("Current")
                            .changeLocation(300,100);

    Maze goalMaze    = Maze.load(args[0])
                           .displayOn(goalView)
                           .clearAgents()
                           .addAgent(new StaticTargetAgent(4,6, Baring.E))
                           .show();
    Maze currentMaze = Maze.create(goalMaze.getWidth(), goalMaze.getHeight())
                           .displayOn(currentView)
                           .show();

    Agent discoverer = new DiscoveryAgent(0,0, Baring.W, goalMaze);
    currentMaze.addAgent(discoverer);
    discoverer.setMaze(currentMaze); // only targets normally get this info

    while(true) {
      currentMaze.moveAgents();
      //goalMaze.show(); // no need to re-render the goal
      currentMaze.show(); // refresh our maze view to reflect movement
      try { Thread.sleep(400); } catch(Exception e) {}
    }
  }
}
