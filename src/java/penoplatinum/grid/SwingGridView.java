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

  private Grid grid;
  private String title = "Grid";
  private int left = -1,
          top = -1,
          width = 0,
          height = 0,
          size = 40;
  private boolean refreshSectors = true;
  private boolean refreshValues = true;
  private boolean refreshAgents = true;
  private boolean refreshBarcodes = true;
  private GridBoard board;

  public GridView display(Grid grid) {
    this.grid = grid;

    this.setupBoard();  // yes keep this order ;-)
    this.setupWindow(); // the board needs to be ready before we construct
    // the window
    this.refresh();

    return this;
  }

  public GridView display(Grid grid, boolean noWindow) {
    if (!noWindow) {
      display(grid);
      return this;
    }
    this.grid = grid;

    this.setupBoard();  // yes keep this order ;-)
    // the board needs to be ready before we construct
    // the window
    this.refresh();

    return this;
  }

  public GridView setSectorSize(int size) {
    this.size = size;
    if (this.board != null) {
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
    boolean somethingChanged = false;

    if (this.refreshSectors) {
      this.board.clearSectors();
      this.addSectors();
      this.addWalls();
      this.refreshSectors = false;
      somethingChanged = true;
    }

    if (this.refreshValues || this.refreshAgents) {
      this.board.clearValues();
      this.addValues();
      this.refreshValues = false;
      somethingChanged = true;
    }

    if (this.refreshAgents || this.refreshBarcodes) {
      this.board.clearAgents();
      this.addAgents();
      this.refreshAgents = false;
      somethingChanged = true;
    }



    if (somethingChanged) {
      this.board.render();
    }
    return this;
  }

  private void addSectors() {
    int minLeft = this.grid.getMinLeft(),
            minTop = this.grid.getMinTop();

    // add sectors
    for (Sector sector : this.grid.getSectors()) {
      this.board.addSector(grid.getPositionOf(sector).getX() - minLeft, grid.getPositionOf(sector).getY() - minTop);
    }

    this.board.addOrigin(-minLeft, -minTop);
  }

  private void addWalls() {
    int minLeft = this.grid.getMinLeft(),
            minTop = this.grid.getMinTop();

    // add sectors
    for (Sector sector : this.grid.getSectors()) {
      for (Bearing b : Bearing.NESW) {
        if (!sector.knowsWall(b)){
          this.board.addWall(grid.getPositionOf(sector).getX() - minLeft, grid.getPositionOf(sector).getY() - minTop,
                  b, true);continue;
        }
        if (!sector.hasWall(b))
          continue;
        this.board.addWall(grid.getPositionOf(sector).getX() - minLeft, grid.getPositionOf(sector).getY() - minTop,
                b, false);
      }
    }
  }

  private void addValues() {
    int minLeft = this.grid.getMinLeft(),
            minTop = this.grid.getMinTop();

    for (Sector sector : this.grid.getSectors()) {
      this.board.addValue(grid.getPositionOf(sector).getX() - minLeft, grid.getPositionOf(sector).getY() - minTop,
              sector.getValue());
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
        this.board.addAgent(position.getX(), position.getY(),
                            grid.getBearingOf(agent), agent.getName(), c);
      } else {
        this.board.addBarcode(position.getX(), position.getY(),
                              grid.getBearingOf(agent), agent.getValue());
      }
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

  public GridView barcodesNeedsRefresh() {
    this.refreshBarcodes = true;
    return this;
  }

  private void refreshSize() {
    int newWidth = this.grid.getWidth(),
            newHeight = this.grid.getHeight();
    if (newWidth != this.width || newHeight != this.height) {
      // Force everything to redraw
      sectorsNeedRefresh();
      valuesNeedRefresh();
      agentsNeedRefresh();
      barcodesNeedsRefresh();

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
