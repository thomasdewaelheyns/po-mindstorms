package penoplatinum.gateway;

public interface Queue {
  public Queue subscribe(MessageReceiver receiver);
  public Queue send(String message);
}
