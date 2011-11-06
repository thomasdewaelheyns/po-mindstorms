package penoplatinum.ui;

/**
 * Console
 * 
 * Console for our robot
 * 
 * Author: Team Platinum
 */

import penoplatinum.bluetooth.*;
import penoplatinum.ui.*;
import java.util.Scanner;

public class Console implements UICommandHandler {
  UIView ui;
  PacketTransporter endpoint;
  
  int msgType;
  String msg;

  public Console() {
    this.setupUI();
  }
  
  private void setupUI() {
    this.ui = new SwingUIView();
    this.ui.setCommandHandler(this);
  }

  private void setupBluetooth() {
    PCBluetoothConnection connection = new PCBluetoothConnection();
    connection.initializeConnection();
    this.endpoint = new PacketTransporter(connection);
    connection.RegisterTransporter(this.endpoint, UIView.LIGHT);
    connection.RegisterTransporter(this.endpoint, UIView.SONAR);
    connection.RegisterTransporter(this.endpoint, UIView.BARCODE);
  }

  private void startEventLoop() {
    String[] values;
    while( true ) {
      this.receive();
      switch(this.msgType) {
        case UIView.LIGHT:
          values = this.msg.split(",");
          this.ui.updateLight( Integer.parseInt(values[0]), 
                               Integer.parseInt(values[1]) );
          break;
        case UIView.SONAR:
          values = this.msg.split(",");
          this.ui.updateSonar( Integer.parseInt(values[0]), 
                               Integer.parseInt(values[1]) );
          break;
        case UIView.BARCODE:
          values = this.msg.split(",");
          this.ui.updateBarcode( Integer.parseInt(values[0]), 
                                 Integer.parseInt(values[1]) );
          break;
        case UIView.LOG:
          this.ui.addConsoleLog(this.msg);
          break;
        case UIView.CLEARLOG:
          this.ui.clearConsole();
          break;
        default:
          this.ui.addConsoleLog( "Unknown msgType received: " + msgType );
      }
    }
  }

  private void receive() {
    this.msgType = this.endpoint.ReceivePacket();
    Scanner s = new Scanner(this.endpoint.getReceiveStream());
    this.msg = s.nextLine();
    this.ui.addConsoleLog( "received (" + this.msgType + ") : " + this.msg );
  }
  
  // handle commands originating in the UI
  public void handle(String command) {
    if( "connect".equals(command) ) {
      this.ui.addConsoleLog( "TODO: execute \"connect\" on robot" );
    } else if( "calibrate".equals(command) ) {
      this.ui.addConsoleLog( "TODO: execute \"calbrate\" on robot" );
    } else if( "line".equals(command) ) {
      this.ui.addConsoleLog( "TODO: execute \"line\" on robot" );
    } else if( "ok".equals(command) ) {
      this.ui.addConsoleLog( "TODO: execute \"ok\" on robot" );
    } else if( "wall".equals(command) ) {
      this.ui.addConsoleLog( "TODO: execute \"wall\" on robot" );
    } else if( "barcode".equals(command) ) {
      this.ui.addConsoleLog( "TODO: execute \"barcode\" on robot" );
    } else {
      this.ui.addConsoleLog( "Unknown command from GUI: " + command );
    }
  }
}
