/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import penoplatinum.util.CircularQueue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import penoplatinum.Utils;

/**
 *
 * @author: Team Platinum
 */
public class QueuedPacketTransporter implements IPacketTransporter {

  private final CircularQueue<Packet> receivedQueue = new CircularQueue<Packet>(100);
  private ByteArrayOutputStream byteArrayOutputStream;
  private DataInputStream receiveStream;
  private DataOutputStream sendStream;
  private final IConnection connection;

  public QueuedPacketTransporter(IConnection connection) {
    receiveStream = new DataInputStream(new ByteArrayInputStream(new byte[1024]));
    byteArrayOutputStream = new ByteArrayOutputStream();
    sendStream = new DataOutputStream(byteArrayOutputStream);
    this.connection = connection;
  }

  public DataInputStream getReceiveStream() {
    synchronized (this) {
      return receiveStream;

    }
  }

  public DataOutputStream getSendStream() {
    synchronized (this) {
      return sendStream;
    }
  }

  /**
   * Blocks until a packet is available. The packet identifier is returned, 
   * and the packet is available for reading on the Receive stream.
   */
  public int ReceivePacket() {
    synchronized (receivedQueue) {
      while (receivedQueue.isEmpty()) {
        try {
          receivedQueue.wait();
        } catch (InterruptedException ex) {
          Utils.Log("ReceivePacketException");
        }
      }

      Packet p = receivedQueue.remove();
      updateReceiveStream(p);

      return p.PacketIdentifier;


    }
  }

  private void updateReceiveStream(Packet p) {
    try {
      receiveStream.close();
    } catch (IOException ex) {
      Utils.Log("UpdateReceiveStream: " + ex.getClass());
    }

    ByteArrayInputStream strm = new ByteArrayInputStream(p.Dgram);
    receiveStream = new DataInputStream(strm);
  }

  /**
   * Returns the oldest packet in the received packet queue.
   * If no packet is available this function immediately returns -1;
   * The packet identifier is returned, 
   * and the packet is available for reading on the Receive stream.
   */
  public int ReceiveAvailablePacket() {
    synchronized (receivedQueue) {
      if (receivedQueue.isEmpty()) {
        return -1;
      }

      Packet p = receivedQueue.remove();

      updateReceiveStream(p);

      return p.PacketIdentifier;
    }

  }

  /**
   * Sends a packet with given identifier. (non-blocking)
   * The data written to the SendStream since the last call to SendPacket is sent.
   */
  public void SendPacket(int packetIdentifier) {
    connection.SendPacket(this, packetIdentifier, byteArrayOutputStream.toByteArray());
    byteArrayOutputStream.reset();

  }

  @Override
  public void onPacketReceived(int packetIdentifier, byte[] dgram, int offset, int size) {
    Packet p = new Packet();
    p.PacketIdentifier = packetIdentifier;
    p.Dgram = Arrays.copyOfRange(dgram, offset, size);

    synchronized (receivedQueue) {
      if (receivedQueue.isFull())
      {
        Utils.Log("Discarding packet!");
        return;
      }
      receivedQueue.insert(p);
      receivedQueue.notify();
    }

  }
}
