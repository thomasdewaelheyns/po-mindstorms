/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

/**
 *
 * @author: Team Platinum
 */
public interface IConnection {

     
     /**
      * Registers given transporter with given packetIdentifier.
      * The transporter now receives all packets with given identifier,
      * and can send packets with cet identifier.
      * @param transporter
      * @param packetIdentifier 
      */
     void RegisterTransporter(IPacketTransporter transporter, int packetIdentifier);
     
     
     
     void SendPacket(IPacketTransporter transporter, int packetIdentifier,byte[] dgram );
     
}
