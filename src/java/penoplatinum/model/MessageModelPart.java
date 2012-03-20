/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

import java.util.ArrayList;
import java.util.List;
import penoplatinum.pacman.GhostProtocolHandler;
import penoplatinum.pacman.GhostProtocolModelCommandHandler;
import penoplatinum.simulator.mini.MessageHandler;
import penoplatinum.simulator.mini.Queue;

/**
 * Holds the inbox and outbox for the ghost communication channel,
 * provides an implementation of the Ghost protocol
 * 
 * @author MHGameWork
 */
public class MessageModelPart {
  // two queue-like lists for in- and out-goinging messages
  private List<String> inbox = new ArrayList<String>();
  private List<String> outbox = new ArrayList<String>();
  
  private GhostProtocolHandler protocol;
  
  
  public MessageModelPart()
  {
   
  }
  
  /**
   * This is thread safe
   * @param msg 
   */
  public void addIncomingMessage(String msg) {
    synchronized (this) {
      this.inbox.add(msg);
    }
  }

  /**
   * @param buffer 
   */
  public void receiveIncomingMessages(List<String> buffer) {
    synchronized (this) {
      buffer.addAll(inbox);
      inbox.clear();
    }
  }

  public List<String> getOutgoingMessages() {
    return this.outbox;
  }

  public GhostProtocolHandler getProtocol() {
    return protocol;
  }

  public void setProtocol(GhostProtocolHandler protocol) {
    this.protocol = protocol;
  }


  
  public void clearOutbox() {
    this.outbox.clear();
  }

  public void queueOutgoingMessage(String msg) {
    outbox.add(msg);
  }

}
