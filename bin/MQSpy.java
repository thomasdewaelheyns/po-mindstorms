/**
 * MQSpy
 * 
 * Connects to an MQ channel and "spies"
 *
 * Author: Team Platinum
 */

public class MQSpy extends BaseRunner {
  public MQSpy(String[] args) {
    super(args, "mj");
  }

  public void connect() {
    this.queue.subscribe(this);
  }
  
  public void receive(String message) {
    System.out.println(message.trim());
  }
  
  public static void main(String[] args) throws java.lang.InterruptedException {
    MQSpy spy = new MQSpy(args);

    System.out.println("Waiting for messages. To exit press CTRL+C");
    spy.connect();
    while (true) { Thread.sleep(100); }
  }
}
