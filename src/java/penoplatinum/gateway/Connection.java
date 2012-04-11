package penoplatinum.gateway;

/**
 * Connection
 *
 * Interface for message-based communication. Hides technical details about
 * messagequeues or bluetooth connections.
 *
 * @author Team Platinum
 */


public interface Connection {
  public Connection send(String msg, int channel);

  public boolean hasNext();
  public String  getMessage();
  public int     getType();
}
