
import java.util.Scanner;

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
  
  public void send(String message){
    this.queue.send(message+"\n");
  }
  
  public static void main(String[] args) throws java.lang.InterruptedException {
    MQSpy spy = new MQSpy(args);

    System.out.println("Waiting for messages. To exit press CTRL+C");
    spy.connect();
    Scanner sc = new Scanner(System.in);
    while (true) { 
      if(sc.hasNextLine()){
        spy.send(sc.nextLine());
      }
      Thread.sleep(100); }
  }
}
