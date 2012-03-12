package penoplatinum.agent;

/**
 * MQRunner
 * 
 * Demo for our MQ class
 *
 * Author: Team Platinum
 */
import penoplatinum.Config;

public class MQRunner {

  public static void main(String[] argv) throws java.io.IOException,
          java.lang.InterruptedException {
    if (argv.length == 0) {
      argv = new String[]{"MQRunner"};
    }

    MQ mq = new MQ() {

      protected void handleIncomingMessage(String message) {
        // handling the incoming messages ...
        System.out.println(message);
      }
    }.connectToMQServer(Config.MQ_SERVER).follow(Config.GHOST_CHANNEL);

    // be nice ...
    mq.sendMessage("Hello everybody.");

    // our actual event loop
    System.out.println("Waiting for messages. To exit press CTRL+C");
    while (true) {
      Thread.sleep(100);
      mq.sendMessage("Some message");
    }
  }
}
