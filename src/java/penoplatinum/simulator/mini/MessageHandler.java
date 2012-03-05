package penoplatinum.simulator.mini;

public interface MessageHandler {
  public void useQueue(Queue queue);
  public void receive(String msg);
}
