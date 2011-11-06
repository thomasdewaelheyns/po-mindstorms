/**
 * demo2
 * 
 * Author: Team Platinum
 */

import penoplatinum.bluetooth.*;
import penoplatinum.Utils;
import java.io.IOException;

class demo2 {
  public static void main(String[] args) {
    UIView ui = new SwingUIView();

    // use a simulated connection
    SimulatedConnection connection = new SimulatedConnection();
    PacketTransporter endpoint = new PacketTransporter(connection);
    
    while( true ) {
      int msgType = endpoint.ReceivePacket();
      // the msg consists of 2 integer values separated by a semi-colon
      String msg = "";
      try {
        msg = endpoint.getReceiveStream().readUTF();
      } catch( IOException e ) {
        System.err.println( e );
      } 
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

    // // simulate event loop
    // while(true) {
    //   ui.updateLight(600, UIView.BROWN);
    //   ui.updateBarcode(UIView.NONE, UIView.NONE);
    //   ui.updateSonar( 0, 8000);
    //   try { Thread.sleep(750); } catch(Exception e) { System.err.println(e); }
    // 
    //   ui.updateLight(800, UIView.WHITE);
    //   ui.updateBarcode(8, UIView.GO_LEFT);
    //   ui.updateSonar(20, 800);
    //   try { Thread.sleep(750); } catch(Exception e) { System.err.println(e); }
    // 
    //   ui.updateLight(500, UIView.BROWN );
    //   ui.updateBarcode(4, UIView.GO_FORWARD);
    //   ui.updateSonar(45, 300);
    //   try { Thread.sleep(750); } catch(Exception e) { System.err.println(e); }
    // 
    //   ui.updateLight(200, UIView.BLACK);
    //   ui.updateBarcode(2, UIView.GO_RIGHT);
    //   ui.updateSonar(90, 200 );
    //   try { Thread.sleep(750); } catch(Exception e) { System.err.println(e); }
    // }
  }
}
