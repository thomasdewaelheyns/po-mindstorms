/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

/**
 *
 * @author: Team Platinum
 */
public interface IPacketReceiver {

    void onPacketReceived(int packetIdentifier, byte[] dgram, int size);
    void onError(Exception ex);
}
