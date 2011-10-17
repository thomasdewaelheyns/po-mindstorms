/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;


/**
 *
 * @author MHGameWork
 */
public class SimpleConnection  implements IConnection {
    public void setEndPoint(SimpleConnection endPoint)
    {
    }
    
    public void acceptPacket(int packetIdentifier, byte[] dgram)
    {
        
    }

    @Override
    public void RegisterTransporter(IPacketTransporter transporter, int packetIdentifier) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void SendPacket(IPacketTransporter transporter, int packetIdentifier, byte[] dgram) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
