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
import java.util.ArrayList;
import penoplatinum.simulator.Board;

public class SwingUIView extends JFrame implements UIView, ActionListener {

    // we create a container, based on a FlowLayout
    private Container content;
    // the dashboard is a JPanel-based custom widget
    private Dashboard dashboard;
    // we provide a console to display log-lines, received from the robot
    private JTextArea console;
    // the board on which the map is constructed
    private Board board;
    // the layout of our UI
    private GroupLayout layout;
    // a list of all the buttons
    private ArrayList<JButton> buttons = new ArrayList();
    // we need an implementation of UICommandHandler to pass back UI-induced
    // commands to the robot
    private UICommandHandler commandHandler;

    public SwingUIView() {
        this.setupContentPane();
        this.setupDashboard();
        this.setupMapView();
        this.setupControlButtons();
        this.setupConsole();
        this.setupWindow();
        this.setupLayout();
    }

    private void setupContentPane() {
        this.content = getContentPane();
        this.layout = new GroupLayout(content);
        this.content.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
    }

    private void setupDashboard() {
        this.dashboard = new Dashboard();
        this.content.add(this.dashboard);
    }

    private void setupControlButtons() {
        this.addButton("Connecteer", "connect");
        this.addButton("Calibreer", "calibrate");
        this.addButton("Ok", "ok");
        this.addButton("Start programma", "run");
    }

    private void addButton(String label, String command) {
        JButton button = new JButton(label);
        button.setActionCommand(command);
        button.addActionListener(this);
        this.content.add(button);
        this.buttons.add(button);
    }

    private void setupConsole() {
        this.console = new JTextArea(8, 57);
        this.console.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(this.console);
        this.content.add(scrollPane);
    }

    private void setupMapView() {
        this.board = new Board();
        JScrollPane scrollPane = new JScrollPane(this.board);
        this.content.add(scrollPane);
    }
       
    private void setupWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1360, 650);
        this.setLocationRelativeTo(null);
        this.setTitle("aNGie Console");
        this.setResizable(false);
        this.setVisible(true);
    }
    
    private void setupLayout() {
        this.layout.setHorizontalGroup(
                this.layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(dashboard)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(buttons.get(0))   // afschuwelijke uitbreidbaarheid maar ik zie niet direct een alternatief
                            .addComponent(buttons.get(1))
                            .addComponent(buttons.get(2))
                            .addComponent(buttons.get(3))
                        )
                        .addComponent(this.console)
                     )
                    .addGap(10)
                    .addComponent(board)
        );
        this.layout.setVerticalGroup(
                this.layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dashboard)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(buttons.get(0))
                            .addComponent(buttons.get(1))
                            .addComponent(buttons.get(2))
                            .addComponent(buttons.get(3))
                        )
                        .addComponent(this.console)
                    )
                    .addComponent(board)
        );
        layout.linkSize(SwingConstants.HORIZONTAL, dashboard, console);
    }

    public void updateLight(final int lightValue, final int lightColor) {
        final JTextArea c = this.console;
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                SwingUIView.this.dashboard.updateLight(lightValue, lightColor);
            }
        });
    }

    public void updateBarcode(final int barcode, final int direction) {
        final JTextArea c = this.console;
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                SwingUIView.this.dashboard.updateBarcode(barcode, direction);
            }
        });
    }

    public void updateSonar(final int angle, final int value) {
        final JTextArea c = this.console;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingUIView.this.dashboard.updateSonar(angle, value);
            }
        });
    }
    
    public void updateTouch(final int position, final boolean pressed) {
        final JTextArea c = this.console;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingUIView.this.dashboard.updateTouch(position, pressed);
            }
        });
    }
    

    /**
     * This function is thread safe!
     * 
     * @param line 
     */
    public void addConsoleLog(final String line) {
        final JTextArea c = this.console;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                c.append(line + "\n");
                c.setCaretPosition(c.getDocument().getLength());
            }
        });

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
