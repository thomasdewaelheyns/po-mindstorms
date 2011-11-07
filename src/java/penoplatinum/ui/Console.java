package penoplatinum.ui;

/**
 * Console
 * 
 * Console for our robot
 * 
 * Author: Team Platinum
 */
import java.io.File;
import java.io.PrintStream;
import penoplatinum.bluetooth.*;
import java.util.Scanner;
import penoplatinum.Utils;

public class Console implements UICommandHandler {

    UIView ui;
    PacketTransporter endpoint;
    PrintStream endpointWrite;
    int msgType;
    String msg;
    PCBluetoothConnection connection;

    public Console() {
        this.setupUI();
    }

    private void setupUI() {
        this.ui = new SwingUIView();
        this.ui.setCommandHandler(this);
    }

    private void setupBluetooth() {
        connection = new PCBluetoothConnection();
        this.endpoint = new PacketTransporter(connection);
        connection.RegisterTransporter(this.endpoint, UIView.LIGHT);
        connection.RegisterTransporter(this.endpoint, UIView.SONAR);
        connection.RegisterTransporter(this.endpoint, UIView.BARCODE);
        connection.RegisterTransporter(this.endpoint, UIView.LOG);
        connection.RegisterTransporter(this.endpoint, UIView.CLEARLOG);

        endpointWrite = new PrintStream(endpoint.getSendStream());


        final UIView ui = this.ui;
        
        RemoteFileLogger logger = new RemoteFileLogger(connection, "RobotLog", new File("../RobotLogs"));
        logger.startLogging();
        logger.setOutputStream(new IRemoteLoggerCallback() {

            @Override
            public void onLog(String message) {
                ui.addConsoleLog(message);
            }
        });

    }

    private void startEventLoop() {
        String[] values;
        while (true) {
            this.receive();
            switch (this.msgType) {
                case UIView.LIGHT:
                    values = this.msg.split(",");
                    this.ui.updateLight(Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]));
                    break;
                case UIView.SONAR:
                    values = this.msg.split(",");
                    this.ui.updateSonar(Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]));
                    break;
                case UIView.BARCODE:
                    values = this.msg.split(",");
                    this.ui.updateBarcode(Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]));
                    break;
                case UIView.LOG:
                    this.ui.addConsoleLog(this.msg);
                    break;
                case UIView.CLEARLOG:
                    this.ui.clearConsole();
                    break;
                default:
                    this.ui.addConsoleLog("Unknown msgType received: " + msgType);
            }
        }
    }

    private void receive() {
        this.msgType = this.endpoint.ReceivePacket();
        Scanner s = new Scanner(this.endpoint.getReceiveStream());
        this.msg = s.nextLine();
        this.ui.addConsoleLog("received (" + this.msgType + ") : " + this.msg);
    }

    // handle commands originating in the UI
    public void handle(String command) {
        if (command.equals("connect")) {
            if (connection == null) {
                setupBluetooth();
            }
            //TODO: correctly disconnect
            connection.initializeConnection();
            return;
        }
        endpointWrite.println(command);
        endpoint.SendPacket(UIView.COMMAND);
        Utils.Log("Send command to Robot: " + command);
    }
}
