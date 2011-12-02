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
  private PacketTransporter     endPoint;

  private String nextMsg = "";

  public BluetoothConnection() {
    this.connection = new PCBluetoothConnection();
    this.endPoint   = new PacketTransporter(this.connection);
    this.connection.RegisterTransporter(this.endPoint, 123);
  }

  public Boolean hasNext() {
    if( this.endPoint.ReceiveAvailablePacket() == -1 ) { return false; }
    this.nextMsg = new Scanner(this.endPoint.getReceiveStream()).nextLine();
    return true;
  }
  
  public String getMessage() {
    return this.nextMsg;
  }
}
