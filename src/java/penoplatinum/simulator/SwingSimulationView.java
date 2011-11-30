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
import java.util.List;

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
    setLocation(0,0);
    this.setTitle("Simulator");
    this.setResizable(false);
    this.setVisible(true);
  }
  
  public void showMap(Map map) {
    if( map == null ) { return; }
    this.board.showMap(map);
    // a tile is 80cm in reality, we apply a scale of 2px/cm
    this.setSize(map.getWidth() * 160, map.getHeight() * 160 + 22 );
  }
  
  public void updateRobot( int x, int y, int direction,
                           List<Integer> values, List<Integer> angles )
  {
    this.board.updateRobot( x, y, direction, values, angles );
    
    // pass on control for a little while
    try {
      Thread.sleep(1);
    } catch( InterruptedException e ) {
      System.err.println( e );
    }
  }
  
  public void log( String msg ) {
    System.out.println( msg );
  }
}
