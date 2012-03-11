/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

/**
 *
 * @author MHGameWork
 */
public interface GhostProtocolCommandHandler {

  void handleBarcode(String agentName, int code, int bearing);

  void handleDiscover(String agentName, int x, int y, int n, int e, int s, int w);

  void handlePosition(String agentName, int x, int y);
  
}
