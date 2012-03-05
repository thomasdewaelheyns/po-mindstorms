package penoplatinum.simulator.mini;

import java.util.List;
import java.util.ArrayList;

public class Queue {
  private List<MessageHandler> subscribers = new ArrayList<MessageHandler>();

  // used by an agent to subscribe to the queue
  public Queue subscribe(MessageHandler handler) {
    this.subscribers.add(handler);
    handler.useQueue(this);
    return this;
  }

  // used by an agent to send a message
  public void send(String msg) {
    for(MessageHandler subscriber : this.subscribers) {
      subscriber.receive(msg);
    }
  }
}
