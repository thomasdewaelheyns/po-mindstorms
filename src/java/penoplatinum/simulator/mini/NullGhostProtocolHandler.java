/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator.mini;

import penoplatinum.gateway.GatewayClient;
import penoplatinum.grid.Sector;
import penoplatinum.pacman.ProtocolHandler;

/**
 *
 * @author MHGameWork
 */
public class NullGhostProtocolHandler implements ProtocolHandler {

  @Override
  public void receive(String msg) {
  }

  @Override
  public void sendBarcode(int code, int bearing) {
  }

  @Override
  public void sendDiscover(Sector sector) {
  }

  @Override
  public void sendPacman() {
  }

  @Override
  public void sendPosition() {
  }

  @Override
  public ProtocolHandler useGatewayClient(GatewayClient client) {
    return this;
  }
  
}
