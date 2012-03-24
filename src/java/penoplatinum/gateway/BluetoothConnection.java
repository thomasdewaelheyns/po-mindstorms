package penoplatinum.gateway;

/**
 * BluetoothConnection
 * 
 * Functional wrapper around technical Bluetooth layer
 *
 * Author: Team Platinum
 */
import java.io.*;
import java.util.Scanner;

import org.apache.log4j.Logger;

import penoplatinum.bluetooth.*;
import penoplatinum.util.Utils;

import penoplatinum.bluetooth.IPacketTransporter;

import penoplatinum.Config;


public class BluetoothConnection implements Connection {

  private static Logger logger = Logger.getLogger("BluetoothConnection");

  private IConnection connection;
  private QueuedPacketTransporter endPoint;
  private String nextMsg = "";
  private int nextType = 0;

  public BluetoothConnection() {
    PCBluetoothConnection connection = new PCBluetoothConnection();
    connection.initializeConnection();
    this.connection = connection;
    initTransporter();
  }
  
  public BluetoothConnection setName(String name) {
    // TODO: take name of robot in account when connecting
    return this;
  }
  
  public void initTransporter() {
    this.endPoint = new QueuedPacketTransporter(this.connection);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_MODEL);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_WALLS);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_VALUES);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_AGENTS);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_LOG);
    this.connection.RegisterTransporter(this.endPoint, Config.BT_GHOST_PROTOCOL);
  }

  public BluetoothConnection send(String msg, int channel) {
    try {
      this.endPoint.getSendStream().write(msg.getBytes());
      this.endPoint.SendPacket(channel);
    } catch (IOException ex) {
      logger.error( "Could not send message to channel : " + channel );
    }
    return this;
  }

  public boolean hasNext() {
    int packet;
    String data;
    Boolean logging = true;

    while (logging) {
      packet = this.endPoint.ReceivePacket();
      data   = new Scanner(this.endPoint.getReceiveStream()).nextLine();
      if( data.length() > 10 ) {
        this.nextType = packet;
        this.nextMsg = data;
        return true;
      }
    }
    this.nextType = 0;
    return false;
  }

  public int getType() {
    return this.nextType;
  }

  public String getMessage() {
    return this.nextMsg;
  }

  public IConnection getConnection() {
    return connection;
  }
}
