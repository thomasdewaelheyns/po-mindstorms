/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * A packet transporter for sending and receiving packets.
 * You have to Register this transporter with this instance, for the types 
 * of packets you which to receive/send with this transporter
 * 
 * @author MHGameWork
 */
public interface IPacketTransporter {

    DataInputStream getReceiveStream();

    DataOutputStream getSendStream();

    /**
     * Blocks until a packet is available. The packet identifier is returned, 
     * and the packet is available for reading on the Receive stream.
     */
    int ReceivePacket();

    /**
     * Returns the oldest packet in the received packet queue.
     * If no packet is available this function immediately returns -1;
     * The packet identifier is returned, 
     * and the packet is available for reading on the Receive stream.
     */
    int ReceiveAvailablePacket();

    /**
     * Sends a packet with given identifier. (non-blocking)
     * The data written to the SendStream since the last call to SendPacket is sent.
     */
    void SendPacket(int packetIdentifier);
    
    
    
    
    
    
    
    void onPacketReceived(int packetIdentifier, byte[] dgram, int offset, int length);
    
    
    
}
