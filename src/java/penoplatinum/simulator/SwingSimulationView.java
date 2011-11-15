package penoplatinum.simulator;

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
    // by default we're 2 tiles by 2 (of 160px with 2px/cm
    this.setSize( 2 * 160, 2 * 160 + 22 );
    this.setLocationRelativeTo(null);
    this.setTitle("Simulator");
    this.setResizable(false);
    this.setVisible(true);
  }
  
  public void showMap(Map map) {
    this.board.showMap(map);
    // a tile is 80cm in reality, we apply a scale of 2px/cm
    this.setSize(map.getWidth() * 160, map.getHeight() * 160 + 22 );
  }
  
  public void updateRobot( int x, int y, int direction ) {
    // apply the scale of 2px/cm
    this.board.updateRobot( x * 2, y * 2, direction );
    // This Thread.sleep() causes "hickups" 90% of the calls run ok, but in 
    // some cases it "hangs" for about a second ?!
    // try {
    //   Thread.sleep(10);
    // } catch( InterruptedException e ) {
    //   System.err.println( e );
    // }
  }
}
