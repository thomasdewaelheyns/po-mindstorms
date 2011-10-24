/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.util.HashMap;


/**
 *
 * @author MHGameWork
 */
public class SimpleConnection  implements IConnection {
    private SimpleConnection endPoint;
    private PacketBuilder builder;
    
    private HashMap<Integer, IPacketTransporter> map = new HashMap<Integer, IPacketTransporter>();
    
    
    public void setEndPoint(SimpleConnection endPoint)
    {
        this.endPoint = endPoint;
    }
    
    public void acceptPacket(int packetIdentifier, byte[] dgram)
    {
        map.get(packetIdentifier).onPacketReceived(packetIdentifier, dgram);
    }

    @Override
    public void RegisterTransporter(IPacketTransporter transporter, int packetIdentifier) {
        map.put(packetIdentifier, transporter);
    }

    @Override
    public void SendPacket(IPacketTransporter transporter, int packetIdentifier, byte[] dgram) {
        endPoint.acceptPacket(packetIdentifier, dgram);
    }
    
    
}
