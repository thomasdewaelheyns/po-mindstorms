/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

/**
 *
 * @author MHGameWork
 */
public interface IPacketReceiver {

    void onPacketReceived(int packetIdentifier, byte[] dgram, int size);
}
