/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Queue;

/**
 *
 * @author MHGameWork
 */
public class PacketTransporter implements IPacketTransporter {

    
    private final Queue<Packet> receivedQueue;
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
        
    }
    
    @Override
    public int ReceiveAvailablePacket() {
        synchronized(receivedQueue)
        {
            if (queue.)
        }
                
    }
    
    @Override
    public void SendPacket(int packetIdentifier) {
        connection.SendPacket(this, packetIdentifier, byteArrayOutputStream.toByteArray());
        sendStream = new DataOutputStream(byteArrayOutputStream); // TODO: GC
        
    }

    @Override
    public void onPacketReceived(int packetIdentifier, byte[] dgram) {
        Packet p = new Packet();
        p.PacketIdentifier = packetIdentifier;
        p.Dgram = dgram;
        
        synchronized(receivedQueue)
        {
            receivedQueue.add(p);
        }
        
    }

}
