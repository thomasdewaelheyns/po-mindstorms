import javax.swing.JFrame;

public class SwingMazeView extends JFrame implements MazeView {
  private Maze maze;

  private MazeBoard board;

  public MazeView show(Maze maze) {
    this.maze = maze;
    this.setupBoard();
    this.setupWindow();
    return this;
  }
  
  private void setupBoard() {
    this.board = new MazeBoard(this.maze.getWidth(), this.maze.getHeight());
    this.add(this.board);
    
    // add walls
    for(int top=0; top<this.maze.getHeight(); top++) {
      for(int left=0; left<this.maze.getWidth(); left++ ) {
        if( maze.hasWall(left, top, Baring.N) ) {
          this.board.addWall(left, top, Baring.N );
        }
        if( maze.hasWall(left, top, Baring.E) ) {
          this.board.addWall(left, top, Baring.E );
        }
        if( maze.hasWall(left, top, Baring.S) ) {
          this.board.addWall(left, top, Baring.S );
        }
        if( maze.hasWall(left, top, Baring.W) ) {
          this.board.addWall(left, top, Baring.W );
        }
      }
    }
  }

  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize( this.maze.getWidth() * 20 -2, this.maze.getHeight() * 20 + 20);
    this.setLocationRelativeTo(null);
    //setLocation(0,0);
    this.setLocationRelativeTo(null);
    this.setTitle("Maze");
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
      this.board.setAgent(agent.getLeft(), agent.getTop());
    }
    
    this.board.render();
    return this;
  }
}
