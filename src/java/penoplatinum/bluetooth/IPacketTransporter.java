/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

/**
 * A packet transporter for sending and receiving packets.
 * You have to Register this transporter with this instance, for the types 
 * of packets you which to receive/send with this transporter
 * 
 * @author: Team Platinum
 */
public interface IPacketTransporter {

    void onPacketReceived(int packetIdentifier, byte[] dgram, int offset, int length);
    
}
