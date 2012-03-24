package penoplatinum.pacman;

/**
 * This interface represents the functionalties provided by the communication 
 * protocol
 * 
 * @author Team Platinum
 */

import penoplatinum.grid.Sector;

import penoplatinum.gateway.GatewayClient;


public interface ProtocolHandler {
  ProtocolHandler useGatewayClient(GatewayClient client);
  
  void receive(String msg);

  void sendBarcode(int code, int bearing);
  void sendDiscover(Sector sector);
  void sendPacman();
  void sendPosition();
}
