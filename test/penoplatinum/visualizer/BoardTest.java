/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.visualizer;

import org.junit.Test;
import penoplatinum.map.Map;
import penoplatinum.simulator.tiles.Sectors;
import penoplatinum.simulator.view.SwingSimulationView;

/**
 *
 * @author MHGameWork
 */
public class BoardTest {

  @Test
  public void testRenderPartialMap() {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        
        Map m = new Map(10);
        
        m.put(Sectors.ESW, 1, 1);
        m.put(Sectors.EW, 2, 3);
        
        SwingSimulationView view = new SwingSimulationView();
        
        view.showMap(m);
        
        //view.pack();
        view.setSize(view.getBoard().getSize());
        view.setVisible(true);
      }
    });

    
  }

  public static void main(String[] args) {
    new BoardTest().testRenderPartialMap();
    
    
  }
}
