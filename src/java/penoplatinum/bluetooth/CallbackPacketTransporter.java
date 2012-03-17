/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

/**
 *
 * @author: Team Platinum
 */
public class CallbackPacketTransporter implements IPacketTransporter {

  private ByteArrayOutputStream byteArrayOutputStream;
  private DataOutputStream sendStream;
  private final IConnection connection;
  private final IPacketHandler handler;

  public CallbackPacketTransporter(IConnection connection, IPacketHandler handler) {
    byteArrayOutputStream = new ByteArrayOutputStream();
    sendStream = new DataOutputStream(byteArrayOutputStream);
    this.connection = connection;
    this.handler = handler;
  }

  public DataOutputStream getSendStream() {
    synchronized (this) {
      return sendStream;
    }
  }

  public void SendPacket(int packetIdentifier) {
    connection.SendPacket(this, packetIdentifier, byteArrayOutputStream.toByteArray());
    byteArrayOutputStream.reset();

  }

  @Override
  public void onPacketReceived(int packetIdentifier, byte[] dgram, int offset, int size) {
    handler.receive(packetIdentifier, Arrays.copyOfRange(dgram, offset, size));
  }


}
