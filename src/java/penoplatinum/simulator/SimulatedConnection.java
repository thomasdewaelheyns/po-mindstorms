package penoplatinum.simulator;

/**
 * SimulatedConnection
 * 
 * implements the Connection interface with a local buffer and provides an
 * interface to send messages
 * 
 * @author: Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import penoplatinum.gateway.Connection;

public class SimulatedConnection implements Connection {
  // inner class to store channel+message info
  private class ChannelMessage {
    public int channel;
    public String message;
    public ChannelMessage(int channel, String message) {
      this.channel = channel;
      this.message = message;
    }
  }

  private SimulatedGatewayClient client;
  private List<ChannelMessage> messages = new ArrayList<ChannelMessage>();
  
  private ChannelMessage nextMessage;

  
  public SimulatedConnection(SimulatedGatewayClient client) {
    this.client = client;
  }
  
  // NOTICE: we're discarting the information about the channel, because
  //         we know we're only receiving command through one channel.
  //         other more valid explanation = we're parsing them anyway ;-)
  public Connection send(String msg, int channel) {
    this.client.receive(msg);
    return this;
  }
  
  // this is used by the SimulatedGatewayClient to send outgoing messages
  public Connection sendToGateway(String msg, int channel) {
    this.messages.add(new ChannelMessage(channel, msg));
    return this;
  }

  public boolean hasNext() {
    return this.messages.size() > 0;
  }

  public String getMessage() {
    this.nextMessage = this.messages.remove(0);
    return this.nextMessage.message;
  }
  
  public int getType() {
    return this.nextMessage.channel;
  }
}
