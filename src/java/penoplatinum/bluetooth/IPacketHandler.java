package penoplatinum.bluetooth;

/**
 *
 * @author MHGameWork
 */
public interface IPacketHandler {
  
  void receive(int packetID, byte[] dgram);
}
