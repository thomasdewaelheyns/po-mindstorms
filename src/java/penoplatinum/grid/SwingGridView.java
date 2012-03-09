package penoplatinum.grid;

/**
 * SwingGridView
 * 
 * Renders a Grid using Swing/AWT, mostly handled by GridBoard
 * 
 * @author: Team Platinum
 */

import javax.swing.JFrame;

import penoplatinum.Color;
import penoplatinum.simulator.mini.Bearing;

public class SwingGridView extends JFrame implements GridView {
  private Grid   grid;
  private String title = "Grid";
  private int    left   = -1,
                 top    = -1,
                 width  = 0,
                 height = 0;

  private boolean refreshSectors = true;
  private boolean refreshValues  = true;
  private boolean refreshAgents  = true;

  private GridBoard board;
  
  public GridView display(Grid grid) {
    this.grid = grid;

    this.setupBoard();  // yes keep this order ;-)
    this.setupWindow(); // the board needs to be ready before we construct
                        // the window
    this.refresh();

    return this;
  }

  private void setupBoard() {
    this.board = new GridBoard();
    this.add(this.board);
  }
  
  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.refreshSize();
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
    this.refreshSize();
    boolean somethingChanged = false;
    
    if( this.refreshSectors ) {
      this.board.clearSectors();
      this.addSectors();
      this.addWalls();
      this.refreshSectors = false;
      somethingChanged = true;
    }
    
    if( this.refreshValues || this.refreshAgents ) {
      this.board.clearValues();
      this.addValues();
      this.refreshValues = false;
      somethingChanged = true;
    }
    
    if( this.refreshAgents ) {
      this.board.clearAgents();
      this.addAgents();
      this.refreshAgents = false;
      somethingChanged = true;
    }

    if( somethingChanged ) { this.board.render(); }
    return this;
  }
  
  private void addSectors() {
    int minLeft = this.grid.getMinLeft(),
        minTop  = this.grid.getMinTop();

    // add sectors
    for(Sector sector : this.grid.getSectors()) {
      this.board.addSector(sector.getLeft()-minLeft, sector.getTop()-minTop);
    }
  }

  private void addWalls() {
    int minLeft = this.grid.getMinLeft(),
        minTop  = this.grid.getMinTop();

    // add sectors
    for(Sector sector : this.grid.getSectors()) {
      for(int wall=Bearing.N; wall<=Bearing.W; wall++ ) {
        if( sector.isKnown(wall) && sector.hasWall(wall) ) {
          this.board.addWall(sector.getLeft()-minLeft, sector.getTop()-minTop, 
                             wall);
        }
      }
    }
  }
    
  private void addValues() {
    int minLeft = this.grid.getMinLeft(),
        minTop  = this.grid.getMinTop();

    for(Sector sector : this.grid.getSectors()) {
      this.board.addValue(sector.getLeft()-minLeft, sector.getTop()-minTop,
                          sector.getValue());
    }
  }

  private void addAgents() {
    int minLeft = this.grid.getMinLeft(),
        minTop  = this.grid.getMinTop();

    for( Agent agent : this.grid.getAgents() ) {
      final Color c = agent.getColor();
      this.board.addAgent(agent.getLeft()-minLeft, agent.getTop()-minTop, 
                          agent.getBearing(), agent.getName(), 
                          new java.awt.Color(c.getR(),c.getG(),c.getB()));
    }
  }
  
  public GridView sectorsNeedRefresh() {
    this.refreshSectors = true;
    return this;
  }

  public GridView valuesNeedRefresh() {
    this.refreshValues = true;
    return this;
  }

  public GridView agentsNeedRefresh() {
    this.refreshAgents = true;
    return this;
  }

  private void refreshSize() {
    int newWidth  = this.grid.getWidth(),
        newHeight = this.grid.getHeight();
    if( newWidth != this.width || newHeight != this.height ) {
      final int width = Math.max(newWidth  * GridBoard.SECTOR_SIZE -  2, 200);
      // set our own size
      this.setSize( width, 
                    newHeight * GridBoard.SECTOR_SIZE + 30 );
      // set the board's size
      if( this.board != null ) {
        this.board.resizeTo(this.grid.getWidth(), this.grid.getHeight());
      }
      this.width = newWidth;
      this.height = newHeight;
    }
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
