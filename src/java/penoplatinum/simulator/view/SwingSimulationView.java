package penoplatinum.simulator.view;

/**
 * SwingSimulationView
 * 
 * Implementation of SimulationView using Swing to setup a visual 
 * representation of the SimulationEnvironment.
 * 
 * @author: Team Platinum
 */
import javax.swing.JFrame;
import penoplatinum.map.Map;

public class SwingSimulationView extends JFrame implements SimulationView {

  private Board board;

  public SwingSimulationView() {
    this.setupBoard();
    this.setupWindow();
  }

  private void setupBoard() {
    this.board = new Board();
    this.add(this.board);
  }

  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // by default we're 2 tiles by 2 (of 160px with 2px/cm
    this.setSize(2 * 160, 2 * 160 + 22);
    this.setLocationRelativeTo(null);
    setLocation(0, 0);
    this.setTitle("Simulator");
    this.setResizable(false);
    this.setVisible(true);
  }

  @Override
  public void showMap(Map map) {
    if (map == null) {
      return;
    }
    this.board.showMap(map);
    // a tile is 80cm in reality, we apply a scale of 2px/cm
    this.setSize(map.getWidth() * 160, map.getHeight() * 160 + 22);
  }

  @Override
  public void updateRobots() {
    this.board.updateRobots();

    // pass on control for a little while
    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
      System.err.println(e);
    }
  }

  @Override
  public void log(String msg) {
    System.out.println(msg);
  }

  @Override
  public void addRobot(ViewRobot r) {
    board.addRobot(r);
  }

  public Board getBoard() {
    return board;
  }
}
