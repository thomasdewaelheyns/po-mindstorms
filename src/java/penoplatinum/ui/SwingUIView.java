package penoplatinum.ui;

/**
 * SwingUIView
 * 
 * Implementation of UIView using Swing to setup a visual representation of 
 * the information provided by the robot.
 * This view implements an Swing-based window, on which a dashboard is
 * projected.
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
  
  public void updateLight( int lightValue, int lightColor ) {
    this.dashboard.updateLight( lightValue, lightColor );
  }

  public void updateBarcode( int barcode, int direction ) {
    this.dashboard.updateBarcode( barcode, direction );
  }
  
  public void updateSonar( int angle, int value ) {
    this.dashboard.updateSonar( angle, value );
  }
}
