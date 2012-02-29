public class MessageSpy implements MessageHandler {
  public void useQueue(Queue queue) {}

  public void receive(String msg) {
    System.out.println(msg);
  }
}
