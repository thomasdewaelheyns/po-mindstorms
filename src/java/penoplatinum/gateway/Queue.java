package penoplatinum.gateway;

/**
 * Queue
 *
 * Interface for queue-based communication.
 *
 * @author Team Platinum
 */

public interface Queue {
  public Queue subscribe(MessageReceiver receiver);
  public Queue send(String message);
}
