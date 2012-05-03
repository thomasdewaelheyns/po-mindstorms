package penoplatinum.gateway;

/**
 * MQRunner
 * 
 * Demo for our MQ class
 *
 * Author: Team Platinum
 */
import java.io.IOException;
import java.util.Scanner;
import penoplatinum.Config;

public class MQRunner {

  public static void main(String[] argv) throws IOException, InterruptedException {
    if (argv.length == 0) {
      argv = new String[]{"MQRunner"};
    }
    Config.load("../../src/java/robot.properties");

    Queue queue = new MQ().connectToMQServer(Config.MQ_SERVER).follow(Config.GHOST_CHANNEL);
    queue.subscribe(new MessageReceiver() {

      @Override
      public void receive(String message) {
        System.out.println("MSG: "+message);
      }
    });

    // our actual event loop
    System.out.println("Waiting for messages. To exit press CTRL+C");
    Scanner sc = new Scanner(System.in);
    while (true) {
      if(sc.hasNextLine()){
        queue.send(sc.nextLine());
      }
    }
  }
}
