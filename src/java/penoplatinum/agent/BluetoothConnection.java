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
    this.connection.initializeConnection();
    this.endPoint   = new PacketTransporter(this.connection);
    this.connection.RegisterTransporter(this.endPoint, 123);
  }

  public Boolean hasNext() {
    this.endPoint.ReceivePacket();
    this.nextMsg = new Scanner(this.endPoint.getReceiveStream()).nextLine();
    return true;
  }
  
  public String getMessage() {
    return this.nextMsg;
  }
}
