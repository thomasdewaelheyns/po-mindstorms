/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

/**
 *
 * @author MHGameWork
 */
public interface IPacketHandler {
  
  void receive(int packetID, byte[] dgram);
}
