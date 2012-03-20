/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

import penoplatinum.grid.Sector;
import penoplatinum.simulator.mini.MessageHandler;
import penoplatinum.simulator.mini.Queue;

/**
 * This interface represents the functionalties provided by the communication 
 * protocol
 * @author MHGameWork
 */
public interface ProtocolHandler extends MessageHandler {

  void receive(String msg);

  void sendBarcode(int code, int bearing);

  void sendDiscover(Sector sector);

  void sendPacman();

  void sendPosition();

  void useQueue(Queue queue);
  
}
