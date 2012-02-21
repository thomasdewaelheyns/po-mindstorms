public class CD {
  public static void apply(Maze maze) {
    int width  = maze.getWidth();
    int height = maze.getHeight();
    
    for( int top=0; top<height; top++ ) {
      for( int left=0; left<width; left++ ) {
        int v = maze.get(left, top);
        if( v != 1000 ) {
          if( maze.hasHuntingAgentOn(left,top) ) {
            maze.set(left, top, 0);
          } else {
            int n = maze.hasWall(left, top, Baring.N) ? 0 : maze.get(left, top-1);
            int s = maze.hasWall(left, top, Baring.S) ? 0 : maze.get(left, top+1);
            int w = maze.hasWall(left, top, Baring.W) ? 0 : maze.get(left-1, top);
            int e = maze.hasWall(left, top, Baring.E) ? 0 : maze.get(left+1, top);
            maze.set(left, top, (int)((n+s+e+w)/4));
          }
        }
      }
    }
  }
}
