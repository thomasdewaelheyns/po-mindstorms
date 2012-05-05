package penoplatinum.grid;

/**
 * SwingGridView
 * 
 * Renders a Grid using Swing/AWT, mostly handled by GridBoard
 * 
 * @author: Team Platinum
 */
import javax.swing.JFrame;

import penoplatinum.util.Bearing;
import penoplatinum.simulator.ColorLink;

import penoplatinum.util.Point;

public class SwingGridView extends JFrame implements GridView {

  private Grid  grid;

  private String title = "Grid";

  private int left = -1,
              top = -1,
              width = 0,
              height = 0,
              size = 40;

  private GridBoard board;


  public GridView display(Grid grid) {
    this.grid = grid;
    this.setupBoard();
    this.setupWindow();
    this.refresh();
    return this;
  }

  public GridView displayWithoutWindow(Grid grid) {
    this.grid = grid;
    this.setupBoard();
    this.refresh();
    return this;
  }

  public GridView setSectorSize(int size) {
    this.size = size;
    if( this.board != null ) {
      this.board.setSectorSize(this.size);
    }
    return this;
  }

  private void setupBoard() {
    this.board = new GridBoard();
    this.add(this.board);
    this.board.setSectorSize(this.size);
  }

  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.refreshSize();
    if (this.left > 0) {
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

    this.board.clearSectors();
    this.addSectors();
    this.addWalls();

    this.board.clearValues();
    this.addValues();

    this.board.clearAgents();
    this.addAgents();

    this.board.render();
    return this;
  }

  private void addSectors() {
    int minLeft = this.grid.getMinLeft(),
        minTop  = this.grid.getMinTop();

    // add sectors
    for (Sector sector : this.grid.getSectors()) {
      this.board.addSector(this.grid.getPositionOf(sector).translate(-minLeft,-minTop));
    }

    this.board.addOrigin(-minLeft, -minTop);
  }

  private void addWalls() {
    int minLeft = this.grid.getMinLeft(),
        minTop = this.grid.getMinTop();

    // add sectors
    for (Sector sector : this.grid.getSectors()) {
      for (Bearing bearing : Bearing.NESW) {
        if( ! sector.knowsWall(bearing) ) {
          this.board.addUnknownWall(grid.getPositionOf(sector).translate(-minLeft, -minTop), bearing);
        } else if( sector.hasWall(bearing) ) {
          this.board.addWall(grid.getPositionOf(sector).translate(-minLeft, -minTop), bearing);
        }
      }
    }
  }

  private void addValues() {
    int minLeft = this.grid.getMinLeft(),
        minTop = this.grid.getMinTop();

    for (Sector sector : this.grid.getSectors()) {
      Point position = grid.getPositionOf(sector).translate(-minLeft, -minTop);
      this.board.addValue(position, sector.getValue());
    }
  }

  private void addAgents() {
    int minLeft = this.grid.getMinLeft(),
        minTop  = this.grid.getMinTop();

    for( Agent agent : this.grid.getAgents() ) {
      final java.awt.Color c = ColorLink.getColorByName(agent.getName());
      Point position = this.grid.getPositionOf(agent);
      position.translate(-minLeft, -minTop);
      if( ! (agent instanceof BarcodeAgent) ) {
        this.board.addAgent(position, grid.getBearingOf(agent), agent.getName(), c);
      } else {
        this.board.addBarcode(position, grid.getBearingOf(agent), agent.getValue());
      }
    }
  }

  private void refreshSize() {
    int newWidth = this.grid.getWidth(),
        newHeight = this.grid.getHeight();
    if (newWidth != this.width || newHeight != this.height) {
      final int width = Math.max(newWidth * this.board.getSectorSize() - 2, this.board.getSectorSize() * 5);
      // set our own size
      this.setSize(width,
              newHeight * this.board.getSectorSize() + 30);
      // set the board's size
      if (this.board != null) {
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
    this.top = top;
    return this;
  }

  public void disableWindow() {
    this.setVisible(false);
  }

  public GridBoard getBoard() {
    return this.board;
  }
}
