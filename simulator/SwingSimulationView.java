/**
 * SwingSimulationView
 * 
 * Implementation of SimulationView using Swing to setup a visual 
 * representation of the SimulationEnvironment.
 * 
 * Author: Team Platinum
 */

import javax.swing.JFrame;

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
    this.setSize(320, 320);
    this.setLocationRelativeTo(null);
    this.setTitle("Simulator");
    this.setResizable(false);
    this.setVisible(true);
  }
  
  public void updateRobot( int x, int y, int direction ) {
    this.board.updateRobot( x, y, direction );
    try { 
      Thread.sleep(30);
    } catch( InterruptedException e ) {
      System.err.println( e );
    }
  }
}
