/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author MHGameWork
 */
public class PacketTransporter implements IPacketTransporter {

    private ByteArrayOutputStream byteArrayOutputStream;
    private DataInputStream receiveStream;
    private DataOutputStream sendStream;
    private final IConnection connection;
    
    public PacketTransporter(IConnection connection) {
        receiveStream = new DataInputStream(new ByteArrayInputStream(new byte[1024]));
        byteArrayOutputStream = new ByteArrayOutputStream();
        sendStream = new DataOutputStream(byteArrayOutputStream);
        this.connection = connection;
    }
    
    @Override
    public DataInputStream getReceiveStream() {
        return receiveStream;
    }
    
    @Override
    public DataOutputStream getSendStream() {
        return sendStream;
    }
    
    @Override
    public int ReceivePacket() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int ReceiveAvailablePacket() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void SendPacket(int packetIdentifier) {
        connection.SendPacket(this, packetIdentifier, byteArrayOutputStream.toByteArray());
        sendStream = new DataOutputStream(byteArrayOutputStream); // TODO: GC 
    }
}
