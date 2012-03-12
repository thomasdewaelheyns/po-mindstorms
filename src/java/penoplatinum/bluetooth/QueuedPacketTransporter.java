/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    p.Dgram = copyOfRange(dgram, offset, size);

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

  /**
   * Copies the specified range of the specified array into a new array.
   * The initial index of the range (<tt>from</tt>) must lie between zero
   * and <tt>original.length</tt>, inclusive.  The value at
   * <tt>original[from]</tt> is placed into the initial element of the copy
   * (unless <tt>from == original.length</tt> or <tt>from == to</tt>).
   * Values from subsequent elements in the original array are placed into
   * subsequent elements in the copy.  The final index of the range
   * (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>,
   * may be greater than <tt>original.length</tt>, in which case
   * <tt>(byte)0</tt> is placed in all elements of the copy whose index is
   * greater than or equal to <tt>original.length - from</tt>.  The length
   * of the returned array will be <tt>to - from</tt>.
   *
   * @param original the array from which a range is to be copied
   * @param from the initial index of the range to be copied, inclusive
   * @param to the final index of the range to be copied, exclusive.
   *     (This index may lie outside the array.)
   * @return a new array containing the specified range from the original array,
   *     truncated or padded with zeros to obtain the required length
   * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt>
   *     or <tt>from &gt; original.length()</tt>
   * @throws IllegalArgumentException if <tt>from &gt; to</tt>
   * @throws NullPointerException if <tt>original</tt> is null
   * @since 1.6
   */
  public static byte[] copyOfRange(byte[] original, int from, int to) {
    int newLength = to - from;
    if (newLength < 0) {
      throw new IllegalArgumentException(from + " > " + to);
    }
    byte[] copy = new byte[newLength];
    System.arraycopy(original, from, copy, 0,
            Math.min(original.length - from, newLength));
    return copy;
  }
}
