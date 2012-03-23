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
    // TODO
    return this;
  }
  
  public void initTransporter() {
    this.endPoint = new QueuedPacketTransporter(this.connection);
    // TODO: put all these endpoints in a SINGLE configuration location
    this.connection.RegisterTransporter(this.endPoint, 123);
    this.connection.RegisterTransporter(this.endPoint, 124);
    this.connection.RegisterTransporter(this.endPoint, 125);
    this.connection.RegisterTransporter(this.endPoint, 126);
    this.connection.RegisterTransporter(this.endPoint, Utils.PACKETID_LOG);
    this.connection.RegisterTransporter(this.endPoint, GatewayConfig.MQRelayPacket);
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
