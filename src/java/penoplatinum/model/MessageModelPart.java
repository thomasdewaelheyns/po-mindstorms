/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

import java.util.ArrayList;
import java.util.List;
import penoplatinum.pacman.ProtocolHandler;

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
  
  private ProtocolHandler protocol;
  
  
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

  public ProtocolHandler getProtocol() {
    return protocol;
  }

  public void setProtocol(ProtocolHandler protocol) {
    this.protocol = protocol;
  }


  
  public void clearOutbox() {
    this.outbox.clear();
  }

  public void queueOutgoingMessage(String msg) {
    outbox.add(msg);
  }

}
