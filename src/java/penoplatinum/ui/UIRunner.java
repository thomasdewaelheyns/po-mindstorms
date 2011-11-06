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

class UIRunner {
  public static void main(String[] args) {
    UIView ui = new SwingUIView();
    
    //SimulatedConnection connection = new SimulatedConnection();// use a simulated connection
    PCBluetoothConnection connection = new PCBluetoothConnection();
    connection.initializeConnection();
    PacketTransporter endpoint = new PacketTransporter(connection);
    connection.RegisterTransporter(endpoint, UIView.LIGHT);
    connection.RegisterTransporter(endpoint, UIView.SONAR);
    connection.RegisterTransporter(endpoint, UIView.BARCODE);
    
    while( true ) {
      int msgType = endpoint.ReceivePacket();
      Scanner s = new Scanner(endpoint.getReceiveStream());
      // the msg consists of 2 integer values separated by a semi-colon
      String msg = "";
      msg = s.nextLine();
        
      System.err.println( "received (" + msgType + ") : " + msg );
      String[] values;
      switch(msgType) {
        case UIView.LIGHT:
          values = msg.split(",");
          ui.updateLight( Integer.parseInt(values[0]), 
                          Integer.parseInt(values[1]) );
          break;
        case UIView.SONAR:
          values = msg.split(",");
          ui.updateSonar( Integer.parseInt(values[0]), 
                          Integer.parseInt(values[1]) );
          break;
        case UIView.BARCODE:
          values = msg.split(",");
          ui.updateBarcode( Integer.parseInt(values[0]), 
                            Integer.parseInt(values[1]) );
          break;
        default:
          System.err.println( "Unknown msgType received: " + msgType );
      }
    }
  }
}
