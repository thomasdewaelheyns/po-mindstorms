package penoplatinum.gateway;

/**
 * MessageReceiver
 *
 * Interface for classes that accepts messages.
 *
 * @author Team Platinum
 */

public interface MessageReceiver {
  public void receive(String message);
}
