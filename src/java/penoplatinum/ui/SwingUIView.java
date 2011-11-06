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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingUIView extends JFrame implements UIView, ActionListener {
  
  private Container content;
  private Dashboard dashboard;
  
  private JTextArea console;
  
  private UICommandHandler commandHandler;

  public SwingUIView() {
    this.setupContentPane();
    this.setupDashboard();
    this.setupControlButtons();
    this.setupConsole();
    this.setupWindow();
  }
  
  private void setupContentPane() {
    this.content = getContentPane();
    this.content.setLayout(new FlowLayout());
  }
  
  private void setupDashboard() {
    this.dashboard = new Dashboard();
    this.content.add(this.dashboard);
  }
  
  private void setupControlButtons() {
    this.addButton("Connecteer",    "connect"  );
    this.addButton("Calibreer",     "calibrate");
    this.addButton("Volg lijn",     "line"     );
    this.addButton("Volg muur",     "wall"     );
    this.addButton("Volg barcodes", "barcode"  );
  }
  
  private void addButton( String label, String command ) {
    JButton button = new JButton(label);
    button.setActionCommand(command);
    button.addActionListener(this);
    this.content.add(button);
  }
  
  
  private void setupConsole() {
    this.console = new JTextArea(8, 52);
    this.console.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(this.console);
    this.content.add(this.console);
  }
  
  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(640, 640);
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
  
  public void addConsoleLog( String line ) {
    this.console.append(line + "\n");
    this.console.setCaretPosition(this.console.getDocument().getLength());
  }
  
  public void clearConsole() {
    this.console.setText("");
  }

  public UIView setCommandHandler(UICommandHandler handler) {
    this.commandHandler = handler;
    return this;
  }

  public void actionPerformed(ActionEvent e) {
    this.commandHandler.handle(e.getActionCommand());
  } 
}
