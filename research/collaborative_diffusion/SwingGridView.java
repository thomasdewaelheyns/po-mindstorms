import javax.swing.JFrame;

public class SwingGridView extends JFrame implements GridView {
  private Grid grid;
  private String title = "Grid";
  private int left = -1, top = -1;

  private GridBoard board;

  public GridView show(Grid grid) {
    this.grid = grid;
    this.setupBoard();
    this.setupWindow(); // yes keep this order ;-)
    return this;
  }
  
  public GridView refreshWalls() {
    this.board.clearWalls();
    this.addWalls();
    this.refresh();
    return this;
  }

  private void setupBoard() {
    this.board = new GridBoard();
    this.refreshSize();
    this.add(this.board);
  }
  
  public GridView refreshSize() {
    this.board.resizeTo(this.grid.getWidth(), this.grid.getHeight());
    // after a resize, we need to redraw the walls
    this.addWalls();
    return this;
  }
  
  private void addWalls() {
    int minLeft = this.grid.getMinLeft();
    int minTop  = this.grid.getMinTop();
    for(int top=this.grid.getMinTop(); top<=this.grid.getMaxTop(); top++) {
      for(int left=this.grid.getMinLeft(); left<=this.grid.getMaxLeft(); left++ ) {
        for(int wall=Bearing.N; wall<=Bearing.W; wall++ ) {
          Sector sector = this.grid.getSector(left, top);
          if( sector != null ) {
            Boolean hasWall = sector.hasWall(wall);
            if( hasWall != null && hasWall ) {
              this.board.addWall(left-minLeft, top-minTop, wall);
            }
          }
        }
      }
    }
  }
  
  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize( this.grid.getWidth() * 20 -2, this.grid.getHeight() * 20 + 20);
    if( this.left > 0 ) {
      this.setLocation(this.left, this.top);
    } else {
      this.setLocationRelativeTo(null);
    }
    this.setTitle(this.title);
    this.setResizable(false);
    this.setVisible(true);
  }

  public GridView refresh() {
    this.setSize( this.grid.getWidth()  * 20 -  2, 
                  this.grid.getHeight() * 20 + 20);

    this.board.start();

    int minLeft = this.grid.getMinLeft(),
        minTop  = this.grid.getMinTop();

    // add scent/height values
    for(int top=this.grid.getMinTop(); top<=this.grid.getMaxTop(); top++) {
      for(int left=this.grid.getMinLeft(); left<=this.grid.getMaxLeft(); left++ ) {
        Sector sector = this.grid.getSector(left,top);
        if( sector != null ) {
          this.board.setValue(left-minLeft, top-minTop, sector.getValue() );
        }
      }
    }

    // add agent positions
    for( Agent agent : this.grid.getAgents() ) {
      this.board.setAgent(agent.getLeft()-minLeft, agent.getTop()-minTop, 
                          agent.getOrientation(), agent.isTarget());
    }
    
    this.board.render();
    return this;
  }

  public GridView changeTitle(String title) { 
    this.title = title;
    return this;
  }
  
  public GridView changeLocation(int left, int top) {
    this.left = left;
    this.top  = top;
    return this;
  }
}
