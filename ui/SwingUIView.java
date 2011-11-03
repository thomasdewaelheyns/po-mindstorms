/**
 * SwingUIView
 * 
 * Implementation of UIView using Swing to setup a visual 
 * representation of the UIEnvironment.
 * 
 * Author: Team Platinum
 */

import javax.swing.JFrame;

public class SwingUIView extends JFrame implements UIView {
  
  private Dashboard dashboard;
  
  public SwingUIView() {
    this.setupDashboard();
    this.setupWindow();
  }
  
  private void setupDashboard() {
    this.dashboard = new Dashboard();
    this.add(this.dashboard);
  }
  
  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize( 640, 480 );
    this.setLocationRelativeTo(null);
    this.setTitle("UI");
    this.setResizable(false);
    this.setVisible(true);
  }
  
  public void update( int lightValue, int lightColor, 
                      int barcode, int direction )
  {
    this.dashboard.update( lightValue, lightColor, barcode, direction );
  }

  public void update( int lightValue, int lightColor ) {
    this.dashboard.update( lightValue, lightColor, UIView.NONE, UIView.NONE );
  }
}
