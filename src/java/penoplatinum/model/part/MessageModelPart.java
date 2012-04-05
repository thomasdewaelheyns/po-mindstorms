package penoplatinum.model;

/**
 * Holds the inbox and outbox for the ghost communication channel,
 * provides an implementation of the Ghost protocol
 * 
 * @author Team Platinum
 */

import java.util.ArrayList;
import java.util.List;

import penoplatinum.pacman.ProtocolHandler;

public class MessageModelPart implements IModelPart {
  // two queue-like lists for in- and out-goinging messages
  private List<String> inbox = new ArrayList<String>();
  private List<String> outbox = new ArrayList<String>();

  // one handler for the protocol
  private ProtocolHandler protocol;


  public MessageModelPart() {}

  // This is thread safe
  public void addIncomingMessage(String msg) {
    synchronized (this) {
      this.inbox.add(msg);
    }
  }

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

  @Override
  public void clearDirty() {
    
  }
}
