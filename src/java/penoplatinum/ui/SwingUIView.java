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
  
  // we create a container, based on a FlowLayout
  private Container content;

  // the dashboard is a JPanel-based custom widget
  private Dashboard dashboard;

  // we provide a console to display log-lines, received from the robot
  private JTextArea console;
  
  // we need an implementation of UICommandHandler to pass back UI-induced
  // commands to the robot
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
    this.addButton("Ok",            "ok"       );
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
    this.console = new JTextArea(8, 57);
    this.console.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(this.console);
    this.content.add(scrollPane);
  }
  
  private void setupWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(700, 640);
    this.setLocationRelativeTo(null);
    this.setTitle("aNGie Console");
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

  // mapper from the (technical) ActionListener to a more functional interface
  public void actionPerformed(ActionEvent e) {
    this.commandHandler.handle(e.getActionCommand());
  } 
}
