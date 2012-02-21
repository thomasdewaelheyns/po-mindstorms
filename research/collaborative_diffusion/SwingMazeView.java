import javax.swing.JFrame;

public class SwingMazeView extends JFrame implements MazeView {
  private Maze maze;
  private String title = "Maze";
  private int left = -1, top = -1;

  private MazeBoard board;

  public MazeView show(Maze maze) {
    this.maze = maze;
    this.setupBoard();
    this.setupWindow(); // yes keep this order ;-)
    return this;
  }
  
  public MazeView refreshWalls() {
    this.board.clearWalls();
    this.addWalls();
    this.refresh();
    return this;
  }

  private void setupBoard() {
    this.board = new MazeBoard(this.maze.getWidth(), this.maze.getHeight());
    this.add(this.board);
    this.addWalls();
  }
  
  private void addWalls() {
    // add walls
    for(int top=0; top<this.maze.getHeight(); top++) {
      for(int left=0; left<this.maze.getWidth(); left++ ) {
        for(int wall=Baring.N; wall<=Baring.W; wall++ ) {
          Boolean hasWall = this.maze.hasWall(left, top, wall);
          if( hasWall != null && hasWall ) {
            this.board.addWall(left, top, wall );
          }
        }
      }
    }
  }
  
  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize( this.maze.getWidth() * 20 -2, this.maze.getHeight() * 20 + 20);
    if( this.left > 0 ) {
      this.setLocation(this.left, this.top);
    } else {
      this.setLocationRelativeTo(null);
    }
    this.setTitle(this.title);
    this.setResizable(false);
    this.setVisible(true);
  }

  public MazeView refresh() {
    int width  = this.maze.getWidth();
    int height = this.maze.getHeight();

    this.board.start();

    // add scent/hight values
    for(int top=0; top<height; top++) {
      for(int left=0; left<width; left++ ) {
        this.board.setValue(left, top, maze.getRaw(left,top) );
      }
    }

    // add agent positions
    for( Agent agent : this.maze.getAgents() ) {
      this.board.setAgent(agent.getLeft(), agent.getTop(), 
                          agent.getOrientation(), agent.isTarget());
    }
    
    this.board.render();
    return this;
  }

  public MazeView changeTitle(String title) { 
    this.title = title;
    return this;
  }
  
  public MazeView changeLocation(int left, int top) {
    this.left = left;
    this.top  = top;
    return this;
  }
}
