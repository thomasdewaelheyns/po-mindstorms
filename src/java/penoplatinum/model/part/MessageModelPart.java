package penoplatinum.model.part;

/**
 * Holds the inbox and outbox for the ghost communication channel,
 * provides an implementation of the Ghost protocol
 * 
 * @author Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import penoplatinum.model.Model;

import penoplatinum.protocol.ProtocolHandler;


public class MessageModelPart implements ModelPart {
  // boilerplate implementation required to register and retrieve a ModelPart
  // from the model
  public static MessageModelPart from(Model model) {
    return (MessageModelPart) model.getPart(ModelPartRegistry.MESSAGE_MODEL_PART);
  }

  // two queue-like lists for in- and out-goinging messages
  private List<String> inbox = new ArrayList<String>();
  private List<String> outbox = new ArrayList<String>();

  // one handler for the protocol
  private ProtocolHandler protocolHandler;


  public void addIncomingMessage(String msg) {
    synchronized (this) {
      this.inbox.add(msg);
    }
  }

  public List<String> getIncomingMessages() {
    List<String> messages = new ArrayList<String>();
    synchronized (this) {
      messages.addAll(this.inbox);
      this.inbox.clear();
    }
    return messages;
  }

  public void addOutgoingMessage(String msg) {
    synchronized (this) {
      this.outbox.add(msg);
    }
  }

  public List<String> getOutgoingMessages() {
    List<String> messages = new ArrayList<String>();
    synchronized (this) {
      messages.addAll(this.outbox);
      this.outbox.clear();
    }
    return messages;
  }

  public ProtocolHandler getProtocolHandler() {
    return this.protocolHandler;
  }

  public void setProtocolHandler(ProtocolHandler protocolHandler) {
    this.protocolHandler = protocolHandler;
  }

}
