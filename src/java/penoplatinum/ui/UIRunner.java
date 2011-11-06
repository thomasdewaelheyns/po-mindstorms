package penoplatinum.ui;

/**
 * UIRunner
 * 
 * Runner for the UI
 * 
 * Author: Team Platinum
 */

import penoplatinum.bluetooth.*;
import penoplatinum.ui.*;
import java.util.Scanner;

class UIRunner implements UICommandHandler {
  UIView ui;
  PacketTransporter endpoint;
  
  int msgType;
  String msg;

  public UIRunner() {
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
          System.err.println( "Unknown msgType received: " + msgType );
      }
    }
  }

  private void receive() {
    this.msgType = this.endpoint.ReceivePacket();
    Scanner s = new Scanner(this.endpoint.getReceiveStream());
    this.msg = s.nextLine();
    System.err.println( "received (" + this.msgType + ") : " + this.msg );
  }
  
  public void handle(String command) {
    if( "connect".equals(command) ) {
      System.err.println( "TODO: execute \"connect\" on robot" );
    } else if( "calibrate".equals(command) ) {
      System.err.println( "TODO: execute \"calbrate\" on robot" );
    } else if( "line".equals(command) ) {
      System.err.println( "TODO: execute \"line\" on robot" );
    } else if( "wall".equals(command) ) {
      System.err.println( "TODO: execute \"wall\" on robot" );
    } else if( "barcode".equals(command) ) {
      System.err.println( "TODO: execute \"barcode\" on robot" );
    } else {
      System.err.println( "Unknown command from GUI: " + command );
    }
  }
  
  public static void main(String[] args) {
    new UIRunner();
  }
}
