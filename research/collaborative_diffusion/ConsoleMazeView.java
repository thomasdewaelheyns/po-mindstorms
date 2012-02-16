public class ConsoleMazeView implements MazeView {
  private Maze maze;
  
  public MazeView show(Maze maze) {
    this.maze = maze;
    return this;
  }
  
  public MazeView refresh() {
    if( this.maze == null ) { return this; }
    
    // clear screen (handy for basic "animation"
    for(int clear=0; clear<150; clear++ ) { System.out.println(""); }

    int width  = this.maze.getWidth();
    int height = this.maze.getHeight();

    // draw the maze using it's internal values
    for(int top=0; top<height; top++) {
      for(int left=0; left<width; left++ ) {
        System.out.printf( "%5d ", this.maze.getValue(left,top) );
      }
      System.out.println("");
    }
    return this;
  }
}
