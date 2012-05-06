package penoplatinum.simulator;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import penoplatinum.gateway.Queue;
import penoplatinum.gateway.MessageReceiver;


public class SimulatedMQ implements Queue {
  private List<MessageReceiver> subscribers = new ArrayList<MessageReceiver>();

  private static Logger logger = Logger.getLogger("SimulatedMQ");

  private static SimulatedMQ singleton = new SimulatedMQ();

  private SimulatedMQ() {}

  public static SimulatedMQ getInstance() {
    return singleton;
  }

  // used by an agent to subscribe to the queue
  public SimulatedMQ subscribe(MessageReceiver receiver) {
    this.subscribers.add(receiver);
    return this;
  }

  // used by an agent to send a message
  public SimulatedMQ send(String msg) {
    logger.info(msg);
    for(MessageReceiver subscriber : this.subscribers) {
      subscriber.receive(msg);
    }
    return this;
  }
}
