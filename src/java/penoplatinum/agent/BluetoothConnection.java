package penoplatinum.agent;

/**
 * BluetoothConnection
 * 
 * Functional wrapper around technical Bluetooth layer
 *
 * Author: Team Platinum
 */

import java.io.*;
import java.util.Scanner;

import penoplatinum.bluetooth.*;

public class BluetoothConnection {
  private PCBluetoothConnection connection;
  private QueuedPacketTransporter     endPoint;

  private String nextMsg = "";

  public BluetoothConnection() {
    this.connection = new PCBluetoothConnection();
    this.connection.initializeConnection();
    this.endPoint   = new QueuedPacketTransporter(this.connection);
    this.connection.RegisterTransporter(this.endPoint, 123);
    this.connection.RegisterTransporter(this.endPoint, penoplatinum.Utils.PACKETID_LOG);
  }

  public Boolean hasNext() {
    int packet;
    String data;
    Boolean logging = true;
    while(logging) {
      packet = this.endPoint.ReceivePacket();
      data   = new Scanner(this.endPoint.getReceiveStream()).nextLine();      
      switch(packet) {
        case penoplatinum.Utils.PACKETID_LOG:
          System.out.println( "Log:>" + data );
          break;
        case 123:
          this.nextMsg = data;
          return true;
      }
    }
    return false;
  }
  
  public String getMessage() {
    return this.nextMsg;
  }
}
