package penoplatinum.gateway;

public interface Queue {
  public Queue setMessageReceiver(MessageReceiver receiver);
  public Queue sendMessage(String message);
}
