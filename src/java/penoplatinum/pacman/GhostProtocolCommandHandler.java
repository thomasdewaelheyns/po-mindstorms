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

  void handleBarcodeAt(String agentName, int x, int y, int code, int bearing);

  void handleDiscover(String agentName, int x, int y, int n, int e, int s, int w);

  void handlePosition(String agentName, int x, int y);

  public void handlePacman(String agentName, int x, int y);
  
}
